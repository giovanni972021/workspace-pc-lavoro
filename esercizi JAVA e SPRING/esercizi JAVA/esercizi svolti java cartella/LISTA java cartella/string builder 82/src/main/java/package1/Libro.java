
public class Libro {
    private String titolo;
    private String autore;
    private double prezzo;
    private int annoPubblicazione;

    public Libro(String titolo, String autore, double prezzo, int annoPubblicazione) {
        if (titolo == null || titolo.isBlank()) {
            throw new IllegalArgumentException("Il titolo non può essere vuoto");
        }
        if (autore == null || autore.isBlank()) {
            throw new IllegalArgumentException("L'autore non può essere vuoto");
        }
        if (prezzo < 0 || annoPubblicazione < 0) {
            throw new IllegalArgumentException("Prezzo e anno non possono essere negativi");
        }

        this.titolo = titolo;
        this.autore = autore;
        this.prezzo = prezzo;
        this.annoPubblicazione = annoPubblicazione;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{\n")

                .append("  Titolo: '").append(titolo).append("',\n")
                .append("  Autore: '").append(autore).append("'\n")
                .append("  Prezzo: '").append(prezzo).append("'\n")
                .append("  Anno pubblicazione: '").append(annoPubblicazione).append("'\n")

                .append("}\n")
                .toString();
    }

}