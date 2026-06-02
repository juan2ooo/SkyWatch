package com.SkyDeliver.msControlador.Infrastructure.Mappers;

import com.SkyDeliver.msControlador.Domain.NoFlyZone;
import com.SkyDeliver.msControlador.Domain.Route;
import com.SkyDeliver.msControlador.Infrastructure.Adapters.Out.RouteEntity;

import java.util.List;

public class RouteMapper {

    public static Route toDomain(RouteEntity routeEntity, List<NoFlyZone> noFlyZones){
        return new Route(routeEntity.getId(), routeEntity.getStart(), routeEntity.getEnd(), noFlyZones);
    }

    public static RouteEntity toEntity(Route route){
        return new RouteEntity(route.getId(), route.getStart(), route.getEnd());
    }
}
