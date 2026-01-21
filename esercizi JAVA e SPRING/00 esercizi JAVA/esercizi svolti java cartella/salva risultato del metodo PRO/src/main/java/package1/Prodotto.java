import java.time.LocalDate;

//dichiaro variabili
public class Prodotto {
    private int id;
    private String nome;
    private int quantita;
    private double prezzo;
    private LocalDate scadenza;

    public Prodotto() {
    }

    public Prodotto(int id, String nome, int quantita, double prezzo, LocalDate scadenza) {
        this.id = id;
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.scadenza = scadenza;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public LocalDate getScadenza() {
        return scadenza;
    }

    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{\n")
                .append("  ID: ").append(id).append(",\n")
                .append("  Nome: '").append(nome).append("',\n")
                .append("  Quantità: ").append(quantita).append(",\n")
                .append("  Prezzo: ").append(prezzo).append(" €,\n")
                .append("  Scadenza: ").append(scadenza).append("\n")
                .append("}")
                .toString();
    }
}