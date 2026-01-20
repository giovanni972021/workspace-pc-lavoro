public class Main {

    public static void main(String[] args) {
        GestoreElenco gestore = new GestoreElenco();

        gestore.aggiungiElemento("Banana");
        gestore.aggiungiElemento("Mela");
        gestore.aggiungiElemento("Arancia");

        gestore.ordinaCrescente();
        System.out.println("\nElementi dopo ordinamento crescente:");
        gestore.stampaElementi();

        gestore.ordinaDecrescente();
        System.out.println("\nElementi dopo ordinamento decrescente:");
        gestore.stampaElementi();

        System.out.println("\n");
    }
}
