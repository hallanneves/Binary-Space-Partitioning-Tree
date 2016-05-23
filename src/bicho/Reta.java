package bicho;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
public class Reta {
    public Line2D linha, linhaInfinita;


    public Reta(Poligono p){
        linha = new Line2D.Double(p.x1, p.y1, p.x2, p.y2);
        linhaInfinita = new Line2D.Double(p.x + Math.cos(Math.toRadians(p.angulo + 90)) * 10000,
                p.y + Math.sin(Math.toRadians(p.angulo + 90)) * 10000,
                p.x - Math.cos(Math.toRadians(p.angulo + 90)) * 10000,
                p.y - Math.sin(Math.toRadians(p.angulo + 90)) * 10000);
    }

    @Override
    public String toString() {
        return linha.toString();
    }

    public boolean temInterseccao(Reta r){
        return linhaInfinita.intersectsLine(r.linha);
    }
    
    public double xInterseccao(Reta r){
        return getIntersectionPoint(linhaInfinita, r.linha).getX();
    }
    
    public double yInterseccao(Reta r){
        return getIntersectionPoint(linhaInfinita, r.linha).getY();
    }


    public Point2D.Double getIntersectionPoint(Line2D line1, Line2D line2) {
        if (!line1.intersectsLine(line2)) return null;
        double px = line1.getX1(),
                py = line1.getY1(),
                rx = line1.getX2() - px,
                ry = line1.getY2() - py;
        double qx = line2.getX1(),
                qy = line2.getY1(),
                sx = line2.getX2() - qx,
                sy = line2.getY2() - qy;

        double det = sx * ry - sy * rx;
        if (det == 0) {
            return null;
        } else {
            double z = (sx * (qy - py) + sy * (px - qx)) / det;
            if (z == 0 || z == 1) return null;  // intersection at end point!
            return new Point2D.Double(
                    (float) (px + z * rx), (float) (py + z * ry));
        }
    }
}
