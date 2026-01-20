public class Frutta {
    private String nome;
    private int quantita;

    public Frutta(String nome, int quantita) {
        this.nome = nome;
        this.quantita = quantita;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantita() {
        return quantita;
    }

    @Override
    public String toString() {
        return nome + " " + quantita;
    }
}
