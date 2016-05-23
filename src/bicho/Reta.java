package bicho;
public class Reta {
    double a, b, x1, x2;

    public Reta(Poligono p){
        this.a = (p.y2 - p.y1) / (p.x2 - p.x1);
        this.b = p.y1 - a*p.x1;
        this.x1 = p.x1;
        this.x2 = p.x2;
    }
    
    public boolean temInterseccao(Reta r){
        double x = (r.b - b)/(a-r.a);
        return x > Math.min(x1,x2) && x < Math.max(x1, x2);
    }
    
    public double xInterseccao(Reta r){
        return (r.b - b)/(a-r.a);
    }
    
    public double yInterseccao(Reta r){
        return a * (r.b - b)/(a-r.a) + b;
    }
    
}
