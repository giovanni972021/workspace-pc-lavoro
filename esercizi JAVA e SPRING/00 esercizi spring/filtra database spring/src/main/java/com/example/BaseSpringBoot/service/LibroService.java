package com.example.BaseSpringBoot.service;

import org.springframework.stereotype.Service;

import com.example.BaseSpringBoot.model.Libro;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

  // Simulazione del database
  private final List<Libro> database = List.of(
      new Libro("Java per Principianti", 45.0),
      new Libro("Spring Boot Guida Rapida", 25.0),
      new Libro("Database NoSQL", 15.0));

  /**
   * Logica del FILTRO:
   * Prende la lista e tiene solo i libri con prezzo <= limite.
   */
  public List<Libro> filtraPerPrezzo(double limite) {
    return database.stream()
        .filter(libro -> libro.prezzo <= limite)
        .collect(Collectors.toList());
  }
}