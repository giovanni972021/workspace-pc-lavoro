package main.java;

public class Risorsa {
    private final String nome;
    private final int eta;

    public Risorsa(String nome, int eta) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome della risorsa non può essere vuoto");
        }
        if (eta < 0) {
            throw new IllegalArgumentException("L'età della risorsa non può essere negativa");
        }
        this.nome = nome;
        this.eta = eta;
    }

    public String getNome() {
        return nome;
    }

    public int getEta() {
        return eta;
    }

    @Override
    public String toString() {
        return nome + " " + eta;
    }
}
