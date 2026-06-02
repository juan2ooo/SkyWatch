package com.SkyDeliver.msControlador.Infrastructure.Adapters.In;

import com.SkyDeliver.msControlador.Application.Dto.DeleteMessage;
import com.SkyDeliver.msControlador.Application.Dto.RouteResponse;
import com.SkyDeliver.msControlador.Application.Ports.In.RouteOpUseCase;
import com.SkyDeliver.msControlador.Domain.Exceptions.InvalidZoneNoFlyException;
import com.SkyDeliver.msControlador.Domain.Route;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiRutas/rutas")
public class RouteControllerEndPoints {

    private final RouteOpUseCase routeUseCase;

    public RouteControllerEndPoints(RouteOpUseCase routeUseCase) {
        this.routeUseCase = routeUseCase;
    }


    @PostMapping
    public ResponseEntity<RouteResponse> create(@RequestBody Map<String, Double> body) {
        try {

            Route route = routeUseCase.newRoute(
                    body.get("lon1"),
                    body.get("lat1"),
                    body.get("lon2"),
                    body.get("lat2")
            );

            RouteResponse response;
            response = new RouteResponse(route.getId(), "Todo ok", route.getStart().getLon(), route.getStart().getLat(), route.getEnd().getLon(), route.getEnd().getLat(), route.getTiempo());
            return ResponseEntity.ok(response);
        }catch (InvalidZoneNoFlyException e){
            RouteResponse r = new RouteResponse(0,e.getMessage(), 0, 0, 0, 0, null);
            return ResponseEntity.badRequest().body(r);
        }catch (Exception e){
            RouteResponse r = new RouteResponse(0,e.getMessage(), 0, 0, 0, 0, null);
            return ResponseEntity.badRequest().body(r);
        }

    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAll() {
        try {
            List<Route> routes = routeUseCase.getAll();
            List<RouteResponse> response = routes.stream()
                    .map(r -> new RouteResponse(r.getId(), "OK", r.getStart().getLon(), r.getStart().getLat(), r.getEnd().getLon(), r.getEnd().getLat(), null))
                    .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getById(@PathVariable int id) {
        Route route = routeUseCase.getById(id);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new RouteResponse(route.getId(), "OK", route.getStart().getLon(), route.getStart().getLat(), route.getEnd().getLon(), route.getEnd().getLat(), route.getTiempo()));
    }

    @RabbitListener(queues = "cola.delete")
    public void consumir(DeleteMessage message) {

        try{
            // Le pasas el objeto entero, sin desarmarlo
            routeUseCase.delete(message);
        } catch (RuntimeException e) {
            System.out.println("problema: " + e);
            throw new RuntimeException(e);
        }


    }


}