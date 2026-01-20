import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Inizializziamo il servizio esterno
        OrdineService service = new OrdineService();

        // Eseguiamo il filtraggio tramite il service
        List<Ordine> risultati = service.filtraOrdini(100.0, "SPEDITO");

        // Stampa professionale
        System.out.println("\n--- RISULTATI FILTRO SERVICE ---\n");
        risultati.forEach(System.out::println);
        System.out.println("\n");
    }
}