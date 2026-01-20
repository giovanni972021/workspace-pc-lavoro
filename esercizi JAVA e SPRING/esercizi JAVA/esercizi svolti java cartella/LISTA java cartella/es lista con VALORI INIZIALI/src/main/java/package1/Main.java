
import java.util.Arrays;
import java.util.List;

public class Main {

    // Variabile di istanza
    private String messaggio;

    // 🔹 Costruttore FUORI da public static void main
    public Main() {
        messaggio = "Costruttore eseguito!";
        System.out.println(messaggio); // stampa costruttore eseguito
    }

    public static void main(String[] args) {

        // Creo un oggetto Main → viene chiamato il costruttore
        Main esempio = new Main();

        // Creo una lista DI STRINGHE dentro main
        List<String> lista = Arrays.asList("Ciao", "Mondo");

        // Aggiungo elementi

        // Stampo la lista
        System.out.println(lista); // stampa mario,luigi
    }
}
