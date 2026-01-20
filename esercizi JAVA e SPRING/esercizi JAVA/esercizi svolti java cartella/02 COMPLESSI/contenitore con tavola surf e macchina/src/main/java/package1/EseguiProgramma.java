//EseguiProgramma.java

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EseguiProgramma {
    // creo lista di nome articoli che contiene elementi di tipo Articolo
    // Articolo è nome della classe
    private static ArrayList<Articolo> articoli = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean continua = true;

        while (continua) {
            mostraMenu();
            int scelta = leggiIntero("Seleziona un'opzione: ");

            switch (scelta) {
                case 1 -> aggiungiMacchina();
                case 2 -> aggiungiTavola();
                case 3 -> stampaArticoli();
                case 4 -> {
                    continua = false;
                    System.out.println("Arrivederci!");
                }
                default -> System.out.println("Opzione non valida. Riprova.");
            }
        }

        scanner.close();
    }

    private static void mostraMenu() {
        System.out.println("\n==== GESTIONE ARTICOLI ====");
        System.out.println("1 - Aggiungi Macchina");
        System.out.println("2 - Aggiungi Tavola da Surf");
        System.out.println("3 - Stampa elenco articoli");
        System.out.println("4 - Esci");
    }

    private static void aggiungiMacchina() {
        String marca = leggiStringa("Inserisci la marca della macchina: ");
        int porte = leggiIntero("Inserisci il numero di porte: ");
        articoli.add(new Macchina(marca, porte));
        System.out.println("Macchina aggiunta con successo!");
    }

    private static void aggiungiTavola() {
        String marca = leggiStringa("Inserisci la marca della tavola da surf: ");
        double lunghezza = leggiDouble("Inserisci la lunghezza della tavola (in metri): ");
        articoli.add(new TavolaDaSurf(marca, lunghezza));
        System.out.println("Tavola da surf aggiunta con successo!");
    }

    private static void stampaArticoli() {
        if (articoli.isEmpty()) {
            System.out.println("Non ci sono articoli da mostrare.");
            return;
        }

        System.out.println("\n==== ELENCO ARTICOLI ====");
        for (int i = 0; i < articoli.size(); i++) {
            System.out.print((i + 1) + ". ");
            articoli.get(i).stampaDettagli();
        }
    }

    // ==============================
    // METODI DI INPUT ROBUSTI
    // ==============================

    public static int leggiIntero(String messaggio) {
        int valore;
        while (true) {
            System.out.print(messaggio);
            try {
                valore = scanner.nextInt();
                scanner.nextLine(); // Consuma il \n
                return valore;
            } catch (InputMismatchException e) {
                System.out.println("Errore: devi inserire un numero intero valido.");
                scanner.nextLine();
            }
        }
    }

    public static double leggiDouble(String messaggio) {
        double valore;
        while (true) {
            System.out.print(messaggio);
            try {
                valore = scanner.nextDouble();
                scanner.nextLine(); // Consuma il \n
                return valore;
            } catch (InputMismatchException e) {
                System.out.println("Errore: devi inserire un numero valido.");
                scanner.nextLine();
            }
        }
    }

    public static String leggiStringa(String messaggio) {
        while (true) {
            System.out.print(messaggio);
            String valore = scanner.nextLine().trim();
            if (!valore.isEmpty()) {
                return valore;
            }
            System.out.println("Errore: il campo non può essere vuoto.");
        }
    }
}
