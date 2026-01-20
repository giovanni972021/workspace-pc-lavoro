package com.example.demo.service;

import com.example.demo.component.Calculator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private final Calculator calculator;

    // Usando @Qualifier per scegliere quale bean iniettare
    public CalculationService(@Qualifier("basicCalculator") Calculator calculator) {
        this.calculator = calculator;
    }

    public int somma(int a, int b) {
        return calculator.add(a, b);
    }

    public int moltiplica(int a, int b) {
        return calculator.multiply(a, b);
    }
}
