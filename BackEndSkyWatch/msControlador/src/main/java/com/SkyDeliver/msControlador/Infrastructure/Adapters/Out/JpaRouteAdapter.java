package com.SkyDeliver.msControlador.Infrastructure.Adapters.Out;


import com.SkyDeliver.msControlador.Application.Ports.out.RouteRepositoryPort;
import com.SkyDeliver.msControlador.Domain.NoFlyZone;
import com.SkyDeliver.msControlador.Domain.Route;
import com.SkyDeliver.msControlador.Infrastructure.Mappers.RouteMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class JpaRouteAdapter implements RouteRepositoryPort {
    private SpringRouteRepository repository;

    public JpaRouteAdapter(SpringRouteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Route save(Route route, Map<String,Object> tiempo) {
        RouteEntity entity = RouteMapper.toEntity(route);
        Route domain = RouteMapper.toDomain(repository.save(entity),List.of());
        domain.setTiempo(tiempo);
        return domain; //realmente ya no importa las zonas restringidas, ta se valido
    }

    @Override
    public List<Route> getAll() {
        return repository.findAll().stream()
                .limit(50)
                .map(entity -> RouteMapper.toDomain(entity, List.of()))
                .toList();
    }

    @Override
    public Route getById(int id) {
        return repository.findById(id)
                .map(entity -> RouteMapper.toDomain(entity, List.of()))
                .orElse(null);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }


}
