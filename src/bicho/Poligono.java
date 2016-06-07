package bicho;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Poligono {
    public double x,y,tamanho,angulo;
    public Color cor;
    double x1,x2,y1,y2;

    public Poligono(double x, double y, double tamanho, double angulo, Color cor) {
        this.x = x;
        this.y = y;
        this.tamanho = tamanho;
        this.angulo = angulo;
        this.cor = cor;
        double anguloRad = Math.toRadians(angulo-90);
        x1 = (x+Math.cos(anguloRad)*tamanho/2);
        x2 = (x-Math.cos(anguloRad)*tamanho/2);
        y1 = (y+Math.sin(anguloRad)*tamanho/2);
        y2 = (y-Math.sin(anguloRad)*tamanho/2);
    }
    
    public Poligono(double x1, double y1, double x2, double y2, double angulo, Color cor){
        this.tamanho = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-x1, 2));
        this.cor = cor;
        this.angulo = angulo;
        this.x = (Math.min(x1, x2) + Math.max(x1, x2)) / 2;
        this.y = (Math.min(y1, y2) + Math.max(y1, y2)) / 2;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    
    public boolean estaAFrente(Poligono p){
        return (p.y-y)-Math.tan(Math.toRadians(angulo+90))*(p.x-x) > 0;
    }

    public boolean estaAFrente(int x, int y) {
        return (y - this.y) - Math.tan(Math.toRadians(angulo + 90)) * (x - this.x) > 0;
    }

    public Reta equacao(){
        return new Reta(this);
    }

    public void draw(Graphics g){
        g.setColor(cor);
        g.drawLine( (int)x1, (int)y1, (int)x2, (int)y2);
        double anguloRad = Math.toRadians(angulo);
        g.drawOval((int) x1 - 5, (int) y1 - 5, 10, 10);
        g.drawOval((int) x2 - 10, (int) y2 - 10, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(cor.toString().substring(1),(int) x,(int) y);
        g.drawLine((int)x, (int)y, (int)(x+Math.cos(anguloRad)*20), (int)(y+Math.sin(anguloRad)*20));

    }

    public void DrawFrontal(Graphics g, int x, int y) throws Exception {
        int tamanho = 5000;
        int x1Proj, x2Proj;
        int steps = 20;
        int xStep = ((int) x2 - (int) x1) / steps;
        int yStep = ((int) y2 - (int) y1) / steps;
        boolean algumVisivel = false;

        for (double xi = x1, yi = y1, i = 0; i < steps; xi += xStep, yi += yStep, i++) {
            if (visivel(xi, yi)) {
                algumVisivel = true;
            }
        }
        if (!algumVisivel) {
            //System.out.println("ninguem visivel");
            return;
        }

        Point2D[] pontos = clipToView();

        x1Proj = projetaXDoPontoNaTela(pontos[0].getX(), pontos[0].getY());
        x2Proj = projetaXDoPontoNaTela(pontos[1].getX(), pontos[1].getY());
        
        int[] arrayx = {x1Proj,x2Proj,x2Proj,x1Proj};
        /*System.out.println("Poligono: x -> ");
        for (int i = 0; i < arrayx.length; i++) {
            System.out.print(" "+arrayx[i]);
        }*/

        double d1 = distancia(Bicho.xObservador, Bicho.yObservador, pontos[0].getX(), pontos[0].getY());
        double d2 = distancia(Bicho.xObservador, Bicho.yObservador, pontos[1].getX(), pontos[1].getY());
        int[] arrayy = {(int) (250 +(tamanho/d1)),(int) (250 + tamanho/d2),(int) (250 - tamanho/d2),(int) (250 - tamanho/d1)};
        /*System.out.println(" y -> ");
        for (int i = 0; i < arrayy.length; i++) {
            System.out.print(" "+arrayy[i]);
        }
        System.out.println("");*/
        g.setColor(cor);
        g.fillPolygon(arrayx, arrayy ,4);
        
    }

    public Point2D[] clipToView() {

        Point2D[] retorno = new Point2D[2];
        Reta retaParede = new Reta(this);
        double anguloDir = Math.toRadians(Bicho.anguloObservador + 45);
        double anguloEsq = Math.toRadians(Bicho.anguloObservador - 45);

        Line2D retaDireita = new Line2D.Double(Bicho.xObservador,
                Bicho.yObservador,
                Bicho.xObservador + Math.cos(anguloDir) * 1000,
                Bicho.yObservador + Math.sin(anguloDir) * 1000);

        Line2D retaEsquerda = new Line2D.Double(Bicho.xObservador,
                Bicho.yObservador,
                Bicho.xObservador + Math.cos(anguloEsq) * 1000,
                Bicho.yObservador + Math.sin(anguloEsq) * 1000);
        retorno[0] = clipPontoDireita(retaParede, retaDireita);
        retorno[1] = clipPontoEsquerda(retaParede, retaEsquerda);
        //System.out.println("projetou ("+x1+" "+y1+") ("+x2+" "+y2+" para "+retorno[0]+" e "+retorno[1]);
        return retorno;
    }

    public Point2D clipPontoDireita(Reta retaParede, Line2D reta) {
        if (retaParede.linha.intersectsLine(reta)) {
            return Reta.getIntersectionPoint(retaParede.linha, reta);
        } else {
            //nada elegante, mas eu testei e funciona
            if (visivel(x1, y1)) {
                return new Point2D.Double(x1, y1);
            } else {
                return new Point2D.Double(x2, y2);
            }
        }
    }

    public Point2D clipPontoEsquerda(Reta retaParede, Line2D reta) {
        if (retaParede.linha.intersectsLine(reta)) {
            return Reta.getIntersectionPoint(retaParede.linha, reta);
        } else {
            //nada elegante, mas eu testei e funciona
            if (visivel(x2, y2)) {
                return new Point2D.Double(x2, y2);
            } else {
                return new Point2D.Double(x1, y1);
            }
        }
    }

    public static double distancia(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    public static int projetaXDoPontoNaTela(double x, double y) throws Exception {
        double xVetorObservador, yVetorObservador;
        xVetorObservador = Math.cos(Math.toRadians(Bicho.anguloObservador-45));
        yVetorObservador = Math.sin(Math.toRadians(Bicho.anguloObservador-45));
        double xVetorObjeto, yVetorObjeto;
        xVetorObjeto = x-Bicho.xObservador;
        yVetorObjeto = y - Bicho.yObservador;
        double angulo = anguloEntreVetores(xVetorObjeto, yVetorObjeto, xVetorObservador, yVetorObservador);
        //System.out.println("angulo que foi calculado "+angulo);
        if (xVetorObservador * yVetorObjeto - xVetorObjeto * yVetorObservador < -1) {
            //throw new Exception("Fora da tela depois do clipping!");
            System.out.println("Isso nao pode acontecer");
        }
        if (angulo > 91.0) {
            //throw new Exception("Fora da tela depois do clipping!");
            System.out.println("Isso nao pode acontecer");
        }
        //System.out.println("projecao calculada para um angulo de "+angulo+" = "+(int)((500.0/90.0)*angulo));
        return (int)((500.0/90.0)*angulo);
    }
    
    public static boolean visivel(double x, double y){
        double xVetorObservador, yVetorObservador;
        xVetorObservador = Math.cos(Math.toRadians(Bicho.anguloObservador-45));
        yVetorObservador = Math.sin(Math.toRadians(Bicho.anguloObservador-45));
        double xVetorObjeto, yVetorObjeto;
        xVetorObjeto = x-Bicho.xObservador;
        yVetorObjeto = y - Bicho.yObservador;
        double angulo = anguloEntreVetores(xVetorObjeto, yVetorObjeto, xVetorObservador, yVetorObservador);
        return (xVetorObservador*yVetorObjeto - xVetorObjeto*yVetorObservador > 0 && angulo < 90);
    }
    
    //radianos
    public static double anguloEntreVetores(double x1, double y1, double x2, double y2){
        return Math.toDegrees(Math.acos((x1*x2 + y1*y2) / Math.sqrt(x1*x1 +y1*y1) * Math.sqrt(x2*x2 +y2*y2)));
    }
}
