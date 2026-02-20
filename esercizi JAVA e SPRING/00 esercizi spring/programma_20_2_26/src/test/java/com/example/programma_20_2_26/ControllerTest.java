package com.example.programma_20_2_26; // Allineato al package del controller originale

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.programma_20_2_26.controller.Controller;
import com.example.programma_20_2_26.service.ServiceClass;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
class ControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean // Se usi Spring Boot 3.4+ puoi usare @MockitoBean, altrimenti usa @MockBean
  private ServiceClass salutoService;

  @Test
  void testSaluto() throws Exception {
    // 1. Arrange
    String messaggioMock = "Ciao dal Service Mockato!";
    when(salutoService.saluta()).thenReturn(messaggioMock);

    // 2. Act & Assert
    mockMvc.perform(get("/api/saluto"))
        .andExpect(status().isOk())
        .andExpect(content().string(messaggioMock));
  }
}