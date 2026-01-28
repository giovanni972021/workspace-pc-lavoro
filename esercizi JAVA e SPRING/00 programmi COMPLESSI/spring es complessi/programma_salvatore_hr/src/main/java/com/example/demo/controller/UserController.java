package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper; // Jackson ObjectMapper

    // Endpoint per ottenere tutti gli utenti
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Endpoint per esportare gli utenti in un file JSON
    @GetMapping("/exportUsers")
    public String exportUsersToJson() {
        try {
            // Ottieni la lista di utenti dal database
            List<User> users = userService.getAllUsers();

            // Salva la lista di utenti in un file JSON
            objectMapper.writeValue(new File("users.json"), users);

            return "Dati esportati correttamente in users.json";
        } catch (IOException e) {
            e.printStackTrace();
            return "Errore durante l'esportazione dei dati";
        }
    }
}
