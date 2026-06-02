package com.skyWatch.msProof.Infrastructure.Adapters.Out;


import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "deliveries")
public class DeliveryEntity {

    @Id
    private String id;

    private int routeId;
    private int droneId;
    private String email;
    private String image;

    public DeliveryEntity() {
    }

    public DeliveryEntity(int routeId, int droneId, String email, String image) {
        this.routeId = routeId;
        this.droneId = droneId;
        this.email = email;
        this.image = image;
    }

    public String getId() {
        return id;
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
