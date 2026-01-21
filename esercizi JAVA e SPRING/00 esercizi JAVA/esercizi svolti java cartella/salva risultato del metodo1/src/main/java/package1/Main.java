// creo la variabile messaggio che memorizza il contenuto del metodo get saluto 

public class Main {
    public static String getSaluto() {
        return "Ciao dal metodo!";
    }

    public static void main(String[] args) {

        // 2b) Salvare in variabile
        String messaggio = getSaluto();
        System.out.println("\n" + messaggio + "\n");
    }
}
// stampa ciao dal metodo
