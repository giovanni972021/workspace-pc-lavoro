package com.example.demo.controller;

import com.example.demo.model.Product;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    // Questa lista è la nostra "Memoria Temporanea".
    // All'inizio è vuota. Si riempirà solo quando userai Postman.
    private List<Product> listaProdotti = new ArrayList<>();

    // 1. IL METODO PER AGGIUNGERE (Usa Postman qui)
    @RequestMapping(value = "/prodotti", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product nuovo) {
        listaProdotti.add(nuovo); // Qui lo aggiungiamo davvero alla lista!
        return "Hai aggiunto: " + nuovo.getNome();
    }

    // 2. IL METODO PER CONTROLLARE (Usa il Browser qui)
    @RequestMapping(value = "/prodotti", method = RequestMethod.GET)
    public List<Product> getAll() {
        return listaProdotti; // Ti mostra tutto quello che hai aggiunto finora
    }
}