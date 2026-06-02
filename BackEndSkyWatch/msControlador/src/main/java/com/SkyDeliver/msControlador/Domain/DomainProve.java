package com.SkyDeliver.msControlador.Domain;

import java.util.List;

/**
 * Clase de prueba para validar
 * la generación de rutas.
 */
public class DomainProve {

    public static void main(String[] args) {

        /*
         * Punto de inicio.
         */
        Point start =
                new Point(6.2442, -75.5812);

        /*
         * Punto destino.
         */
        Point end =
                new Point(6.2500, -75.5700);

        /*
         * Zona restringida de ejemplo.
         */
        NoFlyZone airportZone =
                new NoFlyZone(
                        new Point(6.2470, -75.5760),
                        0.002
                );

        /*
         * Lista de zonas prohibidas.
         */
        List<NoFlyZone> noFlyZones =
                List.of(airportZone);

        try {

            /*
             * Genera la ruta.
             */
            Route route = new Route(1,start, end, noFlyZones);


            System.out.println("Ruta válida");
            System.out.println();

            /*
             * Imprime los puntos
             * de la trayectoria.
             */


        } catch (RuntimeException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }
}