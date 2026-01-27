import java.time.LocalDate;
import java.util.*;

public abstract class Campo {

    protected final TipoCampo tipoCampo;
    protected final Map<LocalDate, List<Disponibilita>> disponibilita;
    protected final List<Prenotazione> prenotazioni;

    protected Campo(TipoCampo tipoCampo) {
        this.tipoCampo = tipoCampo;
        this.disponibilita = new HashMap<>();
        this.prenotazioni = new ArrayList<>();
        inizializzaDisponibilita();
    }

    protected abstract void inizializzaDisponibilita();

    public boolean prenota(LocalDate data, int inizio, int fine, String nome) {
        List<Disponibilita> slotGiorno = disponibilita.get(data);
        if (slotGiorno == null || inizio >= fine)
            return false;

        for (Disponibilita d : slotGiorno) {
            if (d.isDisponibile() && d.getInizio() == inizio && d.getFine() == fine) {
                d.prenota();
                prenotazioni.add(new Prenotazione(nome, inizio, fine, data, tipoCampo));
                return true;
            }
        }
        return false;
    }

    public boolean rimuoviPrenotazione(String nome, LocalDate data, int inizio, int fine) {
        Iterator<Prenotazione> it = prenotazioni.iterator();
        while (it.hasNext()) {
            Prenotazione p = it.next();
            if (p.getNomeCliente().equalsIgnoreCase(nome)
                    && p.getData().equals(data)
                    && p.getInizio() == inizio
                    && p.getFine() == fine) {

                it.remove();
                for (Disponibilita d : disponibilita.get(data)) {
                    if (d.getInizio() == inizio && d.getFine() == fine) {
                        d.libera();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Prenotazione> cercaPerNome(String nome) {
        List<Prenotazione> risultati = new ArrayList<>();
        for (Prenotazione p : prenotazioni) {
            if (p.getNomeCliente().equalsIgnoreCase(nome))
                risultati.add(p);
        }
        return risultati;
    }

    public void stampaDisponibili() {
        System.out.println("\n--- Disponibilità " + tipoCampo + " ---");
        for (LocalDate data : disponibilita.keySet()) {
            System.out.println("Data: " + data);
            for (Disponibilita d : disponibilita.get(data)) {
                System.out.println("  " + d);
            }
        }
    }

    public void stampaPrenotate() {
        System.out.println("\n--- Prenotazioni " + tipoCampo + " ---");
        if (prenotazioni.isEmpty()) {
            System.out.println("Nessuna prenotazione.");
            return;
        }
        prenotazioni.forEach(System.out::println);
    }
}
