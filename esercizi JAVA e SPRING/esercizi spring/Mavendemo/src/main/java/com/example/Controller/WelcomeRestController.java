package com.example.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class WelcomeRestController {
    @RequestMapping("/rest/welcome")
    public String welcome() {
        return "{message: \"welcome\"}";
    }
}
