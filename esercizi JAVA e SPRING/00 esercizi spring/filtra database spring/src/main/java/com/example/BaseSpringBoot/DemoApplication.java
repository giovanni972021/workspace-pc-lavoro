package com.example.BaseSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Classe principale che avvia Spring Boot
 */
@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {

    SpringApplication.run(DemoApplication.class, args);

    System.out.println("Applicazione avviata correttamente!");
  }
}
