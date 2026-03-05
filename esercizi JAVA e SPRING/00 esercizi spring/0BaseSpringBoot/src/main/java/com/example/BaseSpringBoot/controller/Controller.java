package com.example.BaseSpringBoot.controller;

import com.example.BaseSpringBoot.service.ServiceClass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
