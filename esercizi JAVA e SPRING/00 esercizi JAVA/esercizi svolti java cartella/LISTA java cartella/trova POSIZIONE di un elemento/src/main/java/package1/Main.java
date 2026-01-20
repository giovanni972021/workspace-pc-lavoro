import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class Main {
    public static void main(String[] args) {

        // Lista di risorse
        List<String> lista = new ArrayList<>();
        lista.add("1984");
        lista.add("Il Signore degli Anelli");

        // Valore da cercare
        String valoreDaCercare = "1984";

        // Usa Stream per trovare l'indice della prima occorrenza
        OptionalInt indice = lista.stream()
                .sequential()
                .filter(val -> val.equals(valoreDaCercare))
                .mapToInt(lista::indexOf)
                .findFirst();

        System.out.println("\n");
        // Stampa l'indice
        if (indice.isPresent()) {
            System.out.println("Il valore " + valoreDaCercare + " si trova all'indice: " + indice.getAsInt());
        } else {
            System.out.println("Il valore non è presente nella lista.");
        }
        System.out.println("\n");

    }
}
