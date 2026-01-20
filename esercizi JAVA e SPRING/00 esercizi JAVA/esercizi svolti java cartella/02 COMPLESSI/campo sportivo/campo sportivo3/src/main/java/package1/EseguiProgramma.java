import java.util.Scanner;

public class EseguiProgramma {

    public static void main(String[] args) {

        Campo campo = new Campo();
        Scanner scanner = new Scanner(System.in);
        boolean continua = true;

        System.out.println("Benvenuto nel sistema di prenotazione del campo!");

        while (continua) {
            System.out.print("Inserisci il nome del cliente: ");
            String nomeCliente = scanner.nextLine();

            System.out.print("Inserisci ora di inizio (0-24): ");
            int inizio = scanner.nextInt();

            System.out.print("Inserisci ora di fine (0-24): ");
            int fine = scanner.nextInt();

            // Pulisco il buffer del scanner
            scanner.nextLine();

            boolean successo = campo.addPren(inizio, fine, nomeCliente);
            if (successo) {
                System.out.println("Prenotazione effettuata con successo!");
            } else {
                System.out.println("Errore: il campo non è disponibile in quell'orario o dati non validi.");
            }

            System.out.print("Vuoi inserire un'altra prenotazione? (s/n): ");
            String risposta = scanner.nextLine();
            if (risposta.equalsIgnoreCase("n")) {
                continua = false;
            }

        }

        scanner.close();
        campo.stampaTuttePrenotazioni();

    }
}
