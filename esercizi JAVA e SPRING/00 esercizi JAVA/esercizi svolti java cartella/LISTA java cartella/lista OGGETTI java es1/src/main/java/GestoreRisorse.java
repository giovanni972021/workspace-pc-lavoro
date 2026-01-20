package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestoreRisorse {
    // creo lista di nome risorseDisponibili FUORI da costruttore
    // Risorsa è nome classe dove dichiaro caratteristiche di ogni elemento della
    // lista ossia nome eta ecc
    private final List<Risorsa> risorseDisponibili = new ArrayList<>();

    // Aggiunge una risorsa alla lista
    public void aggiungiRisorsa(Risorsa risorsa) {
        if (risorsa == null) {
            throw new IllegalArgumentException("Non è possibile aggiungere una risorsa nulla");
        }
        risorseDisponibili.add(risorsa);
    }

    // Restituisce una lista immutabile delle risorse
    public List<Risorsa> getRisorseDisponibili() {
        return Collections.unmodifiableList(risorseDisponibili);
    }

    // Mostra tutte le risorse
    public void mostraRisorse() {
        System.out.println("\nRisorse disponibili\n");
        risorseDisponibili.forEach(r -> System.out.println("- " + r));
    }
}
