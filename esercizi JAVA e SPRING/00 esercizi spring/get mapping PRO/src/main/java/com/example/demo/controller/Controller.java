package com.example.demo.controller;

//librerie x annotazioni
/*importo libreria per poter usare annotazione get mapping */
import org.springframework.web.bind.annotation.GetMapping;

/*importo libreria per poter usare annotazione rest controller*/
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/ciao")
    public String saluto() {
        return "Ciao dal server Spring!";
    }
}
