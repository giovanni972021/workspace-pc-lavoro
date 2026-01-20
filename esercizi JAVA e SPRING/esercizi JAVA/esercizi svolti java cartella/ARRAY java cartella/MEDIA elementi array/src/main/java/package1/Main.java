package main.java.package1;

public class Main {
    public static void main(String[] args) {
        // dichiaro e inizializzo array di interi

        int arr[] = { 1, 2, 3, 4 };

        // variabile x memorizzare la somma degli elementi
        int somma = 0;

        // Ciclo for per scorrere tutti gli elementi dell'array
        for (int i = 0; i < arr.length; i++) {
            somma += arr[i];
            // aggiungi ogni elemento alla somma
            // è come scrivere somma = somma + arr [ i ]
        }

        // Calcolo della media: somma diviso il numero di elementi (cast a double per
        double media = (double) somma / arr.length;

        // Stampa della media
        System.out.println("La media è: " + media); // Verrà stampato: La media è: 2.5
    }
}
