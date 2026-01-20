package main.java.package1;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // Dichiarazione della Map di nome voti che contiene
        // chiavi di tipo string e
        // valori di tipo int
        Map<String, Integer> voti = new HashMap<>();

        // Inserimento elementi
        // aggiungi a mappa
        // voti è nome della
        voti.put("Mario", 8);
        voti.put("Luigi", 7);
        voti.put("Anna", 9);

        // Recupero di un valore tramite chiave
        // cerca valore associato alla chiave mario
        System.out.println("\nVoto di Mario: " + voti.get("Mario"));

        // Stampa di tutti gli elementi
        System.out.println("\nTutti i voti:");

        // stampa tutta la map
        // nome è ogni elemento contenuto nella map
        // voti è nome della map
        for (String nome : voti.keySet()) {
            System.out.println(nome + " -> " + voti.get(nome));
        }

        System.out.println("\n");
    }
}
