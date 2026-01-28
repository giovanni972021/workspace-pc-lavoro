package com.example.demo.entity; // Verifica che il package sia quello corretto del tuo progetto

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Assicurati che la tabella su MySQL si chiami esattamente 'users'
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Se nel DB la colonna si chiama 'name', questo va bene così
    private String name;

    // Qui risolviamo il tuo problema:
    // Mappiamo il campo Java 'email' alla colonna 'userName' del database
    @Column(name = "`userName`", nullable = false)
    private String email;

    // Costruttore vuoto (obbligatorio per JPA)
    public User() {
    }

    // Costruttore pieno (comodo per i tuoi test)
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // --- GETTER E SETTER ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}