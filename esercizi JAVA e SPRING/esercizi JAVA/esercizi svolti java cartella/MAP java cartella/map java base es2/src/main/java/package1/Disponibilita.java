package main.java.package1;

// Classe per rappresentare la disponibilità di una sala
public class Disponibilita {
    private String sala;
    private boolean disponibile;

    public Disponibilita(String sala, boolean disponibile) {
        this.sala = sala;
        this.disponibile = disponibile;
    }

    public String getSala() {
        return sala;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    @Override
    public String toString() {
        return sala + " (" + (disponibile ? "Libera" : "Occupata") + ")";
    }
}
