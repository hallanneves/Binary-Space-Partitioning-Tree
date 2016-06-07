package bicho;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Bicho {
    
    static ArrayList<Poligono> poligonos; 
    public static Arvore arvore;
    public static OutputStreamWriter out;
    public static JFrame frame;
    public static JPanel painelFrontal, painelSuperior;
    public static int xObservador=250 ,yObservador=550,anguloObservador=270;
    public static Line2D limiteVisaoDireita, limiteVisaoEsquerda;

    private static void geraPoligonos(){
        poligonos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Random r = new Random();
            Color cor = new Color(0.3f+r.nextFloat()*0.5f,0.3f+r.nextFloat()*0.5f,0.3f+r.nextFloat()*0.5f);
            Poligono p = new Poligono(50+r.nextDouble()*400.0,50+r.nextDouble()*400.0f,25+r.nextDouble()*100,r.nextDouble()*360.0,cor);
            poligonos.add(p);
        }
        /*poligonos.add(new Poligono(200, 200, 150, 95, Color.blue));
        poligonos.add(new Poligono(300, 230, 150, 0, Color.red));
        poligonos.add(new Poligono(200, 100, 150, 30, Color.green));
        */

        
    }
    
    private static void drawObservador(Graphics g){
        g.setColor(Color.black);
        g.drawOval(xObservador-10, yObservador-10, 20, 20);
        g.drawLine(xObservador, yObservador, (int) (xObservador+100*Math.cos(Math.toRadians((double)(anguloObservador-45)))), (int) (yObservador+100*Math.sin(Math.toRadians((double)(anguloObservador-45)))));
        g.drawLine(xObservador, yObservador, (int) (xObservador+100*Math.cos(Math.toRadians((double)(anguloObservador+45)))), (int) (yObservador+100*Math.sin(Math.toRadians((double)(anguloObservador+45)))));

    }

    private static void drawFrontalArvore(Arvore a, Graphics g) throws Exception {
        if (a.pivo.estaAFrente(xObservador, yObservador)) {
            if (a.tras != null) {
                drawFrontalArvore(a.tras, g);
            }
            a.pivo.DrawFrontal(g, xObservador, yObservador);
            if (a.frente != null) {
                drawFrontalArvore(a.frente, g);
            }
        } else {
            if (a.frente != null) {
                drawFrontalArvore(a.frente, g);
            }
            a.pivo.DrawFrontal(g, xObservador, yObservador);
            if (a.tras != null) {
                drawFrontalArvore(a.tras, g);
            }
        }
    }

    private static void drawSuperiorArvore(Arvore a,Graphics g){
        drawObservador(g);
        if(a.tras!=null){
            drawSuperiorArvore(a.tras, g);
        }
        a.pivo.draw(g);
        if(a.frente!= null){
            drawSuperiorArvore(a.frente, g);
        }
    }
    
    public static void inicializaJanela(){
        frame = new JFrame();
        painelSuperior = new JPanel();
        painelFrontal = new JPanel();
        frame.setLayout(null);
        frame.setSize(1045, 560);
        painelSuperior.setBounds(10, 10, 510, 510);
        frame.add(painelSuperior);
        frame.addKeyListener(new tratador());
        painelFrontal.setBounds(520, 10, 1020, 510);
        frame.add(painelFrontal);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
    }

    public static void main(String[] args) throws InterruptedException, IOException, Exception {
        inicializaJanela();
        geraPoligonos();

        BufferedImage imgSup = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgFront = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB);

        Graphics gSup = imgSup.getGraphics();
        Graphics gFront = imgFront.getGraphics();

        Graphics gPainelSup = painelSuperior.getGraphics();
        Graphics gPainelFront = painelFrontal.getGraphics();
        
        //Gera o teste inicial
        arvore = new Arvore((ArrayList<Poligono>)poligonos.clone());
        out = new OutputStreamWriter(System.out);
        arvore.printTree(out);
        
        while(true){
            //Desenha a vista Superior
            gSup.setColor(Color.WHITE);
            gSup.fillRect(0, 0, 500, 500);
            drawSuperiorArvore(arvore, gSup);
            //Desenha a vista Frontal
            gFront.setColor(Color.WHITE);
            gFront.fillRect(0, 0, 500, 500);
            gFront.setColor(Color.LIGHT_GRAY);
            gFront.fillRect(0,260,500,500);
            drawFrontalArvore(arvore, gFront);
            gPainelSup.drawImage(imgSup,0,0,null);
            gPainelFront.drawImage(imgFront,0,0,null);
            Thread.sleep(200);
        }
    }
    
    public static class tratador implements KeyListener{

        @Override
        public void keyTyped(KeyEvent ke) {
            if(ke.getKeyChar() == 't'){
                try {
                    geraPoligonos();
                    arvore = new Arvore((ArrayList<Poligono>)poligonos.clone());
                    arvore.printTree(out);
                } catch (IOException ex) {
                    Logger.getLogger(Bicho.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            double passo = 10.0;
            if (ke.getKeyChar() == 'a') {
                xObservador += Math.cos(Math.toRadians(anguloObservador - 90)) * passo;
                yObservador += Math.sin(Math.toRadians(anguloObservador - 90)) * passo;
            }
            if (ke.getKeyChar() == 'd') {
                xObservador += Math.cos(Math.toRadians(anguloObservador + 90)) * passo;
                yObservador += Math.sin(Math.toRadians(anguloObservador + 90)) * passo;
            }
            if (ke.getKeyChar() == 'w') {
                xObservador += Math.cos(Math.toRadians(anguloObservador)) * passo;
                yObservador += Math.sin(Math.toRadians(anguloObservador)) * passo;
            }
            if (ke.getKeyChar() == 's') {
                xObservador += Math.cos(Math.toRadians(anguloObservador + 180)) * passo;
                yObservador += Math.sin(Math.toRadians(anguloObservador + 180)) * passo;
            }
            if (ke.getKeyChar() == 'q'){
                anguloObservador -=10;
            }
            if (ke.getKeyChar() == 'e'){
                anguloObservador +=10;
            }
            System.out.println(anguloObservador);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {   
        }
    }
    
    
}
