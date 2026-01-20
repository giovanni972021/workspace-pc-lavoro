//Macchina.java
public class Macchina extends Articolo {

    private int porte;

    public Macchina(String marca, int porte) {
        super(marca);
        this.porte = porte;
    }

    public int getPorte() {
        return porte;
    }

    public void setPorte(int porte) {
        this.porte = porte;
    }

    @Override
    public void stampaDettagli() {
        System.out.printf("Macchina - Marca: %-15s Porte: %d%n", getMarca(), porte);
    }
}
