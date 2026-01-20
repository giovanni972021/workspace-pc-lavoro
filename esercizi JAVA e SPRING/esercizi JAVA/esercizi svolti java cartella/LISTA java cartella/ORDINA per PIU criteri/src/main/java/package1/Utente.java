/*
creo attributi, i loro get, il costruttore , e to string
*/

public class Utente {

    private String nome;
    private int eta;
    private int altezza;

    public Utente(String nome, int eta, int altezza) {
        this.nome = nome;
        this.eta = eta;
        this.altezza = altezza;
    }

    public String getNome() {
        return nome;
    }

    public int getEta() {
        return eta;
    }

    public int getAltezza() {
        return altezza;
    }

    @Override
    public String toString() {
        // Formattazione migliorata per una lettura più chiara in console
        return String.format("Utente [Nome: %-8s | Età: %2d | Altezza: %3d cm]",
                nome, eta, altezza);
    }
}