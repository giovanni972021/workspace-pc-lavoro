package main.java;

public class EseguiProgramma {
    public static void main(String[] args) {

        // creo oggetto DENTRO main FUORI costruttore
        GestoreRisorse gestore = new GestoreRisorse();

        // Aggiunta di risorse
        gestore.aggiungiRisorsa(new Risorsa("Computer", 10));
        gestore.aggiungiRisorsa(new Risorsa("Stampante", 30));
        gestore.aggiungiRisorsa(new Risorsa("Proiettore", 50));

        // Mostra tutte le risorse
        gestore.stampaRisorseDisponibili();
    }
}
