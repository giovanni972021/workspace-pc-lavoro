package com.example.BaseSpringBoot;

import org.springframework.stereotype.Component;

@Component
public class Calcolatrice {

  public int somma(int a, int b) {
    return a + b;
  }

  public int moltiplicazione(int a, int b) {
    // SABOTAGGIO: aggiungiamo + 1 apposta per far fallire il test
    return a * b + 1;
  }
}