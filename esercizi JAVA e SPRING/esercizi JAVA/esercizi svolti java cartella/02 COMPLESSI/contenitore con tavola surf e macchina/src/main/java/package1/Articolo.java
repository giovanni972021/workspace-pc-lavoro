//Articolo.java ASTRATTA
public abstract class Articolo {

    private String marca;

    public Articolo(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    // Metodo astratto per stampare i dettagli dell'articolo
    public abstract void stampaDettagli();
}
