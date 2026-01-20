package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // apri applicazione
    @RequestMapping(value = "/ciao")
    public String saluto() {
        return "Ciao! Benvenuto nella nostra app!";
    }

    // accedi con login e password
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String username, @RequestParam String password) {
        // In un caso reale controlleresti username/password
        if ("admin".equals(username) && "1234".equals(password)) {
            return "Login effettuato con successo!";
        } else {
            return "Username o password errati!";
        }
    }

    // utente Esiste?

    // 3. Utente specifico (solo se presente parametro 'nome')
    @RequestMapping(value = "/utente", params = "nome")
    public String utente(@RequestParam String nome) {
        return "Benvenuto, " + nome + "! Questi sono i tuoi dati.";
    }

    // contenuto segreto

    // 4. Contenuto segreto (solo se header speciale)
    @RequestMapping(value = "/segreto", headers = "X-Tipo=speciale")
    public String segreto() {
        return "Contenuto segreto: solo chi ha l’header giusto può vederlo!";
    }
    // accesso a dati privati

    // 5. Combinazione avanzata (POST + parametro + header)
    @RequestMapping(value = "/dati", method = RequestMethod.POST, params = "id", headers = "X-Autorizzazione=12345")
    public String datiCombinati(@RequestParam String id) {
        return "Accesso consentito ai dati per ID: " + id;
    }
}
