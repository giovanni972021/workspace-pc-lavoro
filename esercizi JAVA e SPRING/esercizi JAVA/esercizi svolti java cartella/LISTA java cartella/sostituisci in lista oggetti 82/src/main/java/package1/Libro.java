import java.util.Objects;

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
        return "Titolo: " + titolo + ", Autore: " + autore + ", Prezzo: " + prezzo + ", Anno: " + annoPubblicazione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Libro libro = (Libro) o;
        return Double.compare(libro.prezzo, prezzo) == 0 &&
                annoPubblicazione == libro.annoPubblicazione &&
                Objects.equals(titolo, libro.titolo) &&
                Objects.equals(autore, libro.autore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, autore, prezzo, annoPubblicazione);
    }
}