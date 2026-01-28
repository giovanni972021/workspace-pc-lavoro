package com.example.demo;

// Indica la cartella in cui si trova questo file per organizzarlo nel progetto.

import org.springframework.boot.SpringApplication;

// Importa le istruzioni necessarie per far partire l'applicazione.
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Importa l'automatismo che configura tutto da solo.

/**
 * Questa è la classe principale che avvia l'intero sistema di gestione
 * magazzino.
 */
@SpringBootApplication

// annotazione magica: accende tutte le funzionalità di Spring Boot in un colpo
// solo.
public class MagazzinoApplication {

    // Questo è il punto di partenza: quando premi "Play", il computer inizia a
    // leggere da qui.
    public static void main(String[] args) {

        // Questo comando mette in moto l'app e la mantiene accesa finché non decidi di
        // spegnerla.
        SpringApplication.run(MagazzinoApplication.class, args);

    }

    // Fine del punto di partenza.

}

// Fine della classe principale.
