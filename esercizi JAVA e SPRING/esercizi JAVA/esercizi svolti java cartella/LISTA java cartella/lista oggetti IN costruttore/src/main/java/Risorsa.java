package main.java;

public class Risorsa {
    private final String nome;
    private final int quantita;

    public Risorsa(String nome, int quantita) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome della risorsa non può essere vuoto");
        }
        if (quantita < 0) {
            throw new IllegalArgumentException("L'età della risorsa non può essere negativa");
        }
        this.nome = nome;
        this.quantita = quantita;
    }

    public String getNome() {
        return nome;
    }

                               public int getquantita() {
        return quantita;
    }

    @Override
    public String toString() {
        return nome + " " + quantita;
    }
}
