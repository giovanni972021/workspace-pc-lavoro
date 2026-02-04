package com.example.BaseSpringBoot;

import org.springframework.stereotype.Component;

@Component
public class Calcolatrice {

  public int somma(int a, int b) {
    return a + b;
  }

  public int moltiplicazione(int a, int b) {
    return a * b + 1;
  }
}