package com.SkyDeliver.msControlador.Application.Ports.out;

import com.SkyDeliver.msControlador.Domain.NoFlyZone;

import java.util.List;

public interface ApiNoFlyZones {


    public List<NoFlyZone> obtenerNoFlyZones(String city);
}
