public class Main {

    /* Dichiaro l'array come attributo della classe */
    protected int[] movimenti_conto;

    public static void main(String[] args) {

        /* Istanzio l'oggetto della classe Main per accedere ai suoi attributi */
        Main oggetto1 = new Main();

        /* Inizializzo l'array dell'oggetto */
        oggetto1.movimenti_conto = new int[] { 10, 21, 30, 45, 50 };
        System.out.println("\n");

        // --- PRIMO CICLO: Stampa i VALORI pari ---
        System.out.println("Stampa solo i VALORI il cui modulo due restituisce 0 (numeri pari):");

        for (int i = 0; i < oggetto1.movimenti_conto.length; i++) {
            // Calcolo il modulo sul VALORE dell'array nella posizione i
            if (oggetto1.movimenti_conto[i] % 2 == 0) {
                System.out.println(oggetto1.movimenti_conto[i]);
            }
        }
        System.out.println("\n");
        // --- SECONDO CICLO: Stampa la POSIZIONE dei numeri pari ---
        System.out.println("Stampa la POSIZIONE dei numeri il cui modulo due restituisce 0:");

        for (int i = 0; i < oggetto1.movimenti_conto.length; i++) {
            // Il controllo deve essere sempre sul VALORE (movimenti_conto[i]),
            // ma l'output deve essere l'INDICE (i)
            if (oggetto1.movimenti_conto[i] % 2 == 0) {
                System.out.println("Il numero pari si trova all'indice: " + i);
            }
        }
        System.out.println("\n");
    }
}