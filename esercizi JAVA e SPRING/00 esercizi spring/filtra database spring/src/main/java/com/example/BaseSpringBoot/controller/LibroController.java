package com.example.BaseSpringBoot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import com.example.BaseSpringBoot.model.Libro;
import com.example.BaseSpringBoot.service.LibroService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Libreria Digitale", description = "Ricerca filtrata nel database")
public class LibroController {

  private final LibroService libroService;

  // Costruttore per la Dependency Injection
  public LibroController(LibroService libroService) {
    this.libroService = libroService;
  }

  /**
   * Endpoint per la tua legenda Swagger.
   * URL: http://localhost:8080/api/filtra?prezzoMassimo=30
   */
  @Operation(summary = "Filtro Database", description = "Esegue un filtro sul prezzo dei libri")
  @GetMapping("/filtra")
  public List<Libro> cercaLibri(@RequestParam("prezzoMassimo") double prezzoMassimo) {
    // Chiama il metodo di filtro nel Service
    return libroService.filtraPerPrezzo(prezzoMassimo);
  }
}