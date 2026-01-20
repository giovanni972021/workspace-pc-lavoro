import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Creiamo una lista di stringhe
        List<String> lista = new ArrayList<>();

        // Aggiungiamo elementi
        lista.add("Ciao");
        lista.add("Come va?");
        lista.add("Arrivederci");

        // stampa tutta la lista uno SOTTO ALTRO
        // stampa lista uno sotto altro DENTRO main
        //elemento rappresenta ogni elemento della lista
        for (String elemento : lista) {
            System.out.println(elemento);
        }

    }
}
