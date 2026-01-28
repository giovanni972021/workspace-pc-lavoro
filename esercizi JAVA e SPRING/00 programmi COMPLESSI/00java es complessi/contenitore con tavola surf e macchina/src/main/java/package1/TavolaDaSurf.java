//TavolaDaSurf.java
public class TavolaDaSurf extends Articolo {

    private double lunghezza;

    public TavolaDaSurf(String marca, double lunghezza) {
        super(marca);
        this.lunghezza = lunghezza;
    }

    public double getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(double lunghezza) {
        this.lunghezza = lunghezza;
    }

    @Override
    public void stampaDettagli() {
        System.out.printf("Tavola da Surf - Marca: %-15s Lunghezza: %.2f m%n", getMarca(), lunghezza);
    }
}
