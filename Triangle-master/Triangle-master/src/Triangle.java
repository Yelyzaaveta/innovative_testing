
public class Triangle {
    private double a;
    private double b;
    private double c;

    public double takeA() {
        return a;
    }

    public void putA(double a) {
        this.a = a;
    }

    public double takeB() {
        return b;
    }

    public void putB(double b) {
        this.b = b;
    }

    public double takeC() {
        return c;
    }

    public void putC(double c) {
        this.c = c;
    }

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle() {
    }

   public double perim() {
        return a + b + c;
   }

   public double area() {
        return Math.sqrt(0.5*perim()*(0.5*perim()-a)*(0.5*perim()-b)*(0.5*perim()-c));
   }

   public  boolean  equilateral(){
        if (a == b && b == c){
            return true;
        } else return false;

   }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Triangle triangle)) return false;

        return Double.compare(a, triangle.a) == 0 && Double.compare(b, triangle.b) == 0 && Double.compare(c, triangle.c) == 0;
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(a);
        result = 31 * result + Double.hashCode(b);
        result = 31 * result + Double.hashCode(c);
        return result;
    }
}
