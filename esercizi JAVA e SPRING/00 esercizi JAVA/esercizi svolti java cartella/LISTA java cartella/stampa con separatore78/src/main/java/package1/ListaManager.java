import java.util.ArrayList;
import java.util.List;

public class ListaManager {
    private List<String> elementi;

    public ListaManager() {
        elementi = new ArrayList<>();
        elementi.add("Mela");
        elementi.add("Banana");
        elementi.add("Arancia");
    }

    public List<String> getElementi() {
        return elementi;
    }

    public static void stampaLista(List<String> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("La lista è vuota.");
            return;
        }

        System.out.println("\n--- Elenco Elementi ---");
        for (String elemento : lista) {
            System.out.println("- " + elemento+ "\n");
        }
        System.out.println();
    }
}