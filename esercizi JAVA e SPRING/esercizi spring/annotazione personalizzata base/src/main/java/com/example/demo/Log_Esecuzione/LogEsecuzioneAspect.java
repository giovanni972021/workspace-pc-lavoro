package com.example.demo.Log_Esecuzione;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogEsecuzioneAspect {

    // Intercettiamo tutti i metodi annotati con @LogEsecuzione
    @Around("@annotation(logEsecuzione)")
    public Object logTime(ProceedingJoinPoint joinPoint, LogEsecuzione logEsecuzione) throws Throwable {
        long inizio = System.currentTimeMillis();

        // Stampiamo il messaggio personalizzato definito nell'annotazione
        System.out.println(">>> NOTA: " + logEsecuzione.messaggio());

        // Eseguiamo il metodo originale
        Object proceed = joinPoint.proceed();

        long durata = System.currentTimeMillis() - inizio;
        System.out.println(">>> " + joinPoint.getSignature() + " eseguito in " + durata + "ms");

        return proceed;
    }
}