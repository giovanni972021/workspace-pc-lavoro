package com.example.BaseSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(DemoApplication.class, args);
            System.out.println("\n\nPROGRAMMA ESEGUITO CORRETTAMENTE\n\n");
        } catch (Exception e) {
            System.out.println("ERRORE:\n\n");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            System.out.println("\n\n");
        }
    }
}
