import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Utente> utenti = new ArrayList<>();

        utenti.add(new Utente("Anna", 22, 165));
        utenti.add(new Utente("Mario", 40, 170));
        utenti.add(new Utente("Mario", 18, 185));
        utenti.add(new Utente("Giulia", 30, 160));

        // Ordinamento: Nome (A-Z) e poi Età (Crescente)
        utenti.sort(Comparator
                .comparing(Utente::getNome)
                .thenComparingInt(Utente::getEta)
                .reversed());

        System.out.println("\n");
        // Stampa professionale corretta (System.out::println)
        System.out.println("--- Lista Utenti Ordinata ---");
        utenti.forEach(System.out::println);

        System.out.println("\n");

    }
}