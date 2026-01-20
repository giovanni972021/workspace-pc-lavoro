package com.example.demo.component;

import org.springframework.stereotype.Component;

@Component("basicCalculator")
public class BasicCalculator implements Calculator {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int multiply(int a, int b) {
        return a * b;
    }
}
