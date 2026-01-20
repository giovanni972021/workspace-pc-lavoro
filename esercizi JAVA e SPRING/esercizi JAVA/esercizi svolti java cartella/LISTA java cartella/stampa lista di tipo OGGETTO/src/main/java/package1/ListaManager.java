import java.util.ArrayList;
import java.util.List;

public class ListaManager {

    private List<Frutta> frutti;

    public ListaManager() {
        frutti = new ArrayList<>();

        // frutti è nome della lista, Frutta è nome della classe, quantita e nome sono
        // attributi dichiarati nella classe frutta
        frutti.add(new Frutta("Mela", 10));
    }

    public List<Frutta> getFrutti() {
        return frutti;
    }

    public void aggiungiFrutta(Frutta frutto) {
        frutti.add(frutto);
    }

    // Metodo x stampare lista
    public static void stampaLista(List<Frutta> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("La lista è vuota.");
            return;
        }
        System.out.println("\nlista stampata \n");
        for (Frutta frutto : lista) {
            System.out.println(frutto);
        }
        System.out.println("\n");
    }
}
