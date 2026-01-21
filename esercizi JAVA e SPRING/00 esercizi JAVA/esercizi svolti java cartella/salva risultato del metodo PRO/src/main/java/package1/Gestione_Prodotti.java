import java.util.ArrayList;
import java.util.List;

public class Gestione_Prodotti {
    private final List<Prodotto> lista_Prodotti;

    public Gestione_Prodotti() {
        this.lista_Prodotti = new ArrayList<>();
    }

    public void aggiungiProdotto(Prodotto prodotto) {
        if (prodotto == null) {
            throw new IllegalArgumentException("Prodotto nullo");
        }
        lista_Prodotti.add(prodotto);
    }

    // Questo è il metodo che restituisce un oggetto Prodotto alla variabile nel
    // Main
    public Prodotto getPrimoInScadenza() {
        if (lista_Prodotti.isEmpty()) {
            return null;
        }
        // Per ora restituisce semplicemente il primo inserito
        return lista_Prodotti.get(0);
    }

    public void stampalista_Prodotti() {
        System.out.println("\n--- Lista Completa Prodotti ---\n");
        lista_Prodotti.forEach(System.out::println);
    }
}