package com.example.demo.controller;

import com.example.demo.service.CalcoloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalcoloController {

    private final CalcoloService calcoloService;

    public CalcoloController(CalcoloService calcoloService) {
        this.calcoloService = calcoloService;
    }

    @GetMapping("/add")
    public String add(@RequestParam int a, @RequestParam int b) {
        int risultato = calcoloService.somma(a, b);
        return "programma annotazione Il risultato di " + a + " + " + b + " è " + risultato;
    }
}
