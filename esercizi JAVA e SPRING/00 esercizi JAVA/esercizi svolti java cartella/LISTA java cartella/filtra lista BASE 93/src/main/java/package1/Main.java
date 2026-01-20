package main.java.package1;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // aggiungi elementi alla lista
        List<Integer> numeri = List.of(1, 2, 3, 4, 5, 6);

        // numeri pari è nome oggetto legato a classe integer filter
        List<Integer> numeriPari = IntegerFilter.filtraNumeriPari(numeri);

        System.out.println("\n Lista originale: \n" + numeri + "\n Numeri pari: " + numeriPari + "\n");

    }
}
