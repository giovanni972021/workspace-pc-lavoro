package com.example.programma_base_spring_boot; // 1. Cambiato package per allinearlo all'app

import com.example.demo.model.Prodotto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate; // Import necessario per la scadenza

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; //

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testInserimentoProdotto() throws Exception {
		// 2. Aggiornato costruttore con la data di scadenza (oggi + 30 giorni)
		Prodotto p = new Prodotto("Martello", 50, 15.99, LocalDate.now().plusDays(30));

		// 3. Cambiato l'URL in "/prodotti" come definito nel nuovo Controller
		mockMvc.perform(post("/prodotti")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(p)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Martello"))
				.andExpect(jsonPath("$.quantita").value(50))
				.andExpect(jsonPath("$.scadenza").exists()); // Verifichiamo che la scadenza ci sia
	}
}