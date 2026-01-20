
public class Main {
    public static void main(String[] args) {
        Gestione_Libri gestore = new Gestione_Libri();

        gestore.aggiungiLibro(new Libro("il signore degli anelli", "J.R.R. Tolkien", 100.50, 1985));
        gestore.aggiungiLibro(new Libro("titolo2", "autore titolo2", 150.95, 1960));

        System.out.println("\nlista originale\n");
        gestore.stampalista_Libri();

        gestore.getLista_Libri().set(0,
                new Libro("NUOVO libro posizione 0", "autore signore degli anelli", 300.50, 1985));

        System.out.println("\nlista DOPO sostituzione\n");
        gestore.stampalista_Libri();
        System.out.println("\n");
    }
}