package com.example.BaseSpringBoot.controller;

import com.example.BaseSpringBoot.service.ServiceClass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final ServiceClass salutoService;

    // Dependency injection tramite costruttore
    public Controller(ServiceClass salutoService) {
        this.salutoService = salutoService;
    }

    // Endpoint base
    @GetMapping("/saluto")
    public String saluto() {
        return salutoService.saluta();
    }

}
