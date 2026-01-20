package com.example.programma_base_spring_boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hellocontroller {

    @GetMapping("/ciao")
    public String ciaoMondo() {
        return "Ciao mondo1!";
    }
}
