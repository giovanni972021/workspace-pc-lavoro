package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class EsempioAddAll {
    public static void main(String[] args) {
        List<String> lista1 = new ArrayList<>();

        lista1.addAll(List.of("Mario", "Genoveffo"));

        System.out.println("\nPRIMA della sostituzione\n" + lista1);

        // sostituisco elemento sito in posizione 0 ossia la prima da sinistra con
        // Mario2
        lista1.set(0, "Mario2");

        System.out.println("\nDOPO sostituzione\n" + lista1 + "\n");

        // Risultato: ["Mario2", "Giovanni"] -> La posizione è conservata!
    }
}