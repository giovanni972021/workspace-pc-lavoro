package com.example.demo.repository;
// cartella repository dove Spring cercherà i componenti per gestire i dati

import com.example.demo.model.Movimento;
// Importa la classe Movimento per comunicare al database quale tabella gestire
import org.springframework.data.jpa.repository.JpaRepository;
// Importa funzioni base come salva, elimina e cerca già pronte da usare
import org.springframework.stereotype.Repository;
// Etichetta la classe come componente speciale che gestisce la comunicazione con il database

@Repository
/*
 * Indica a Spring Boot di creare automaticamente l'istanza di questa classe nel
 * contenitore
 */
public interface MovimentoRepository extends JpaRepository<Movimento, Long> {

    /*
     * Eredita tutti i metodi CRUD (create read update delete) usando Movimento come
     * oggetto e Long come tipo ID
     */
}
