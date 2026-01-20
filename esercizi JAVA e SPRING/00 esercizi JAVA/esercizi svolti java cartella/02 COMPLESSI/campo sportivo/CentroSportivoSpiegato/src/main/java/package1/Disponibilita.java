public class Disponibilita {

    private final int inizio;
    private final int fine;
    private boolean disponibile;

    public Disponibilita(int inizio, int fine) {
        this.inizio = inizio;
        this.fine = fine;
        this.disponibile = true;
    }

    public int getInizio() {
        return inizio;
    }

    public int getFine() {
        return fine;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void prenota() {
        disponibile = false;
    }

    public void libera() {
        disponibile = true;
    }

    @Override
    public String toString() {
        return (disponibile ? "LIBERO  " : "OCCUPATO") + " " + inizio + "-" + fine;
    }
}
