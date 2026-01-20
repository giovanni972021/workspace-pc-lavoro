import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        List<String> lista = new ArrayList<>();

        String Stringa = "Ciao";

        lista.add("Ciao");
        lista.add("Come va?");
        lista.add("Arrivederci");

        // elemento rappresenta ogni elemento della lista

        System.out.println("\nPRIMA della rimozione\n");
        for (String elemento : lista) {
            System.out.println(elemento);
        }
        System.out.println("\n");

        lista.remove(Stringa);

        System.out.println("\nDOPO rimozione\n");
        for (String elemento : lista) {
            System.out.println(elemento);
        }
        System.out.println("\n");

    }
}
