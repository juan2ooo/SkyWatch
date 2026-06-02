package com.SkyDeliver.msControlador.Domain;

/**
 * Representa una zona aérea restringida
 * modelada como un círculo.
 */
public class NoFlyZone {

    /**
     * Centro de la zona.
     */
    private final Point center;

    /**
     * Radio de la zona.
     */
    private final double radius;

    /**
     * Construye una zona restringida.
     *
     * @param center centro del círculo
     * @param radius radio del círculo
     */
    public NoFlyZone(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Determina si el segmento formado
     * por start y end intersecta
     * esta zona restringida.
     *
     * @param start punto inicial
     * @param end punto final
     * @return true si existe intersección
     */
    public boolean intersects(Point start, Point end) {

        // Coordenadas del punto inicial
        double x1 = start.getLat();
        double y1 = start.getLon();

        // Coordenadas del punto final
        double x2 = end.getLat();
        double y2 = end.getLon();

        // Coordenadas del centro
        double cx = center.getLat();
        double cy = center.getLon();

        // Vector AB
        double abx = x2 - x1;
        double aby = y2 - y1;

        // Vector AC
        double acx = cx - x1;
        double acy = cy - y1;

        // Magnitud cuadrada de AB
        double abSquared = (abx * abx) + (aby * aby);

        // Proyección escalar
        double t = ((acx * abx) + (acy * aby)) / abSquared;

        // Limitar la proyección al segmento
        if (t < 0) {
            t = 0;
        } else if (t > 1) {
            t = 1;
        }

        // Punto más cercano al centro
        double px = x1 + (t * abx);
        double py = y1 + (t * aby);

        // Vector desde P hasta el centro
        double dx = cx - px;
        double dy = cy - py;

        // Distancia cuadrada
        double distanceSquared = (dx * dx) + (dy * dy);

        // Verificar intersección
        return distanceSquared <= (radius * radius);
    }
}