package main.java.package1;

//OBIETTIVO calcolare valore massimo tra quelli presenti nell’array

public class Main {
    public static void main(String[] args) {
        int[] numeri = { 4, 10, 2, 8, 6 };

        int massimo = numeri[0]; // partiamo dal primo elemento

        for (int i = 1; i < numeri.length; i++) {
            if (numeri[i] > massimo) {
                massimo = numeri[i]; // aggiorniamo il massimo
            }
        }

        System.out.println("\n");
        System.out.println("Il valore massimo è: " + massimo);

        System.out.println("\n");
    }
}