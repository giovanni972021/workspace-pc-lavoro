import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EseguiProgramma {

    public static void main(String[] args) {
        GestorePrenotazioni gestore = new GestorePrenotazioni();
        Scanner scanner = new Scanner(System.in);
        boolean continua = true;

        while (continua) {
            System.out.println("\n--- MENU ---");
            System.out.println("1 - Prenota campo");
            System.out.println("2 - Rimuovi prenotazione");
            System.out.println("3 - Cerca prenotazione per nome");
            System.out.println("4 - Visualizza prenotazioni per campo");
            System.out.println("0 - Esci");
            System.out.print("Scelta: ");

            int scelta = safeReadInt(scanner);

            switch (scelta) {
                case 1 -> prenotaCampo(scanner, gestore);
                case 2 -> rimuoviPrenotazione(scanner, gestore);
                case 3 -> cercaPrenotazioni(scanner, gestore);
                case 4 -> visualizzaPrenotazioni(scanner, gestore);
                case 0 -> continua = false;
                default -> System.out.println("Scelta non valida.");
            }
        }
        scanner.close();
    }

    private static void prenotaCampo(Scanner scanner, GestorePrenotazioni gestore) {
        TipoCampo tipo = scegliCampo(scanner);
        if (tipo == null)
            return;

        gestore.stampaDisponibili(tipo);

        scanner.nextLine(); // consuma newline
        System.out.print("Nome cliente: ");
        String nome = scanner.nextLine();

        System.out.print("Giorno (19-21): ");
        int giorno = safeReadInt(scanner);
        System.out.print("Ora inizio: ");
        int inizio = safeReadInt(scanner);
        System.out.print("Ora fine: ");
        int fine = safeReadInt(scanner);

        LocalDate data = LocalDate.of(2025, 12, giorno);

        System.out.println(
                gestore.prenota(tipo, data, inizio, fine, nome)
                        ? "Prenotazione effettuata!"
                        : "Slot non disponibile o dati non validi.");
    }

    private static void rimuoviPrenotazione(Scanner scanner, GestorePrenotazioni gestore) {
        TipoCampo tipo = scegliCampo(scanner);
        if (tipo == null)
            return;

        scanner.nextLine(); // consuma newline
        System.out.print("Nome cliente: ");
        String nome = scanner.nextLine();

        System.out.print("Giorno (19-21): ");
        int giorno = safeReadInt(scanner);
        System.out.print("Ora inizio: ");
        int inizio = safeReadInt(scanner);
        System.out.print("Ora fine: ");
        int fine = safeReadInt(scanner);

        LocalDate data = LocalDate.of(2025, 12, giorno);

        System.out.println(
                gestore.rimuovi(tipo, nome, data, inizio, fine)
                        ? "Prenotazione rimossa."
                        : "Prenotazione non trovata.");
    }

    private static void cercaPrenotazioni(Scanner scanner, GestorePrenotazioni gestore) {
        scanner.nextLine(); // consuma newline
        System.out.print("Nome cliente: ");
        String nome = scanner.nextLine();

        var risultati = gestore.cercaPerNome(nome);
        if (risultati.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata.");
        } else {
            risultati.forEach(System.out::println);
        }
    }

    private static void visualizzaPrenotazioni(Scanner scanner, GestorePrenotazioni gestore) {
        TipoCampo tipo = scegliCampo(scanner);
        if (tipo != null)
            gestore.stampaPrenotate(tipo);
    }

    private static TipoCampo scegliCampo(Scanner scanner) {
        System.out.println("1 - Campo da Basket");
        System.out.println("2 - Campo da Calcio");
        System.out.print("Scelta: ");
        int scelta = safeReadInt(scanner);

        return switch (scelta) {
            case 1 -> TipoCampo.BASKET;
            case 2 -> TipoCampo.CALCIO;
            default -> {
                System.out.println("Campo non valido.");
                yield null;
            }
        };
    }

    private static int safeReadInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input non valido, riprova.");
                scanner.nextLine();
            }
        }
    }
}
