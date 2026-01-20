import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<String> lista1 = Arrays.asList("Marco", "Anna", "Luca", "Andrea", "Paolo");

        // Filtriamo i lista1 che iniziano con la lettera 'A'
        List<String> filtrati = lista1.stream()
                .filter(nome -> nome.startsWith("A")) // Condizione
                .collect(Collectors.toList()); // Trasforma il risultato di nuovo in una lista
        System.out.println("\nstampa lista1 per nomi che iniziano con lettera A\n" + filtrati + "\n");

    }
}