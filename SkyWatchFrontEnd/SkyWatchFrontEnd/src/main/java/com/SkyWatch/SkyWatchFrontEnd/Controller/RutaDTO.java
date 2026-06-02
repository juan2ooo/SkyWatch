package com.SkyWatch.SkyWatchFrontEnd.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RutaDTO {

    private Long id;
    private String status;

    @JsonProperty("start_lon")
    private Double startLon;

    @JsonProperty("start_lat")
    private Double startLat;

    @JsonProperty("end_lon")
    private Double endLon;

    @JsonProperty("end_lat")
    private Double endLat;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Double getStartLon() {
        return startLon;
    }

    public Double getStartLat() {
        return startLat;
    }

    public Double getEndLat() {
        return endLat;
    }

    public Double getEndLon() {
        return endLon;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartLon(Double startLon) {
        this.startLon = startLon;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public void setEndLon(Double endLon) {
        this.endLon = endLon;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }
}