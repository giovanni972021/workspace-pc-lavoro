package com.example.programma_base_spring_boot;

import org.junit.jupiter.api.Test;
import com.example.BaseSpringBoot.Calcolatrice;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcolatriceTest {

  @Test // Fondamentale!
  void testSomma() {
    Calcolatrice calc = new Calcolatrice();
    int risultato = calc.somma(2, 2);
    assertEquals(4, risultato, "L'allarme suona: 2+2 deve fare 4!");
  }

  @Test // Serve anche qui per farlo apparire in VS Code
  void testMoltiplicazione() {
    // 1. Arrange: Devo ricreare calc o dichiararlo fuori
    Calcolatrice calc = new Calcolatrice();

    // 2. Act
    int risultato = calc.moltiplicazione(3, 3);

    // 3. Assert
    // Nota: ho cambiato i numeri così vedi che funziona davvero!
    assertEquals(9, risultato, "L'allarme suona: 3*3 deve fare 9!");
  }
}