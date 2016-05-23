package bicho;

import java.awt.*;

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
    
    public void DrawFrontal(Graphics g,int x,int y){
        int tamanho = 5000;
        int x1Proj, x2Proj;
        if (!visivel(x1, y1) && !visivel(x2,y2)){
            return;
        }
        x1Proj = (int)projetaXDoPontoNaTela(x1,y1);
        x2Proj = (int)projetaXDoPontoNaTela(x2,y2);
        
        int[] arrayx = {x1Proj,x2Proj,x2Proj,x1Proj};
        /*System.out.println("Poligono: x -> ");
        for (int i = 0; i < arrayx.length; i++) {
            System.out.print(" "+arrayx[i]);
        }*/
        
        double d1 = distancia(Bicho.xObservador,Bicho.yObservador,x1,y1);
        double d2 = distancia(Bicho.xObservador,Bicho.yObservador,x2,y2);
        int[] arrayy = {(int) (250 +(tamanho/d1)),(int) (250 + tamanho/d2),(int) (250 - tamanho/d2),(int) (250 - tamanho/d1)};
        /*System.out.println(" y -> ");
        for (int i = 0; i < arrayy.length; i++) {
            System.out.print(" "+arrayy[i]);
        }
        System.out.println("");*/
        g.setColor(cor);
        g.fillPolygon(arrayx, arrayy ,4);
        
    }
    public static double distancia(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }
    
    public static int projetaXDoPontoNaTela(double x, double y){
        double xVetorObservador, yVetorObservador;
        xVetorObservador = Math.cos(Math.toRadians(Bicho.anguloObservador-45));
        yVetorObservador = Math.sin(Math.toRadians(Bicho.anguloObservador-45));
        double xVetorObjeto, yVetorObjeto;
        xVetorObjeto = x-Bicho.xObservador;
        yVetorObjeto = y - Bicho.yObservador;
        double angulo = anguloEntreVetores(xVetorObjeto, yVetorObjeto, xVetorObservador, yVetorObservador);
        //System.out.println("angulo que foi calculado "+angulo);
        if (xVetorObservador*yVetorObjeto - xVetorObjeto*yVetorObservador < 0){
            return  -1;
        }
        if(angulo >90.0){
            return 501;
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
