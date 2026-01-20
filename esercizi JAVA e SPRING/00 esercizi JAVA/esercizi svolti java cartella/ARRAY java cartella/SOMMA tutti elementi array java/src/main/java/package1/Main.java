package main.java.package1;

public class Main {
    public static void main(String[] args) {
        // dichiaro array di nome numeri e di tipo int

        int[] numeri = { 10, 20, 30 };
        // oppure Str [] var1 = {“ciao”,”ciao2”};

        // dichiaro variabile somma
        int somma = 0;

        System.out.println("\nsomma tutti gli elementi dell'array");
        for (int i = 0; i < numeri.length; i++) {

            /*
             * int i = 0 inizia ciclo con i uguale a zero
             * i < numeri.length - interrompi ciclo SOLO quando i NON è piu minore della
             * lunghezza dell’array, che in questo caso è 3 ossia la quantita di elementi
             * presenti nell’array
             * i++ al termine di ogni esecuzione del ciclo, incrementa i di 1
             * quindi primo giro i sara 0+1 ossia 1
             * secondo giro sara 1+1 ossia 2 ecc
             * calcola la somma
             */

            somma += numeri[i];

        }
        System.out.println(somma + "\n");

    }
}