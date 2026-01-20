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

    public void aggiungi_Libro_posizione_Specifica(int posizione, Libro libro) {
        if (libro == null)
            throw new IllegalArgumentException("Libro nullo");
        this.lista_Libri.add(posizione, libro);
    }

    public void stampalista_Libri() {

        System.out.println("\n--- Lista Libri ---\n");
        lista_Libri.forEach(System.out::println);
        System.out.println("\n");
    }

    public List<Libro> getLista_Libri() {
        return lista_Libri;
    }
}