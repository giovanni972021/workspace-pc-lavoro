package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
  // Non devi scrivere nulla qui!
  // Ereditando da JpaRepository, hai già i metodi .save(), .findAll(), .delete()
}