
public class Main {
    public static void main(String[] args) {
        Gestione_Libri gestore = new Gestione_Libri();

        gestore.aggiungiLibro(new Libro("il signore degli anelli", "J.R.R. Tolkien", 100.50, 1985));
        gestore.aggiungiLibro(new Libro("titolo2", "autore titolo2", 150.95, 1960));

        System.out.println("\nlista originale\n");
        gestore.stampalista_Libri();

        Libro libroDaSostituire = new Libro("il signore degli anelli", "J.R.R. Tolkien", 100.50, 1985);
        int indice = gestore.getLista_Libri().indexOf(libroDaSostituire);

        if (indice != -1) {
            Libro libroNuovo = new Libro("NUOVO signore degli anelli ", "autore libro 3", 900, 1947);
            gestore.getLista_Libri().set(indice, libroNuovo);
        }

        System.out.println("\nlista DOPO sostituzione\n");
        gestore.stampalista_Libri();
        System.out.println("\n");
    }
}