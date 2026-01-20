
//stampa Lista originale: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        // CREO LISTA di numeri da 0 a 9
        List<Integer> numeri = IntStream.range(0, 10) // genera numeri da 0 a 9
                .boxed() // trasforma gli int in Integer
                .collect(Collectors.toList()); // raccoglie in una lista

        // Stampa della lista originale
        System.out.println("Lista : " + numeri);

    }
}
