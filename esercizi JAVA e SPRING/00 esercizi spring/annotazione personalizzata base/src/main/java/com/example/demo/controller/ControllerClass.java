package com.example.demo.controller;

import com.example.demo.Log_Esecuzione.LogEsecuzione;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerClass {

    @GetMapping("/test")
    @LogEsecuzione(messaggio = "Sto calcolando i dati per l'utente")
    public String saluta() throws InterruptedException {
        Thread.sleep(500);
        return "Operazione completata!aaaaaaa";
    }
}