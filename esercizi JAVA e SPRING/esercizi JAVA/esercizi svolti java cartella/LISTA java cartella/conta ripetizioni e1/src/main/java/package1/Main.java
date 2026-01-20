/*
GOAL quante volte si ripete il numero 10 nella lista?

*/

package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Integer> lista1 = new ArrayList<>();
        lista1.addAll(List.of(10, 20, 10, 40, 10));

        int i = 0;
        int contatore = 0;
        int x = 10;
        int contatore2 = 0;

        while (i < lista1.size()) {
            if (lista1.get(i) == x) {
                contatore++;
            }
            i++;
        }

        System.out.println("\n");

        System.out.println("Il numero " + x + " si ripete " + contatore + " volte nella LISTA");

        System.out.println("\n");

        int array[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 10, 10 };
        while (i < array.length) {
            if (array[i] == x) {
                contatore2++;
            }
            i++;
        }

        System.out.println("Il numero " + x + " si ripete " + contatore2 + " volte nell ARRAY ");

        System.out.println("\n");
    }
}
