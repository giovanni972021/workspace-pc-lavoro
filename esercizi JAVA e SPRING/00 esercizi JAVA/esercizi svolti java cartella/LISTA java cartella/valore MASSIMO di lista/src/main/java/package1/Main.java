package main.java.package1;

import java.util.ArrayList;
import java.util.List;

//OBIETTIVO calcolare valore massimo tra quelli presenti nella lista

public class Main {
    public static void main(String[] args) {

        List<Integer> lista = new ArrayList<>();

        lista.add(10);
        lista.add(50);

        int massimo = lista.get(0); // partiamo dal primo elemento

        for (int i = 1; i < lista.size(); i++) {
            if (lista.get(i) > massimo) {
                massimo = lista.get(i); // aggiorniamo il massimo
            }
        }

        System.out.println("\n");
        System.out.println("Il valore massimo è: " + massimo);

        System.out.println("\n");
    }
}