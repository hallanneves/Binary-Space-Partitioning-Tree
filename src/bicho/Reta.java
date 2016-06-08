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

    public static Point2D.Double getIntersectionPoint(Line2D line1, Line2D line2) {
        /*
            Cada linha (de p1 a p2) é decomposta como um ponto base(P) + um vetor R tal que P+R = P2
            Representando ambas retas como P+R e Q+S.
            Se Hover uma interseção, existe um par de escalares entre 0 e 1 T e U tal que P + TR = Q + US
            (se os escalares não estiverem neste intervalo a intersecção não acontece dentro dos segmentos de reta)
         */
        //Pontos base
        Point2D p = line1.getP1();
        Point2D q = line2.getP1();

        //vetores direcionais

        Point2D r = new Point2D.Double(line1.getX2() - line1.getX1(), line1.getY2() - line1.getY1());
        Point2D s = new Point2D.Double(line2.getX2() - line2.getX1(), line2.getY2() - line2.getY1());

        /*
        Queremos então encontrar U e T tal que
            P + tR = Q + uS
            Podemos aplicar o produto vetorial (x) com S nos dois lados sem alterar a igualdade
            (P + TR) x S = (Q + US) x S
            Como S x S = 0 (na verdade falamos aqui da magnitude do produto vetorial, visto que formalmente ele seria um vetor, não um escalar)
            (P + tR) x S = Q x S
            Eliminando U da equação, resta apenas uma variável para isolar
            (P + TR - Q) x S = 0
            t (R x S) = (P - Q) x S
            t = (P - Q) x S / (R x S)
            tendo T em mãos sabemos o ponto de intersecção (se houver)
         */

        double t = produtoVetorial(subtrai(q, p), s) / produtoVetorial(r, s);

        //se r e s são paralelos, não intersecção
        //se t for menor que 0 ou maior que 1, a intersecção está fora dos segmentos de reta
        if (Double.compare(produtoVetorial(r, s), 0.0) == 0 || t < 0.0 || t > 1.0) {
            return null;
        }

        return (Point2D.Double) soma(p, multiplica(r, t)); // Intersecção = P + TR


    }

    private static Point2D multiplica(Point2D a, double t) {
        return new Point2D.Double(a.getX() * t, a.getY() * t);
    }

    private static Point2D soma(Point2D a, Point2D b) {
        return new Point2D.Double(a.getX() + b.getX(), a.getY() + b.getY());
    }

    private static Point2D subtrai(Point2D a, Point2D b) {
        return new Point2D.Double(a.getX() - b.getX(), a.getY() - b.getY());
    }

    private static double produtoVetorial(Point2D v1, Point2D v2) {
        return v1.getX() * v2.getY() - v1.getY() * v2.getX();
    }

    public static Point2D.Double getIntersectionPointOld(Line2D line1, Line2D line2) {
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
