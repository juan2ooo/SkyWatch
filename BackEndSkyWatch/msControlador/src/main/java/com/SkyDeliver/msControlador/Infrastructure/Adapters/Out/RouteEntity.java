package com.SkyDeliver.msControlador.Infrastructure.Adapters.Out;


import com.SkyDeliver.msControlador.Domain.NoFlyZone;
import com.SkyDeliver.msControlador.Domain.Point;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ruta")
@NoArgsConstructor

public class RouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lon", column = @Column(name = "start_lon")),
            @AttributeOverride(name = "lat", column = @Column(name = "start_lat"))
    })
    private Point start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lon", column = @Column(name = "end_lon")),
            @AttributeOverride(name = "lat", column = @Column(name = "end_lat"))
    })
    private Point end;

    public RouteEntity(int id, Point start, Point end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }



    //Getters

    public int getId() {
        return id;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }




    //Setters


    public void setId(int id) {
        this.id = id;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }


}
