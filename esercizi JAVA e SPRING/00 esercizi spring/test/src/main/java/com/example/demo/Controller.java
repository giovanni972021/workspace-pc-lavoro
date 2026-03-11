package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

  // Il nostro "telecomando" per il database
  @Autowired
  private ProdottoRepository prodottoRepository;

  // --- METODO 1: SALVATAGGIO ---
  @PostMapping("/salvaProdotto")
  public String salvaProdotto(@RequestBody Prodotto p) {
    // 1. Calcoliamo l'IVA prima di salvare
    double prezzoIvato = p.getPrezzo() * 1.22;
    p.setPrezzo(prezzoIvato);

    // 2. Salviamo l'oggetto nel database fisico
    // La variabile 'prodottoSalvato' conterrà anche l'ID generato dal DB
    Prodotto prodottoSalvato = prodottoRepository.save(p);

    return "Prodotto salvato correttamente! ID assegnato dal DB: " + prodottoSalvato.getId();
  }

  // --- METODO 2: VISUALIZZAZIONE ---
  @GetMapping("/listaProdotti")
  public List<Prodotto> guardaTutti() {
    // Chiediamo al magazzino di darci tutto quello che ha
    return prodottoRepository.findAll();
  }

  // --- METODO VECCHIO (Opzionale, per test veloce) ---
  @GetMapping("/ciao")
  public String ciaoMondo() {
    return "Il sistema è online e pronto a ricevere prodotti!";
  }
}