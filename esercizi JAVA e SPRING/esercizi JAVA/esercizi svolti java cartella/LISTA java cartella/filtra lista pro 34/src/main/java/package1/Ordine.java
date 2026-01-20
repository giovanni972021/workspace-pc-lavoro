public class Ordine {
    private String id;
    private double importo;
    private String stato;

    public Ordine(String id, double importo, String stato) {
        this.id = id;
        this.importo = importo;
        this.stato = stato;
    }

    public String getStato() {
        return stato;
    }

    public double getImporto() {
        return importo;
    }

    @Override
    public String toString() {
        return String.format("ID: %-8s | %6.2f€ | %s", id, importo, stato);
    }
}