package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/giochi")
public class VideogameController {

    private List<String> listaGiochi = new ArrayList<>(Arrays.asList("Elden Ring", "FIFA", "Zelda"));

    // --- LEGGERE TUTTI i giochi---
    @GetMapping("/tutti")
    public List<String> getGiochi() {
        return listaGiochi;
    }

    // --- AGGIUNGI a lista con POST ---
    @PostMapping("/aggiungi")
    public String aggiungiGiochi(@RequestBody List<String> nomiGiochi) {
        // addAll unisce la lista che invii a quella esistente
        listaGiochi.addAll(nomiGiochi);
        return "Hai aggiunto i seguenti giochi: " + nomiGiochi;
    }

    // --- AGGIORNA lista con (PUT) ---
    @PutMapping("/modifica/{indice}")
    public String modificaGioco(@PathVariable int indice, @RequestBody String nuovoNome) {
        // .set(posizione, nuovoValore) sostituisce l'elemento
        listaGiochi.set(indice, nuovoNome);
        return "Gioco aggiornato in: " + nuovoNome;
    }

    // --- ELIMINARE (DELETE) ---
    @DeleteMapping("/elimina/{indice}")
    public String eliminaGioco(@PathVariable int indice) {
        // .remove(posizione) toglie l'elemento dalla lista
        String rimosso = listaGiochi.remove(indice);
        return "Hai rimosso: " + rimosso;
    }
}