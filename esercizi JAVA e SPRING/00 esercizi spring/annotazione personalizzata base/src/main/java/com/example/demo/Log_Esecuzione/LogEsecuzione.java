package com.example.demo.Log_Esecuzione;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Indica che l'annotazione sarà disponibile a runtime
@Retention(RetentionPolicy.RUNTIME)
// Indica che può essere usata solo sui metodi
@Target(ElementType.METHOD)
public @interface LogEsecuzione {
    String messaggio() default "Esecuzione metodo in corso...";
}