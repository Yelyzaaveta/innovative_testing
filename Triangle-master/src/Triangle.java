import java.util.Arrays;

public class Triangle {
    private double a;
    private double b;
    private double c;

    public double getA() {
        return a;
    }

    public void setA(double a) {
        if (a <= 0) throw new IllegalArgumentException("Side must be positive");
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        if (b <= 0) throw new IllegalArgumentException("Side must be positive");
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        if (c <= 0) throw new IllegalArgumentException("Side must be positive");
        this.c = c;
    }

    public Triangle(double a, double b, double c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new IllegalArgumentException("Sides must be positive");
        }
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Invalid triangle sides");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle() {
    }

    public double perimeter() {
        return a + b + c;
    }

    public double area() {
        double s = perimeter() / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public boolean isEquilateral() {
        return a == b && b == c;
    }

    @Override
    public String toString() {
        return String.format("Triangle{a=%s, b=%s, c=%s}", a, b, c);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triangle other)) return false;
        return Arrays.equals(sortedSides(), other.sortedSides());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sortedSides());
    }

    private double[] sortedSides() {
        double[] sides = {a, b, c};
        Arrays.sort(sides);
        return sides;
    }
}
