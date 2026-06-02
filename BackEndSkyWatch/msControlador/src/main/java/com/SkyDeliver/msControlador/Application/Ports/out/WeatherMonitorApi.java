package com.SkyDeliver.msControlador.Application.Ports.out;

import com.SkyDeliver.msControlador.Domain.Point;

import java.util.List;
import java.util.Map;

public interface WeatherMonitorApi {
    public Map<String, Object > getWeatherAuth(List<Point>puntos);
}
