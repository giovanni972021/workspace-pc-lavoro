package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> lista = new ArrayList<>();

        lista.add(10);
        lista.add(20);
        // variabile x memorizzare la somma degli elementi
        int somma = 0;
        for (int i = 0; i < lista.size(); i++) {

            somma += lista.get(i);

        }
        double media = (double) somma / lista.size();

        // Stampa della media
        System.out.println("\n");
        System.out.println("La media è: " + media);

        System.out.println("\n");

    }
}
