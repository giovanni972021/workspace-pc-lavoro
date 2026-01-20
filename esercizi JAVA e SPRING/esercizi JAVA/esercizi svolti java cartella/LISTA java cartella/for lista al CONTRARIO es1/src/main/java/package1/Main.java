package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String args[]) {

        List<Integer> numeri = new ArrayList<>();
        numeri.addAll(List.of(10, 20, 30));

        System.out.println("\nStampa lista al CONTRARIO da ultimo a primo elemento\n");

        // Inizio dall’ultimo indice: numeri.length - 1, e vado all’indietro fino a 0
        for (int i = numeri.size() - 1; i >= 0; i--) {
            System.out.println(numeri.get(i));
        }
        System.out.println("\n");
    }
}
