package main.java;

import java.util.ArrayList;
import java.util.List;

public class GestoreRisorse {
    // dichiaro lista di nome risorseDisponibili di tipo risorsa
    // Risorsa è nome classe che contiene caratteristiche degli elementi della lista
    // come nome quantita ecc
    // Lista che conterrà tutte le risorse disponibili
    private final List<Risorsa> risorseDisponibili;

    // Costruttore: qui inizializzo la lista
    public GestoreRisorse() {
        // creo lista
        // Creo la lista vuota all'atto della creazione dell'oggetto
        risorseDisponibili = new ArrayList<>();
    }

    // Aggiunge una risorsa alla lista
    public void aggiungiRisorsa(Risorsa risorsa) {
        if (risorsa == null) {
            throw new IllegalArgumentException("Non è possibile aggiungere una risorsa nulla");
        }
        risorseDisponibili.add(risorsa);
    }

    // Mostra tutte le risorse
    public void stampaRisorseDisponibili() {
        System.out.println("\nRisorse disponibili:");
        risorseDisponibili.forEach(r -> System.out.println("- " + r));
        System.out.println("\n");
    }
}
