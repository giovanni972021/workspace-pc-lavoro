public class Main {
    public static void main(String[] args) {
        Gestione_Libri gestore = new Gestione_Libri();

        gestore.aggiungiLibro(new Libro("il signore degli anelli", "J.R.R. Tolkien", 100.50, 1985));

        gestore.aggiungiLibro(new Libro("Il nome della rosa", "Umberto Eco", 22.50, 1980));

        gestore.aggiungiLibro(new Libro("Il Grande Gatsby", "F. Scott Fitzgerald", 12.90, 1925));

        gestore.rimozione_Condizionale(50.0);

        gestore.stampalista_Libri();

    }
}