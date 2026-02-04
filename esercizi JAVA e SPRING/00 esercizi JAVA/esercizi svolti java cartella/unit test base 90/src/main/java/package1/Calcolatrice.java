// File: Calcolatrice.java
public class Calcolatrice {

    public int somma(int a, int b) {
        return a + b; 
    }

    public double divisione(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Non puoi dividere per zero!");
        }
        return a / b;
    }
}