package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int i = 0;

        // creo lista
        List<Integer> lista1 = new ArrayList<>();
        lista1.addAll(List.of(10, 20, 30, 40, 50));
        System.out.println("\nNumero elementi LISTA");
        while (i < lista1.size()) {
            i++;
        }
        // stampo numero elementi LISTA

        System.out.println(i + "\n");

        // creo array
        int[] array = { 10, 20, 30 };

        int i2 = 0;

        System.out.println("\nNumero elementi ARRAY");

        while (i2 < array.length) {
            i2++;
        }

        // stampo numero elementi array
        System.out.println(i2 + "\n");
    }
}
