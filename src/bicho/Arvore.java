package bicho;

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
