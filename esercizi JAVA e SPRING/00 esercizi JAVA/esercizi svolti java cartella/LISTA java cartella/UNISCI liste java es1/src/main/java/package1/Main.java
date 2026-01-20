package main.java.package1;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        List<String> lista1 = new ArrayList<>();
        lista1.add("ciao");
        lista1.add("ciao 2");

        List<String> lista2 = new ArrayList<>();
        lista2.add("ciao 3");
        lista2.add("ciao 4");

        lista2.add("ciao 2");
        // crea lista 3 che contiene unione di lista 1 e lista 2
        List<String> lista3 = Stream
                .concat(lista1.stream(), lista2.stream())
                .toList();

        List<String> lista4 = Stream
                .concat(lista1.stream(), lista2.stream())
                // ESCLUDI duplicati
                .distinct()
                .collect(Collectors.toList());
        System.out.println("\nlista1 " + lista1);
        System.out.println("lista2 " + lista2);
        System.out.println("lista 3 " + lista3 + "\n");
        System.out.println(
                "come lista 3 ma SENZA duplicati\n" +

                        lista4 + "\n");

    }
}
