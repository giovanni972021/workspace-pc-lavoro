package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class EsempioAddAll {
    public static void main(String[] args) {
        List<String> lista1 = new ArrayList<>();
        lista1.addAll(List.of("Geppetto", "Genoveffo", "peppe", "Mario", "stefano"));

        System.out.println("\nPRIMA della sostituzione\n" + lista1);

        String nomeDaCercare = "Mario";
        int indice = lista1.indexOf(nomeDaCercare);
        String nomeSostituto = "Mario2";

        if (indice != -1) {
            lista1.set(indice, nomeSostituto);
        } else {
            System.out.println("Elemento non trovato nella lista.");
        }

        System.out.println("\nDOPO sostituzione\n" + lista1 + "\n");

    }
}