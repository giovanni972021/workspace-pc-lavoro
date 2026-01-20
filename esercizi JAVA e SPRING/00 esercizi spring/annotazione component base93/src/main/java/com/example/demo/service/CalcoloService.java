package com.example.demo.service;

import com.example.demo.component.Utility;
import org.springframework.stereotype.Service;

@Service
public class CalcoloService {

    private final Utility utility;

    // Dependency injection del Component
    public CalcoloService(Utility utility) {
        this.utility = utility;
    }

    public int somma(int a, int b) {
        return utility.add(a, b);
    }
}
