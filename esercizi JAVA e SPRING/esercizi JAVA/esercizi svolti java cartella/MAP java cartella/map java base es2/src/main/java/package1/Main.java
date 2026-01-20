package main.java.package1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // OBIETTIVO della map associare data a disponibilita del calendario
        // dichiaro mappa di nome calendario con chiave data locale e valore la lista di
        // tipo Disponibilita dove Disponibilita è il nome della classe che contiene
        // tutte le caratteristiche della disponibilita
        Map<LocalDate, List<Disponibilita>> calendario;

        // 2. Creazione della mappa
        calendario = new HashMap<>();

        // Creazione di alcune disponibilità per diversi giorni
        List<Disponibilita> disponibilitaGiorno1 = new ArrayList<>();
        disponibilitaGiorno1.add(new Disponibilita("Sala A", true));
        disponibilitaGiorno1.add(new Disponibilita("Sala B", false));

        List<Disponibilita> disponibilitaGiorno2 = new ArrayList<>();
        disponibilitaGiorno2.add(new Disponibilita("Sala A", false));
        disponibilitaGiorno2.add(new Disponibilita("Sala C", true));

        // Inserimento nella mappa
        calendario.put(LocalDate.of(2025, 12, 24), disponibilitaGiorno1);
        calendario.put(LocalDate.of(2025, 12, 25), disponibilitaGiorno2);

        // Stampa delle disponibilità di un giorno specifico
        LocalDate giorno = LocalDate.of(2025, 12, 24);
        System.out.println("\nDisponibilità del " + giorno + ": " + calendario.get(giorno));

        // Stampa di tutte le disponibilità
        System.out.println("\nTutte le disponibilità:");
        for (LocalDate data : calendario.keySet()) {
            System.out.println(data + " -> " + calendario.get(data));
        }

        System.out.println("\n");
    }
}
