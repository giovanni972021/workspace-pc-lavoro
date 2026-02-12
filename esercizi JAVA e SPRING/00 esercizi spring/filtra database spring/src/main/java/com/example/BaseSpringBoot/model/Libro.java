package com.example.BaseSpringBoot.model;

/**
 * Questa classe rappresenta la struttura dei dati.
 * È un semplice oggetto che descrive cosa contiene il database.
 */
public class Libro {
  public String titolo;
  public double prezzo;

  public Libro(String titolo, double prezzo) {
    this.titolo = titolo;
    this.prezzo = prezzo;
  }
}