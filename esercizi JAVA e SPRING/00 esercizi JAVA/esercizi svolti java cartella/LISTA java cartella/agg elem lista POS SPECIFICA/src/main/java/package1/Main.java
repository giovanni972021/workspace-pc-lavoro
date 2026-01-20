package main.java.package1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Creazione della lista
        List<String> nomi = new ArrayList<>();

        // Inserimento iniziale
        nomi.add("Mario");
        nomi.add("Giovanni");

        //aggiungi piu elementi alla lista
        nomi.addAll(List.of("Geppetto, Genoveffo"));


        
        // Inserimento di Sergio in posizione 0 ossia la prima a sinistra
        nomi.add(0, "Sergio");

        // Inserimento di Filippo in posizione 3 ossia la QUARTA da sinistra
        nomi.add(3, "Filippo");

        // Stampa della lista
        System.out.println("\n" + nomi + "\n");
    }
}
