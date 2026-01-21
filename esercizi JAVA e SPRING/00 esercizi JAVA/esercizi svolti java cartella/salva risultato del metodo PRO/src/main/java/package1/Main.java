import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Inizializziamo il service
        Gestione_Prodotti service = new Gestione_Prodotti();

        // Aggiungiamo dati di esempio
        service.aggiungiProdotto(new Prodotto(1, "Latte", 12, 1.50, LocalDate.of(2024, 5, 20)));
        service.aggiungiProdotto(new Prodotto(2, "Pasta", 50, 0.90, LocalDate.of(2025, 12, 1)));

        // --- LA RIGA CHE DESIDERAVI ---
        // Memorizziamo il risultato del metodo in una variabile di tipo Prodotto
        Prodotto inScadenza = service.getPrimoInScadenza();

        // Stampiamo il contenuto della variabile
        System.out.println("\nStampa variabile che contiene risultato del medoto\n");
        if (inScadenza != null) {
            System.out.println(inScadenza);
        } else {
            System.out.println("Nessun prodotto trovato.");
        }

        // Verifica finale della lista intera
        service.stampalista_Prodotti();
    }
}