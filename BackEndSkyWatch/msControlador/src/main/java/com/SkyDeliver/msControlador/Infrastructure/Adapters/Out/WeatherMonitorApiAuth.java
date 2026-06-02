package com.SkyDeliver.msControlador.Infrastructure.Adapters.Out;

import com.SkyDeliver.msControlador.Application.Ports.out.WeatherMonitorApi;
import com.SkyDeliver.msControlador.Domain.Point;

import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class WeatherMonitorApiAuth implements WeatherMonitorApi {


    @Override
    public Map<String, Object> getWeatherAuth(List<Point> puntos) {
        // Construir el body: { "punto1": {lat, lon}, "punto2": {lat, lon}, ... }
        Map<String, Object> body = new LinkedHashMap<>();
        for (int i = 0; i < puntos.size(); i++) {
            body.put("punto" + (i + 1), puntos.get(i));
        }

        // Llamar al API y retornar la respuesta directamente
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("llego hasta aqui");
        System.out.println("hola");

        Map<String, Object> res = restTemplate.postForObject(
                "http://msweathermonitor:8083/apiRutas/meteorologico",
                body,
                Map.class
        );

        return res;
    }


    public static void main(String[] args) {
        List<Point> puntos = List.of(
                new Point(1, 1),
                new Point(1, 1),
                new Point(1, 1),
                new Point(1, 1)
        );

        // Instanciar la clase que implementa el metodo
        WeatherMonitorApiAuth service = new WeatherMonitorApiAuth();

        Map<String, Object> response = service.getWeatherAuth(puntos);

        System.out.println("Respuesta: " + response);
        System.out.println("Autorización: " + response.get("autorizacion"));
    }

}
