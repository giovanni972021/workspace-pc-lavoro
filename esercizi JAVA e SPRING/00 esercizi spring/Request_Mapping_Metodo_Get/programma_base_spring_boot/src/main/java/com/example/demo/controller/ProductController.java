package com.example.demo.controller;

import com.example.demo.model.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {

    // Esempio richiesto: mappatura esplicita dell'URL e del Metodo GET
    @RequestMapping(value = "/prodotti", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return Arrays.asList(
                new Product(1L, "Laptop Gaming", 1500.00),
                new Product(2L, "Mouse Ottico", 30.50),
                new Product(3L, "Monitor 4K", 350.00));
    }
}