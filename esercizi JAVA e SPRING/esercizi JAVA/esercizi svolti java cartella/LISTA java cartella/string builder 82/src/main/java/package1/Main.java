public class Main {
    public static void main(String[] args) {
        Gestione_Libri gestore = new Gestione_Libri();

        gestore.aggiungiLibro(new Libro("il signore degli anelli", "J.R.R. Tolkien", 100.50, 1985));

        gestore.aggiungiLibro(new Libro("titolo2", "autore titolo2", 150.95, 1960));

        gestore.stampalista_Libri();

    }
}