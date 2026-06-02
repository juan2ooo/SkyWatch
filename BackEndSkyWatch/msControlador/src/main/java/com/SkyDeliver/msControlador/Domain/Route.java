package com.SkyDeliver.msControlador.Domain;
import com.SkyDeliver.msControlador.Domain.Exceptions.InvalidZoneNoFlyException;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Representa una ruta aérea recta entre dos puntos.
 *
 * La ruta NO almacena puntos intermedios.
 * Se modela matemáticamente como un segmento
 * de línea entre start y end.
 */
@Getter
@Setter
public class Route {
    private Integer id;

    /**
     * Punto inicial.
     */
    private final Point start;

    /**
     * Punto destino.
     */
    private final Point end;

    Map<String,Object> tiempo;

    /**
     * Construye una ruta y valida
     * que no atraviese zonas restringidas.
     *
     * @param start punto inicial
     * @param end punto destino
     * @param noFlyZones zonas restringidas
     */
    public Route(
            Integer id,
            Point start,
            Point end,
            List<NoFlyZone> noFlyZones
    ) {

        this.id = Objects. requireNonNullElse(id,0);

        this.start = start;
        this.end = end;

        validateRoute(noFlyZones);
    }

    /**
     * Valida si la línea de la ruta
     * intersecta alguna No-Fly Zone.
     *
     * @param noFlyZones zonas restringidas
     */
    private void validateRoute(
            List<NoFlyZone> noFlyZones
    ) {

        for (NoFlyZone zone : noFlyZones) {

            if (zone.intersects(start, end)) {

                throw new InvalidZoneNoFlyException(
                        "Ruta inválida: atraviesa una No-Fly Zone"
                );
            }
        }
    }



    /**
     * Genera puntos intermedios entre el punto inicial (start)
     * y el punto final (end) usando interpolación lineal.
     *
     * La lista retornada NO incluye ni el punto start ni el end.
     *
     * Ejemplo:
     *
     * START ---- P1 ---- P2 ---- END
     *
     * @param intermediatePoints cantidad de puntos intermedios
     * @return lista de puntos generados entre start y end
     */
    public List<Point> generatePoints(int intermediatePoints) {

        List<Point> points = new ArrayList<>();

        // Coordenadas del punto inicial
        double lat1 = start.getLat();
        double lon1 = start.getLon();

        // Coordenadas del punto final
        double lat2 = end.getLat();
        double lon2 = end.getLon();


        // Número total de segmentos en la línea
        // Ejemplo:
        // 2 puntos intermedios => 3 segmentos
        int totalSegments = intermediatePoints + 1;

        // Generar puntos intermedios
        for (int i = 1; i <= intermediatePoints; i++) {

            // Valor entre 0 y 1 que indica
            // la posición relativa sobre la línea
            double factor = (double) i / totalSegments;

            // Interpolación lineal de latitud
            double lat = lat1 + (lat2 - lat1) * factor;

            // Interpolación lineal de longitud
            double lon = lon1 + (lon2 - lon1) * factor;

            // Crear y agregar punto generado
            points.add(new Point(lat, lon));
        }

        return points;
    }



}