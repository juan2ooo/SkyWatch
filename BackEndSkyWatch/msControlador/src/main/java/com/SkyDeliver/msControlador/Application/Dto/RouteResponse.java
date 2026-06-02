package com.SkyDeliver.msControlador.Application.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteResponse {
    public int id;
    public String status;
    public double start_lon;
    public double start_lat;
    public double end_lon;
    public double end_lat;
    public Map<String,Object> tiempo;

    public RouteResponse(int id, String status, double start_lon, double start_lat, double end_lon, double end_lat, Map<String, Object> tiempo) {
        this.id = id;
        this.status = status;
        this.start_lon = start_lon;
        this.start_lat = start_lat;
        this.end_lon = end_lon;
        this.end_lat = end_lat;
        this.tiempo = tiempo;
    }
}
