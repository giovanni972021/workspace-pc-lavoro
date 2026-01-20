package com.example.demo.component;

import org.springframework.stereotype.Component;

@Component("scientificCalculator")
public class ScientificCalculator implements Calculator {

    @Override
    public int add(int a, int b) {
        // esempio: aggiungo 1 per farlo diverso
        return a + b + 1;
    }

    @Override
    public int multiply(int a, int b) {
        // esempio: moltiplico e aggiungo 10
        return a * b + 10;
    }
}
