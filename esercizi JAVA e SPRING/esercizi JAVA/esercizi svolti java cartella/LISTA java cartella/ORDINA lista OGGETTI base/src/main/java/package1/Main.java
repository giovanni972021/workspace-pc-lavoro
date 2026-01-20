public class Main {

    public static void main(String[] args) {
        GestoreElenco gestore = new GestoreElenco();

        gestore.aggiungiElemento(new Persona("Mario", 30));
        gestore.aggiungiElemento(new Persona("Anna", 25));
        gestore.aggiungiElemento(new Persona("Luca", 28));

        gestore.ordinaCrescentePerNome();
        System.out.println("\nElementi dopo ordinamento crescente per nome:");
        gestore.stampaElementi();

        gestore.ordinaDecrescentePerNome();
        System.out.println("\nElementi dopo ordinamento decrescente per nome:");
        gestore.stampaElementi();

        gestore.ordinaCrescentePerEta();
        System.out.println("\nElementi dopo ordinamento crescente per età:");
        gestore.stampaElementi();

        gestore.ordinaDecrescentePerEta();
        System.out.println("\nElementi dopo ordinamento decrescente per età:");
        gestore.stampaElementi();
        System.out.println("\n");
    }
}
