package com.SkyDeliver.msControlador.Infrastructure.Configuration;


import com.SkyDeliver.msControlador.Application.Ports.In.RouteOpUseCase;
import com.SkyDeliver.msControlador.Application.Ports.out.ApiNoFlyZones;
import com.SkyDeliver.msControlador.Application.Ports.out.RouteRepositoryPort;
import com.SkyDeliver.msControlador.Application.Service.RouteUseCase;
import com.SkyDeliver.msControlador.Domain.NoFlyZone;
import com.SkyDeliver.msControlador.Infrastructure.Adapters.Out.NoFlyZonesApiController;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;


@Configuration
public class AppConfig {

    @Bean
    public RouteOpUseCase routeOpUseCase(RouteRepositoryPort routeRepositoryPort, Cache<String, List<NoFlyZone>> cache, RabbitTemplate rabbitTemplate){
        ApiNoFlyZones controllerApi = new NoFlyZonesApiController(cache);
        return new RouteUseCase(routeRepositoryPort, controllerApi, rabbitTemplate);
    }

    @Bean
    public Cache<String, List<NoFlyZone>> noFlyZonesCache() {
        return Caffeine.newBuilder().build();
    }

}
