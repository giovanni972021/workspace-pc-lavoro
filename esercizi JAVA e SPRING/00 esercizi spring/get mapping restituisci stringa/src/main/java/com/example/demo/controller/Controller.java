package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    /**
     * Questo endpoint restituisce un testo semplice (Plain Text)
     * specificando esplicitamente la codifica UTF-8.
     */
    @GetMapping(value = "/saluto", produces = "text/plain;charset=UTF-8")
    public String getPlainGreeting() {
        return "Ciao! Benvenuto nel mondo di Spring Boot. 🚀 \n" +
                "Caratteri speciali testati: à, è, ì, ò, ù.";
    }
}