package main.java;

public class Main {
    public static void main(String[] args) {
        GestoreRisorse gestore = new GestoreRisorse();

        // Aggiunta di risorse
        // gestore è nome oggetto appena creato
        // aggiungi risorsa è un metodo contenuto nella classe gestore risorse
        // Risorsa è il nome della classe che contiene attributi nome ed eta
        gestore.aggiungiRisorsa(new Risorsa("Computer", 10));
        gestore.aggiungiRisorsa(new Risorsa("Stampante", 30));
        gestore.aggiungiRisorsa(new Risorsa("Proiettore", 50));

        // Mostra tutte le risorse
        gestore.mostraRisorse();
        System.out.println("\n");
    }
}
