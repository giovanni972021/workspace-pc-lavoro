
class Main {
    public static void main(String[] args) {
        GestoreElenco esempio = new GestoreElenco();
        esempio.aggiungiElemento("Barba");
        esempio.aggiungiElemento("Angelo");
        esempio.aggiungiElemento("Zorro");

        esempio.ordina_crescente();

        System.out.println("\nLista ordinata CRESCENTE:");
        esempio.stampaElementi();

        esempio.ordina_decrescente();

        System.out.println("\nLista ordinata DECRESCENTE:");
        esempio.stampaElementi();

        System.out.println("\n");
    }
}
