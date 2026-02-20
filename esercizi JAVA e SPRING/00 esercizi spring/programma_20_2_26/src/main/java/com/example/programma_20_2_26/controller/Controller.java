package com.example.programma_20_2_26.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.programma_20_2_26.service.ServiceClass;

@RestController
@RequestMapping("/api")
// Imposta il prefisso "api" per tutti gli endpoint della classe
@Tag(name = "Controller es base spring boot", description = "Gestisce i messaggi di base")
// Titolo nella legenda
public class Controller {

    private final ServiceClass salutoService;

    public Controller(ServiceClass salutoService) {
        this.salutoService = salutoService;
    }

    @Operation(summary = "api base", description = "Ritorna il messaggio di saluto dal Service")
    @GetMapping("/saluto")
    // Path finale: localhost:8080/api/saluto
    public String saluto() {
        return salutoService.saluta();
    }
}
