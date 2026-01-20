package sottoClassi;

import model.Prestito;
import model.Risorsa;

// Sottoclasse Dvd
//classe Dvd è figlia di classe madre risorsa che implementa interfaccia prestito
public class Dvd extends Risorsa implements Prestito {
    // numero canzoni è variabile SOLO di Dvd
    private final int numeroCanzoni;

    // costruttore di Dvd dove metto sia variabili di risorsa sia variabili di Dvd
    public Dvd(String titoloRisorsa, int annoPubblicazione, int numeroCanzoni) {
        super(titoloRisorsa, annoPubblicazione);
        this.numeroCanzoni = numeroCanzoni;
    }

    // get SOLO delle variabili di Dvd
    public int getNumeroCanzoni() {
        return numeroCanzoni;
    }

    // metodo presta cosa deve stampare quando richiamato da classe Dvd?
    @Override
    public void presta() {
        System.out.println("🔵 Prestito Dvd:");
        /*
         * this messo cosi serve a stampare tostring stampa to string start, stampa
         * tostring start , this start, stampa this start
         */
        System.out.println(this);
    }

    @Override
    public void restituisci() {
        System.out.println("🟢 Restituzione Dvd:");
        System.out.println(this);
    }

    /*
     * to string , questa volta aggiungo SOLO le variabili presenti in questa classe
     * ossia numero canzoni
     */
    @Override
    public String toString() {
        return super.toString() + "  Numero canzoni: " + numeroCanzoni;
    }
}
