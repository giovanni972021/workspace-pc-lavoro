package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class EsempioAddAll {
    public static void main(String[] args) {

        // Lista principale
        List<String> lista1 = new ArrayList<>();
        lista1.add("Mario");
        lista1.add("Giovanni");

        // Nuova lista
        List<String> nuovi = new ArrayList<>();
        nuovi.add("Luigi");
        nuovi.add("Sergio");

        // Aggiunge tutti gli elementi di "nuovi" a "lista1"
        // unisci lista1 a lista di nome nuovi
        lista1.addAll(nuovi);

        // Stampa finale
        
        System.out.println(lista1);
    }
}
