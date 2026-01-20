import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String testo1 = "ciao";
        List<String> lista = new ArrayList<>();

        // Aggiungiamo elementi
        lista.add(testo1);
        lista.add("Come va?");

        System.out.println("\n");
        if (lista.contains(testo1)) {
            System.out.println("la lista CONTIENE " + testo1);
        }
        System.out.println("\n");

    }
}
