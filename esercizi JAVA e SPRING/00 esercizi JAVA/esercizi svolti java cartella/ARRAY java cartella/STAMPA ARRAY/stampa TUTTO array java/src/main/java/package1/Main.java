package main.java.package1;

public class Main {
    public static void main(String[] args) {
        // dichiaro array di nome numeri e di tipo int

        int[] numeri = { 10, 20, 30 };
        // oppure Str [] var1 = {“ciao”,”ciao2”};

        System.out.println("\nstampa tutti gli elementi dell array\n");

        for (int i = 0; i < numeri.length; i++) {
            System.out.println(numeri[i]);
        }

        System.out.println("\n");
    }
}