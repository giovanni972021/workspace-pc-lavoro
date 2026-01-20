
public class Main {
    public static void main(String[] args) {
        Gestione_Libri gestore = new Gestione_Libri();

        gestore.aggiungiLibro(new Libro("il signore degli anelli", "J.R.R. Tolkien", 100.50, 1985));

        gestore.aggiungiLibro(new Libro("titolo2", "autore titolo2", 150.95, 1960));

        gestore.aggiungiLibro(new Libro("Il nome della rosa", "Umberto Eco", 22.50, 1980));

        System.out.println("\nstampa il primo elemento della lista");
        // Usando Streams per stampare i primi 3 libri
        gestore.getLista_Libri().stream()
                .limit(1) // Limita ai primi 3 libri
                .forEach(System.out::println); // Stampa ogni libro
        System.out.println("\n");

    }
}