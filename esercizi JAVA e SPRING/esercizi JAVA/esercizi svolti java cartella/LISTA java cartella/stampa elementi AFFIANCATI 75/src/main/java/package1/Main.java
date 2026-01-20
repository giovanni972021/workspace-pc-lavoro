import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> frutti = Arrays.asList("Mela", "Banana", "Arancia", "Pera");
        System.out.println("\n");
        // stampa tutta la lista
        // 1️⃣ Ciclo for
        for (int i = 0; i < frutti.size(); i++) {
            System.out.print(frutti.get(i));
            if (i < frutti.size() - 1) {
                System.out.print(" - "); // separatore personalizzato
            }
        }
        System.out.println("\n"); // a capo alla fine

    }
}
