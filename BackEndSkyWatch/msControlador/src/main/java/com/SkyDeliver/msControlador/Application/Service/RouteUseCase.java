package com.SkyDeliver.msControlador.Application.Service;

import com.SkyDeliver.msControlador.Application.Dto.DeleteMessage;
import com.SkyDeliver.msControlador.Application.Ports.In.RouteOpUseCase;
import com.SkyDeliver.msControlador.Application.Ports.out.ApiNoFlyZones;
import com.SkyDeliver.msControlador.Application.Ports.out.RouteRepositoryPort;
import com.SkyDeliver.msControlador.Application.Ports.out.WeatherMonitorApi;
import com.SkyDeliver.msControlador.Domain.Exceptions.WhetherException;
import com.SkyDeliver.msControlador.Domain.NoFlyZone;
import com.SkyDeliver.msControlador.Domain.Point;
import com.SkyDeliver.msControlador.Domain.Route;
import com.SkyDeliver.msControlador.Infrastructure.Adapters.Out.WeatherMonitorApiAuth;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.*;

public class RouteUseCase implements RouteOpUseCase {
    private RouteRepositoryPort repository;
    private ApiNoFlyZones controllerApi;
    private final RabbitTemplate rabbitTemplate;

    public RouteUseCase(RouteRepositoryPort repository, ApiNoFlyZones controllerApi,RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.controllerApi = controllerApi;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public Route newRoute(double lon1, double lat1, double lon2, double lat2) {

        //si crea la ruta es porque es valida
        List<NoFlyZone> noFlyZones = controllerApi.obtenerNoFlyZones("Medellín");
        Route route = new Route(null, new Point(lon1,lat1), new Point(lon2,lat2),noFlyZones);


        //ahora se mira el clima
        List<Point> puntos = route.generatePoints(4);
        WeatherMonitorApi api  = new WeatherMonitorApiAuth();
        Map<String,Object> tiempo = api.getWeatherAuth(puntos);

        if(tiempo.get("autorizacion").toString().equals("NA")){
            throw new WhetherException("Mal tiempo");
        };

        return repository.save(route,tiempo);


    }

    @Override
    public List<Route> getAll() {
        return repository.getAll();
    }

    @Override
    public Route getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void delete(DeleteMessage message) {

        // 1. Primero intenta mandar a la cola, si falla no borra en BD
        try {
            rabbitTemplate.convertAndSend("fotos.queue", message);
        } catch (AmqpException e) {
            System.out.print("Error al enviar mensaje a la cola, abortando delete: {}" + e.getMessage());
            throw new RuntimeException("No se pudo enviar a la cola, operación cancelada", e);
        }

        // 2. Solo borra en BD si la cola fue exitosa
        repository.delete(message.getIdRuta());
    }

}
