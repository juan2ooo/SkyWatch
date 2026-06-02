package com.SkyDeliver.msControlador.Application.Ports.In;

import com.SkyDeliver.msControlador.Application.Dto.DeleteMessage;
import com.SkyDeliver.msControlador.Domain.Route;

import java.util.List;

public interface RouteOpUseCase {
    public Route newRoute(double lon1, double lat1, double lon2, double lat2);
    public List<Route> getAll();
    public Route getById(int id);
    public void delete(DeleteMessage mensaje) ;
}
