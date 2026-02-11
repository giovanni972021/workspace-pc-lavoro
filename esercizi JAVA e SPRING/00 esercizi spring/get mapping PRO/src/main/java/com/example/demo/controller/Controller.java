package com.example.demo.controller;

// Importazione per la gestione delle richieste GET
import org.springframework.web.bind.annotation.GetMapping;
// Importazione per definire il percorso base delle API
import org.springframework.web.bind.annotation.RequestMapping;
// Importazione per definire questa classe come un Controller REST
import org.springframework.web.bind.annotation.RestController;

// Importazione per documentare il titolo dell'operazione nella legenda
import io.swagger.v3.oas.annotations.Operation;
// Importazione per definire il gruppo principale nella legenda
import io.swagger.v3.oas.annotations.tags.Tag;

// Indica che questa classe gestirà le risposte HTTP in formato dati (JSON/Stringhe)
@RestController
// Imposta il prefisso "/api" obbligatorio per tutti gli indirizzi di questa
// classe
@RequestMapping("/api")
// Definisce il nome del gruppo che apparirà nella parte alta della legenda
// Swagger
@Tag(name = "Titolo1", description = "Area dedicata ai servizi di messaggistica")
public class Controller {

    // Definisce il titolo che apparirà accanto all'endpoint /ciao nella legenda
    @Operation(summary = "api base", description = "Restituisce un messaggio di benvenuto")
    // Associa questo metodo all'indirizzo specifico /ciao (URL completo: /api/ciao)
    @GetMapping("/ciao")
    public String saluto() {
        // Restituisce la stringa di testo al client che ha effettuato la chiamata
        return "Ciao dal server Spring!";
    }

    // Definisce il titolo che apparirà accanto all'endpoint /addio nella legenda
    @Operation(summary = "api addio", description = "Restituisce un messaggio di chiusura")
    // Associa questo metodo all'indirizzo specifico /addio (URL completo:
    // /api/addio)
    @GetMapping("/addio")
    public String addio() {
        // Restituisce il messaggio finale di saluto
        return "Arrivederci dal server Spring!";
    }
}