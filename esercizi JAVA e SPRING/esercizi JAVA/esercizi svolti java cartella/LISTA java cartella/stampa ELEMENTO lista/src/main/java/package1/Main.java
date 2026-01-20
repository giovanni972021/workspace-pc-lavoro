package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Integer> numeri = new ArrayList<>();

        // aggiungo elementi alla lista
        numeri.add(10);
        numeri.add(20);

        System.out.println("\nstampa elemento della lista sito in posizione 0");

        System.out.println(numeri.get(0));

        System.out.println("\n");
    }
}
