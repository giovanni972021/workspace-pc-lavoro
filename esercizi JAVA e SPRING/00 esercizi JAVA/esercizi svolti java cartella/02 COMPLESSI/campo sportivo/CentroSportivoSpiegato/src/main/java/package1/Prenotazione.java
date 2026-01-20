import java.time.LocalDate;

public class Prenotazione {

    private final String nomeCliente;
    private final int inizio;
    private final int fine;
    private final LocalDate data;
    private final TipoCampo tipoCampo;

    public Prenotazione(String nomeCliente, int inizio, int fine,
            LocalDate data, TipoCampo tipoCampo) {
        this.nomeCliente = nomeCliente;
        this.inizio = inizio;
        this.fine = fine;
        this.data = data;
        this.tipoCampo = tipoCampo;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public int getInizio() {
        return inizio;
    }

    public int getFine() {
        return fine;
    }

    public LocalDate getData() {
        return data;
    }

    public TipoCampo getTipoCampo() {
        return tipoCampo;
    }

    @Override
    public String toString() {
        return nomeCliente + " | " + tipoCampo + " | " + data + " | " + inizio + "-" + fine;
    }
}
