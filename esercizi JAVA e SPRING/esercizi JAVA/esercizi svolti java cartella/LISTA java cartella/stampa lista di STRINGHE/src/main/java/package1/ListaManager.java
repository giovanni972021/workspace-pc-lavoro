
//classe x CREARE lista e METODO x stamparla
import java.util.ArrayList;
import java.util.List;

// Classe che si occupa della gestione della lista
public class ListaManager {
    // dichiaro lista di nome elementi di tipo string
    private List<String> elementi;

    // Costruttore: inizializza la lista con alcuni valori
    public ListaManager() {
        // creo lista di nome elementi
        elementi = new ArrayList<>();
        // aggiungo elemento mela alla lista
        elementi.add("Mela");
        elementi.add("Banana");
        elementi.add("Arancia");
    }

    // creo get della lista
    public List<String> getElementi() {
        return elementi;
    }

    // il metodo di nome stampaLista mi permette di stampare tutta la lista
    // parametro del metodo si chiama lista di tipo List <String>
    // Metodo x stampare lista
    public static void stampaLista(List<String> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("La lista è vuota.");
            return;
        }
        System.out.println("\nstampa tutta la lista\n");
        // elemento poteva chiamarsi anche pippo , indica ogni elemento della lista
        for (String elemento : lista) {
            System.out.println(elemento);
        }
        System.out.println("\n");

    }
}
