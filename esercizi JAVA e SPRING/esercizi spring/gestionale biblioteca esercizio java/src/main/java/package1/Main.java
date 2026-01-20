
// Classe Main

import model.Biblioteca;
import sottoClassi.Dvd;
import sottoClassi.Libro;

public class Main {
    public static void main(String[] args) {

        /*
         * creo oggetto di nome dvd1 e come parametro passo i valori relativi alle
         * variavbili presenti nei parametri del costruttore della classe dvd
         */
        Dvd dvd1 = new Dvd("Disco ACDC", 1980, 12);
        Dvd dvd2 = new Dvd("Disco Queen", 1985, 10);

        Libro libro1 = new Libro("Il Signore degli Anelli", 1954, 1200);
        Libro libro2 = new Libro("1984", 1949, 350);

        // creo oggetto biblioteca
        Biblioteca biblioteca = new Biblioteca();

        // aggiungo elemento risorsa a contenitore biblioteca,
        /*
         * aggiungi elemento a lista risorse disponibili,
         * dvd1 è oggetto creato poco sopra, biblioteca è nome oggetto appena creato e
         * aggiungi risorsa è nome metodo contenuto nella classe biblioteca
         */

        biblioteca.aggiungiRisorsa(dvd1);
        biblioteca.aggiungiRisorsa(dvd2);
        biblioteca.aggiungiRisorsa(libro1);
        biblioteca.aggiungiRisorsa(libro2);

        // stampa tutto elenco risorse disponibili
        System.out.println("🏛️ BIBLIOTECA INIZIO");
        biblioteca.stampaRisorseDisponibili();
        biblioteca.stampaRisorseInPrestito();

        /*
         * -oggetto.metodo(parametro)
         * -biblioteca è nome oggetto appena creato
         * -presta risorsa è metodo presente in classe biblioteca su cui si basa
         * creazione oggetto
         * -dvd1 è valore che assegno alla variabile Risorsa risorsa , Risorsa risorsa
         * era parametro del metodo presta risorsa classe biblioteca
         */
        System.out.println("\n=== 🔵 PRESTO dvd1 ===");
        biblioteca.prestaRisorsa(dvd1);

        System.out.println("\n📉 Dopo il prestito:");
        biblioteca.stampaRisorseDisponibili();
        biblioteca.stampaRisorseInPrestito();

        // Restituzioni
        System.out.println("\n=== 🟢 RESTITUISCO dvd1 ===");
        biblioteca.restituisciRisorsa(dvd1);

        System.out.println("\n📈 Dopo la restituzione:");
        biblioteca.stampaRisorseDisponibili();
        biblioteca.stampaRisorseInPrestito();

        System.out.println("\nRimuovo risorsa dvd1");
        biblioteca.rimuoviRisorsa(dvd1);

        System.out.println("\nDopo la rimozione:");
        biblioteca.stampaRisorseDisponibili();
    }
}
