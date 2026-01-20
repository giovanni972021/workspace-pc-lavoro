import java.util.ArrayList;
import java.util.List;

public class Gestione_Libri {
    private final List<Libro> lista_Libri;

    public Gestione_Libri() {
        this.lista_Libri = new ArrayList<>();
    }

    public void aggiungiLibro(Libro libro) {
        if (libro == null)
            throw new IllegalArgumentException("Libro nullo");
        lista_Libri.add(libro);
    }

    public void stampalista_Libri() {

        System.out.println("\nLibri disponibili:\n");

        lista_Libri.forEach(r -> System.out.println("- " + r + "\n"));

        System.out.println("\n");

    }

}