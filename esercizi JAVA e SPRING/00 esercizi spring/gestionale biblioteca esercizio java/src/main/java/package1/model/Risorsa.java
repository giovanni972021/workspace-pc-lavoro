package model;

// Classe astratta Risorsa

public abstract class Risorsa {
    /*
     * tutte le classi figlie di risorsa dovranno avere titolo risorsa e anno
     * pubblicazione
     */
    private final String titoloRisorsa;
    private final int annoPubblicazione;

    // costruttore risorsa
    public Risorsa(String titoloRisorsa, int annoPubblicazione) {
        this.titoloRisorsa = titoloRisorsa;
        this.annoPubblicazione = annoPubblicazione;
    }

    // get di tutte le variabili di risorsa
    public String getTitoloRisorsa() {
        return titoloRisorsa;
    }

    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    // to string per stampare meglio il risultato
    @Override
    public String toString() {
        return "Titolo: " + titoloRisorsa + "  Anno: " + annoPubblicazione;
    }
}