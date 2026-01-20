package model;

// Classe Biblioteca
import java.util.ArrayList;
import java.util.List;

import sottoClassi.Dvd;
import sottoClassi.Libro;

public class Biblioteca {

    /*
     * crea lista di nome risorse disponibili che sara uno dei due contenitori
     * collegata alla classe
     * madre astratta di nome risorsa
     * 0crea lista start, 0nuova lista start, 0crea elenco start, 0crea contenitore
     * start,
     */
    private final List<Risorsa> risorseDisponibili = new ArrayList<>();
    private final List<Risorsa> risorseInPrestito = new ArrayList<>();

    /*
     * creo metodo di nome aggiungi risorsa con parametro variabile di nome risorsa
     * di tipo Risorsa dove Risorsa è nome della classe madre astratta
     */
    public void aggiungiRisorsa(Dvd dvd1) {
        /*
         * aggiungi elemento a lista risorse disponibili, 0aggiungi a lista start,
         * 0inserisci nell’ elenco start, 0inserisci nella lista start, 0aggiungi a
         * contenitore start, 0inserisci nel contenitore start, 0aggiungi alla lista
         * start,
         * 0aggiungi all elenco start, 0aggiungi a elenco start
         */
        risorseDisponibili.add(dvd1);
    }

    public void aggiungiRisorsa(Libro libro1) {
        risorseDisponibili.add(libro1);

    }

    public void rimuoviRisorsa(Risorsa risorsa) {
        risorseDisponibili.remove(risorsa);
        risorseInPrestito.remove(risorsa);
    }

    public void stampaRisorseDisponibili() {
        System.out.println("Risorse DISPONIBILI:");

        // se lista vuota start, elenco vuoto start, contenitore vuoto start
        if (risorseDisponibili.isEmpty()) {
            System.out.println("Nessuna risorsa disponibile.");
            return;
        }
        /*
         * stampa tutta la lista start, stampa lista start, stampa tutta lista start,
         * stampa elenco start,
         * stampa contenitore start
         * Risorsa nome della classe
         * risorse disponibili nome della lista
         */
        for (Risorsa r : risorseDisponibili) {
            System.out.println(r);
            System.out.println("-------------------------------");
        }
    }

    public void stampaRisorseInPrestito() {
        System.out.println("Risorse in PRESTITO:");

        if (risorseInPrestito.isEmpty()) {
            System.out.println("Nessuna risorsa in prestito.");
            return;
        }

        for (Risorsa r : risorseInPrestito) {
            System.out.println(r);
            System.out.println("-------------------------------");
        }
    }

    public void prestaRisorsa(Risorsa risorsa) {
        /*
         * se contenitore contiene elemento start, se lista contiene elemento start, se
         * lista contiene start
         * Prestito nome interfaccia, Risorsa nome classe astratta
         */
        if (risorseDisponibili.contains(risorsa)) {
            if (risorsa instanceof Prestito prestabile) {
                prestabile.presta(); // Chiama il metodo della risorsa
                risorseDisponibili.remove(risorsa);
                risorseInPrestito.add(risorsa);
            } else {
                System.out.println("La risorsa non può essere prestata.");
            }
        } else {
            System.out.println("Risorsa non disponibile per il prestito.");
        }
    }

    public void restituisciRisorsa(Risorsa risorsa) {
        if (risorseInPrestito.contains(risorsa)) {
            // Controlla se la risorsa può essere restituita
            if (risorsa instanceof Prestito prestabile) {
                prestabile.restituisci(); // Chiama il metodo della risorsa
                risorseInPrestito.remove(risorsa);
                risorseDisponibili.add(risorsa);
            } else {
                System.out.println("La risorsa non può essere restituita.");
            }
        } else {
            System.out.println("La risorsa non risulta in prestito.");
        }
    }
}