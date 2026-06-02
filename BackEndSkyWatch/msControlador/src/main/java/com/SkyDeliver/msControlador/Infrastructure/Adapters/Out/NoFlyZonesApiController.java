package com.SkyDeliver.msControlador.Infrastructure.Adapters.Out;

import com.SkyDeliver.msControlador.Application.Ports.out.ApiNoFlyZones;
import com.SkyDeliver.msControlador.Domain.NoFlyZone;
import com.SkyDeliver.msControlador.Domain.Point;

import com.github.benmanes.caffeine.cache.Cache;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NoFlyZonesApiController implements ApiNoFlyZones {
    //private final RedisTemplate<String, Object> redisTemplate;


    private final Cache<String, List<NoFlyZone>> cache;

    public NoFlyZonesApiController(Cache<String, List<NoFlyZone>> cache) {
        this.cache = cache;
    }

    @Override
    public List<NoFlyZone> obtenerNoFlyZones(String city) {
        String cacheKey = "noFlyZones:" + city;

        @SuppressWarnings("unchecked")
        // 1. Revisar si ya está en caché
        List<NoFlyZone> cached = cache.getIfPresent(city);
        if (cached != null) {
            return cached;
        }

        // 2. Todo tu código original sin cambios...
        String query = """
    [out:json];
    area["name"="%s"]->.searchArea;
    (
      way["aeroway"="aerodrome"](area.searchArea);
      way["amenity"="hospital"](area.searchArea);
      way["landuse"="military"](area.searchArea);
      way["amenity"="prison"](area.searchArea);
      way["aeroway"="helipad"](area.searchArea);
    );
    out bb; 
    """.formatted(city);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(query, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://overpass-api.de/api/interpreter",
                HttpMethod.POST,
                entity,
                Map.class
        );

        List<NoFlyZone> zones = new ArrayList<>();
        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("elements")) {
            return zones;
        }

        List<Map<String, Object>> elements = (List<Map<String, Object>>) body.get("elements");

        for (Map<String, Object> element : elements) {
            Map<String, Object> bounds = (Map<String, Object>) element.get("bounds");
            if (bounds == null) continue;

            double minLat = ((Number) bounds.get("minlat")).doubleValue();
            double minLon = ((Number) bounds.get("minlon")).doubleValue();
            double maxLat = ((Number) bounds.get("maxlat")).doubleValue();
            double maxLon = ((Number) bounds.get("maxlon")).doubleValue();

            double centerLat = (minLat + maxLat) / 2.0;
            double centerLon = (minLon + maxLon) / 2.0;
            double radius = (maxLat - minLat) / 2.0;

            zones.add(new NoFlyZone(new Point(centerLon, centerLat), radius));
        }

        // 3. Guardar en Redis sin expiración
        cache.put(city, zones);

        return zones;
    }


    public static void main(String[] args) {
        System.out.println("Prueba del controlador");
        NoFlyZonesApiController controller = new NoFlyZonesApiController(null);
        controller.obtenerNoFlyZones("Medellín");
        // ...
    }


}
