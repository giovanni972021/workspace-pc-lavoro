package main.java.model;

public class Persona {
    private String nome;
    private int eta;

    public Persona(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    public String getNome() {
        return nome;
    }

    public int getEta() {
        return eta;
    }

    public void saluta() {
        System.out.println("Ciao, mi chiamo " + nome + " e ho " + eta + " anni.");
    }
}
