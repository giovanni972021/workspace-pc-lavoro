//GOAL stampare lista base composta SOLO da stringhe
//STAMPA [Mario, Luigi]
//vedi  Creo una lista DI STRINGHE dentro main

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Creo una lista di tipo stringa di nome lista
        // lista DENTRO public static void main
        // lista FUORI da costruttore
        ArrayList<String> lista = new ArrayList<>();

        // Aggiungo elementi
        lista.add("Mario");
        lista.add("Luigi");

        // stampa tutta la lista elementi UNO AFFIANCO ALL'ALTRO
        System.out.println(lista); // stampa mario,luigi
    }
}
