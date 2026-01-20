package main.java.package1;

import java.time.LocalDate;
//ESEGUI PROGRAMMA
import java.util.Scanner;

public class EseguiProgramma {
    public static void main(String[] args) {

        System.out.println("\n");
        int giorno2 = 25;
        LocalDate data = LocalDate.of(2025, 12, giorno2);
        System.out.println("La data di giorno2 è: " + data);
        System.out.println("\n");

        // Scanner per permettere ad utente di inserire valori
        Scanner scanner = new Scanner(System.in);

        /*
         * int giorno serve a salvare numero inserito da utente
         */
        System.out.print("Inserisci il giorno di nascita del primo dipendente: ");
        // next int metodo scanner per inserire numero intero ,

        int giorno = scanner.nextInt();

        System.out.print("Inserisci il mese di nascita del primo dipendente: ");
        int mese = scanner.nextInt();

        System.out.print("Inserisci anno di nascita del primo dipendente: ");
        int anno = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nnome dipendente1");
        // next Line metodo scanner per inserire stringa

        String nome = scanner.nextLine();
        DataNascita dipendente1 = new DataNascita(giorno, mese, anno, nome);

        System.out.println("\nData di nascita " + nome + " " + dipendente1.getGiorno() + "/" + dipendente1.getMese()
                + "/" + dipendente1.getAnno() + "\n");

        System.out.print("Inserisci anno di nascita del secondo dipendente: ");
        int anno2 = scanner.nextInt();

        DataNascita dipendente2 = new DataNascita(1, 1, anno2, "dipendente2");

        System.out.println("\nData di nascita del secondo dipendente: " +
                dipendente2.getGiorno() + "/" + dipendente2.getMese() + "/" + dipendente2.getAnno() + "\n");

        // Stampiamo la differenza di anni
        int differenza = Math.abs(dipendente1.differenzaAnni(dipendente2));
        System.out.println("Differenza anni tra i due dipendenti: " + differenza);

        scanner.close();
        System.out.println("\n");

    }
}
