package main.java.package1;

public class Main {
    public static void main(String args[]) {
        int numeri[] = { 10, 11, 12 };

        System.out.println("\nStampa array al CONTRARIO da ultimo a primo elemento\n");

        // Inizio dall’ultimo indice: numeri.length - 1, e vado all’indietro fino a 0
        for (int i = numeri.length - 1; i >= 0; i--) {
            System.out.println(numeri[i]);
        }
        System.out.println("\n");
    }
}
