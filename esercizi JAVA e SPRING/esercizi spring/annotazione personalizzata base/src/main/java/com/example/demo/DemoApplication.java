package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

/*
 * per TESTARLO
 * nel TERMINALE
 * 
 * mvn clean
 * mvn spring-boot:run
 * 
 * su CHROME
 * http://localhost:8080/test
 * 
 */