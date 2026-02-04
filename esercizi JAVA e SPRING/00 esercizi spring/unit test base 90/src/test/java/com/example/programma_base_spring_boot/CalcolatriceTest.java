package com.example.programma_base_spring_boot;

import org.junit.jupiter.api.Test;

import com.example.BaseSpringBoot.Calcolatrice;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Nota: Non serve @SpringBootTest per un unit test banale, è più veloce così!
class CalcolatriceTest {

  @Test
  void testSomma() {
    // 1. Arrange
    Calcolatrice calc = new Calcolatrice();

    // 2. Act
    int risultato = calc.somma(2, 2);

    // 3. Assert
    assertEquals(4, risultato, "L'allarme suona: 2+2 deve fare 4!");
  }
}