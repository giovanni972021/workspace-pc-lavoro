package main.java.package1;

public class Main {
    public static void main(String[] args) {
        String parola = "ciao";
        /*
         * in questo caso stringa è lunga 4 caratteri ma le posizioni sono 3 perche
         * primo carattere a sinistra è in posizione 0
         */

        System.out.println("\n");
        for (int i = parola.length() - 1; i >= 0; i--) {
            System.out.print(parola.charAt(i));

        }

        System.out.println("\n");
    }
}
