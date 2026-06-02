package com.SkyDeliver.msControlador.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una coordenada geográfica.
 *
 * lat = latitud
 * lon = longitud
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    /**
     * Latitud del punto.
     */
    private double lat;

    /**
     * Longitud del punto.
     */
    private double lon;

}
