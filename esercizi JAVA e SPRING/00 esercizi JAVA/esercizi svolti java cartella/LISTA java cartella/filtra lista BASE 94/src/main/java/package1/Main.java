package main.java.package1;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> nomi = List.of(
                "Roberto",
                "Marco",
                "Rita",
                "Anna",
                "Riccardo",
                "Luca");

        List<String> nomiConR = StringFilter.filtraNomiCheInizianoConR(nomi);

        System.out.println("\nLista originale:\n" + nomi + "\n\nNomi che iniziano con R\n" + nomiConR + "\n");
    }
}