package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // <--- FONDAMENTALE: trasforma la classe in tabella
public class Prodotto {

  @Id // <--- FONDAMENTALE: definisce la chiave primaria
  @GeneratedValue(strategy = GenerationType.IDENTITY) // <--- L'ID aumenta da solo
  private Long id;

  private String nome;
  private double prezzo;

  // COSTRUTTORE VUOTO: Obbligatorio per far funzionare Spring/JPA
  public Prodotto() {
  }

  // GETTER E SETTER per ID
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  // GETTER E SETTER per NOME
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  // GETTER E SETTER per PREZZO
  public double getPrezzo() {
    return prezzo;
  }

  public void setPrezzo(double prezzo) {
    this.prezzo = prezzo;
  }
}