//creo classe prenotazione con attributi nome cliente e ora prenotazione e i loro get e set
//creo costruttore prenotazione contenente tutti gli attributi della classe prenotazione 
//creo costruttore prenotazione vuoto
class Prenotazione {
    private String nomeCliente;
    private int inizio;
    private int fine;


    public int getInizio() {
        return inizio;
    }

    public void setInizio(int inizio) {
        this.inizio = inizio;
    }



    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Prenotazione(String nomeCliente, int inizio, int fine) {
        this.fine = fine;
        this.nomeCliente = nomeCliente;
        this.inizio = inizio;
    }

    public Prenotazione() {
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }
}