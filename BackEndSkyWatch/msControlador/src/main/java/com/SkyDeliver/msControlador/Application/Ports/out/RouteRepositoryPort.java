package com.SkyDeliver.msControlador.Application.Ports.out;


import com.SkyDeliver.msControlador.Domain.NoFlyZone;
import com.SkyDeliver.msControlador.Domain.Route;

import java.util.List;
import java.util.Map;

public interface RouteRepositoryPort {

    public Route save(Route route, Map<String,Object> tiempo);
    public List<Route> getAll();
    public Route getById(int id);
    public void delete(int id);
    
}
