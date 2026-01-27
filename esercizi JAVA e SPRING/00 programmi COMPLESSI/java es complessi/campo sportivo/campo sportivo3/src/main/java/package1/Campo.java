import java.util.ArrayList;
// Importo la classe ArrayList, che mi serve per creare una lista dinamica dove posso aggiungere o togliere prenotazioni.

public class Campo {

    // dichiaro la variabile di nome prenotazioni che rappresenta una lista di
    // oggetti di tipo Prenotaazione
    private ArrayList<Prenotazione> prenotazioni;

    // creo costruttore della classe campo
    public Campo() {
        // creo lista di nome prenotazioni di tipo Prenotaazione che conterra tutte le
        // prenotazioni
        prenotazioni = new ArrayList<>();
    }

    // Metodo per aggiungere una prenotazione.
    // inizio = ora di inizio richiesta
    // fine = ora di fine richiesta
    // nomeCliente = nome della persona che vuole prenotare
    public boolean addPren(int inizio, int fine, String nomeCliente) {

        // --- 1) CONTROLLO DATI INSERITI ---
        // Se l'ora di inizio è minore di 0 → non valida
        // Se l'ora di fine è maggiore di 24 → non valida
        // Se inizio è >= fine → intervallo impossibile
        if (inizio < 0 || fine > 24 || inizio >= fine) {
            return false; // Prenotazione non valida
        }

        // --- 2) CONTROLLO SE IL CAMPO È GIÀ OCCUPATO ---
        // Scorro tutte le prenotazioni già presenti
        for (Prenotazione p : prenotazioni) {

            // Prendo l’ora di inizio della prenotazione già esistente
            int oraInizio = p.getInizio();
            int oraFine = p.getFine();

            // Controllo se l'intervallo richiesto (inizio-fine)
            // si sovrappone con una prenotazione esistente.
            // Se l'ora salvata cade dentro l’intervallo richiesto, il campo è occupato.
            // ora maggiore uguale a inizio per controllare se ora inserita è uguale
            if (oraInizio >= inizio && oraInizio < fine) {
                System.out.println("prenotazione non valida");

                return false; // Intervallo già prenotato → prenotazione non possibile
            }
        }

        // --- 3) AGGIUNGO LA PRENOTAZIONE ---
        // Creo una nuova prenotazione usando solo l’ora di inizio
        // creo oggetto di nome nuova dove verranno salvate le prenotazioni
        // nome cliente e inizio sono attributi della classe prenotazione

        Prenotazione nuova = new Prenotazione(nomeCliente, inizio, fine);

        // La aggiungo alla lista delle prenotazioni
        prenotazioni.add(nuova);

        // Messaggio di conferma
        System.out.println("Il campo è stato correttamente prenotato");

                    return true; // Prenotazione avvenuta con successo
                }

    public void stampaTuttePrenotazioni() {
        if (prenotazioni.isEmpty()) {
            System.out.println("Nessuna prenotazione presente.");
            return;
    }

    System.out.println("\n--- Lista prenotazioni ---");
    for (Prenotazione p : prenotazioni) {
        System.out.println(
                "Cliente: " + p.getNomeCliente() +
                        " | dalle ore " + p.getInizio()

                        + " alle ore " + p.getFine()

        );
    }
    System.out.println("--------------------------\n");
    }



}

