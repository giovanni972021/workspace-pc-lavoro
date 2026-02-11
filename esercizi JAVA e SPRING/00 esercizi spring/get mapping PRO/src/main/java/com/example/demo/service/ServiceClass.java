package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    public String saluta() {
        return "Ciao da Spring Boot con Service!";
    }

}