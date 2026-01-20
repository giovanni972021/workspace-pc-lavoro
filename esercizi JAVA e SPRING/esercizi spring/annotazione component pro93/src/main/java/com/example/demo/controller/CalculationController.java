package com.example.demo.controller;

import com.example.demo.service.CalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculationController {

    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping("/add")
    public String add(@RequestParam int a, @RequestParam int b) {
        int result = calculationService.somma(a, b);
        return "Somma usando BasicCalculator: " + result;
    }

    @GetMapping("/multiply")
    public String multiply(@RequestParam int a, @RequestParam int b) {
        int result = calculationService.moltiplica(a, b);
        return "Moltiplicazione usando BasicCalculator: " + result;
    }
}
