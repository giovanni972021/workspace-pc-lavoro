package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/ciao")
  public String ciaoMondo() {
    return "Ciao mondooooooo!";
  }
}
