package com.example.demo.controller;

// Import necessari per le annotazioni di Spring
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Definisce la rotta base per tutti i metodi in questa classe
public class Controller {

    /**
     * Risponde all'indirizzo: http://localhost:8080/api/ciao
     */
    @GetMapping("/ciao")
    public String saluto() {
        return "Ciao dal server Spring!";
    }

    /**
     * Risponde all'indirizzo: http://localhost:8080/api/addio
     */
    @GetMapping("/addio")
    public String addio() {
        return "Arrivederci dal server Spring!";
    }
}