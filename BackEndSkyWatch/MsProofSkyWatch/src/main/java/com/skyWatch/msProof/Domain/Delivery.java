package com.skyWatch.msProof.Domain;

public class Delivery {

    private int routeId;
    private int droneId;
    private String email;
    private String image;

    public Delivery() {
    }

    public Delivery(int routeId, int droneId, String email, String image) {
        this.routeId = routeId;
        this.droneId = droneId;
        this.email = email;
        this.image = image;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getDroneId() {
        return droneId;
    }

    public void setDroneId(int droneId) {
        this.droneId = droneId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
