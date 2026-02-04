package com.example.programma_base_spring_boot;

import com.example.BaseSpringBoot.controller.Controller;
import com.example.BaseSpringBoot.service.ServiceClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ControllerTest {

  @Test
  void testSalutoIsolato() {
    // 1. ARRANGE: Creiamo il manichino (Mock)
    ServiceClass serviceMock = Mockito.mock(ServiceClass.class);

    // Programmiamo il manichino
    when(serviceMock.saluta()).thenReturn("Messaggio Finto");

    // Infiliamo il manichino nel Controller
    Controller controller = new Controller(serviceMock);

    // 2. ACT
    String risultato = controller.saluto();

    // 3. ASSERT
    assertEquals("Messaggio Finto", risultato, "Il controller deve usare il messaggio del mock!");
  }
}