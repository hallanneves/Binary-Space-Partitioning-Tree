package bicho;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class Arvore {
    Arvore frente, tras;
    Poligono pivo;
    
    public Arvore(ArrayList<Poligono> poligonos) {
        ArrayList<Poligono> arrayfrente,arraytras;
        arrayfrente = new ArrayList<>();
        arraytras = new ArrayList<>();
        pivo = poligonos.get(new Random().nextInt(poligonos.size()));
        poligonos.remove(pivo);
        ArrayList<Poligono> novo = new ArrayList<>();
        for (Poligono p: poligonos) {
            if(false){//pivo.equacao().temInterseccao(p.equacao())){
                double xmin = Math.min(p.x1, p.x2);
                double xmax = Math.max(p.x1, p.x2);
                double ymin = Math.min(p.y1, p.y2);
                double ymax = Math.max(p.y1, p.y2);
                double xintersec = pivo.equacao().xInterseccao(p.equacao());
                double yintersec = pivo.equacao().yInterseccao(p.equacao());
                Poligono p1 = new Poligono(xmin, ymin, xintersec, yintersec, p.angulo, p.cor.darker());
                Poligono p2 = new Poligono(xintersec, yintersec, xmax, ymax, p.angulo, p.cor.brighter());
                novo.add(p1);
                novo.add(p2);
                System.out.println("Dividiu o poligono p = ("+p.x1+","+p.y1+")("+p.x2+","+p.y2+")");
                System.out.println("P1 = x1 = "+xmin+" y1 = "+ymin+" x2 = "+xintersec+" y2 = "+yintersec);
                System.out.println("P2 = x1 = "+xintersec+" y1 = "+yintersec+" x2 = "+xmax+" y2 = "+ymax);
                
            }else{
                novo.add(p);
            }
        }
        poligonos = novo;
        for (Poligono p: poligonos) {
            
            if(pivo.estaAFrente(p)){
                arrayfrente.add(p);
            }else{
                arraytras.add(p);
            }
        }
        if (!arrayfrente.isEmpty()){
            frente = new Arvore(arrayfrente);
        }else{
            frente = null;
        }
        
        if(!arraytras.isEmpty()){
            tras = new Arvore(arraytras);
        }else{
            tras = null;
        }
    }
    
   public void printTree(OutputStreamWriter out) throws IOException {
        if (frente != null) {
            frente.printTree(out, true, "");
        }
        printNodeValue(out);
        if (tras != null) {
            tras.printTree(out, false, "");
        }
    }
   
    private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
        if (frente != null) {
            frente.printTree(out, true, indent + (isRight ? "        " : " |      "));
        }
        System.out.print(indent);
        if (isRight) {
            System.out.print(" /");
        } else {
            System.out.print(" \\");
        }
        System.out.print("----- ");
        printNodeValue(out);
        if (tras != null) {
            tras.printTree(out, false, indent + (isRight ? " |      " : "        "));
        }
    }
    
    private void printNodeValue(OutputStreamWriter out) throws IOException {
        if (pivo == null) {
            System.out.print("<null>");
        } else {
            System.out.print(pivo.cor.toString().substring(14));
        }
        System.out.print('\n');
    }
    
}
