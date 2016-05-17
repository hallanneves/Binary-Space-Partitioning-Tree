package bicho;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.Position;

public class Bicho {
    
    static ArrayList<Poligono> poligonos; 
    public static Arvore arvore;
    public static OutputStreamWriter out;
    public static JFrame frame;
    public static JPanel painelFrontal, painelSuperior;
    public static int xObservador=250 ,yObservador=550,anguloObservador=270;

    private static void geraPoligonos(){
        poligonos = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Random r = new Random();
            Color cor = new Color(0.3f+r.nextFloat()*0.5f,0.3f+r.nextFloat()*0.5f,0.3f+r.nextFloat()*0.5f);
            Poligono p = new Poligono(50+r.nextDouble()*400.0,50+r.nextDouble()*400.0f,25+r.nextDouble()*100,r.nextDouble()*360.0,cor);
            poligonos.add(p);
        }
        
    }
    
    private static void drawSuperior(Graphics g){       
        drawObservador(g);
        for (Poligono poligono : poligonos) {
            poligono.draw(g);
        }
    }
    
    private static void drawObservador(Graphics g){
        g.setColor(Color.black);
        g.drawOval(xObservador-10, yObservador-10, 20, 20);
        g.drawLine(xObservador, yObservador, (int) (xObservador+100*Math.cos(Math.toRadians((double)(anguloObservador-45)))), (int) (yObservador+100*Math.sin(Math.toRadians((double)(anguloObservador-45)))));
        g.drawLine(xObservador, yObservador, (int) (xObservador+100*Math.cos(Math.toRadians((double)(anguloObservador+45)))), (int) (yObservador+100*Math.sin(Math.toRadians((double)(anguloObservador+45)))));

    }
        
    private static void drawFrontalArvore(Arvore a,Graphics g){
        if(a.tras!=null){
            drawFrontalArvore(a.tras,g);
        }
        a.pivo.DrawFrontal(g,xObservador, yObservador);
        if(a.frente!= null){
            drawFrontalArvore(a.frente, g);
        }
    }
    
    public static void inicializaJanela(){
        frame = new JFrame();
        painelSuperior = new JPanel();
        painelFrontal = new JPanel();
        frame.setLayout(null);
        frame.setSize(1100, 600);
        painelSuperior.setBounds(10, 10, 510, 510);
        frame.add(painelSuperior);
        frame.addKeyListener(new tratador());
        painelFrontal.setBounds(520, 10, 1020, 510);
        frame.add(painelFrontal);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
    }
    
    public static void main(String[] args) throws InterruptedException, IOException {
        inicializaJanela();
        geraPoligonos();
        
        Graphics gSup = painelSuperior.getGraphics();
        Graphics gFront = painelFrontal.getGraphics();
        
        //Gera o teste inicial
        arvore = new Arvore((ArrayList<Poligono>)poligonos.clone());
        out = new OutputStreamWriter(System.out);
        arvore.printTree(out);
        
        while(true){
            //Desenha a vista Superior
            gSup.setColor(Color.WHITE);
            gSup.fillRect(0, 0, 500, 500);
            drawSuperior(gSup);
            //Desenha a vista Frontal
            gFront.setColor(Color.WHITE);
            gFront.fillRect(0, 0, 500, 500);
            drawFrontalArvore(arvore, gFront);
            
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
            
            if (ke.getKeyChar() == 'a')
                xObservador-=10;
            if(ke.getKeyChar() == 'd')
                xObservador+=10;
            if(ke.getKeyChar() == 'w')
                yObservador-=10;
            if(ke.getKeyChar() == 's')
                yObservador+=10;
            if (ke.getKeyChar() == 'q'){
                anguloObservador -=10;
            }
            if (ke.getKeyChar() == 'e'){
                anguloObservador +=10;
            }
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {   
        }
    }
    
    
}
