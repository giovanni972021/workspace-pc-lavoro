package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // dichiaro una lista di Integer
        List<Integer> numeri = new ArrayList<>();

        // aggiungo elementi alla lista
        numeri.add(10);
        numeri.add(20);

        // dichiaro variabile somma
        int somma = 0;

        System.out.println("\nsomma tutti gli elementi della lista");

        for (int i = 0; i < numeri.size(); i++) {

            /*
             * i = 0 inizio ciclo
             * i < numeri.size() -> size() restituisce il numero di elementi nella lista
             * i++ incrementa i di 1 ad ogni giro
             * numeri.get(i) prende l'elemento alla posizione i
             */

            somma += numeri.get(i);
        }

        System.out.println(somma + "\n");
    }
}
