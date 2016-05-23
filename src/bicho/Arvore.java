package bicho;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Arvore {
    Arvore frente, tras;
    Poligono pivo;
    
    public Arvore(ArrayList<Poligono> poligonos) {
        ArrayList<Poligono> arrayfrente,arraytras;
        arrayfrente = new ArrayList<>();
        arraytras = new ArrayList<>();
        pivo = poligonos.get(0);//new Random().nextInt(poligonos.size()));
        poligonos.remove(pivo);
        ArrayList<Poligono> novo = new ArrayList<>();
        for (Poligono p: poligonos) {
            if (pivo.equacao().temInterseccao(p.equacao())) {
                double xmin, xmax, ymin, ymax;
                if (p.x1 >= p.x2) {
                    xmax = p.x1;
                    ymax = p.y1;
                    xmin = p.x2;
                    ymin = p.y2;
                } else {
                    xmax = p.x2;
                    ymax = p.y2;
                    xmin = p.x1;
                    ymin = p.y1;
                }

                double xintersec = pivo.equacao().xInterseccao(p.equacao());
                double yintersec = pivo.equacao().yInterseccao(p.equacao());
                Poligono p1 = new Poligono(xmin, ymin, xintersec, yintersec, p.angulo, p.cor.darker());
                Poligono p2 = new Poligono(xintersec, yintersec, xmax, ymax, p.angulo, p.cor.brighter());
                novo.add(p1);
                novo.add(p2);
                System.out.println("Dividiu o poligono p = (" + p.x + "," + p.y + "angulo = " + p.angulo);
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
