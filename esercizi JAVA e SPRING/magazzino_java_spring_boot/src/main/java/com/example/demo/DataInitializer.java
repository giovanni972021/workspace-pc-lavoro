package com.example.demo;

import com.example.demo.model.Prodotto;
import com.example.demo.repository.ProdottoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProdottoRepository repository;

    public DataInitializer(ProdottoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            // Inserimento prodotti reali per un magazzino ortofrutta
            repository.save(new Prodotto("Banane Chiquita", 150, 1.99, LocalDate.now().plusDays(7)));
            repository.save(new Prodotto("Mele Golden", 200, 2.50, LocalDate.now().plusDays(15)));
            repository.save(new Prodotto("Susine Nere", 80, 3.20, LocalDate.now().plusDays(5)));
            repository.save(new Prodotto("Pere Abate", 120, 2.80, LocalDate.now().plusDays(10)));
            repository.save(new Prodotto("Arance Tarocco", 300, 1.50, LocalDate.now().plusDays(20)));
            repository.save(new Prodotto("Kiwi Zespri", 100, 4.50, LocalDate.now().plusDays(12)));
            repository.save(new Prodotto("Uva Vittoria", 60, 3.90, LocalDate.now().plusDays(4)));
            repository.save(new Prodotto("Fragole Candonga", 40, 5.50, LocalDate.now().plusDays(3)));
            repository.save(new Prodotto("Ananas Del Monte", 30, 2.20, LocalDate.now().plusDays(14)));
            repository.save(new Prodotto("Limoni di Sorrento", 90, 1.80, LocalDate.now().plusDays(30)));

            System.out.println(
                    "\n\n\n>>> Database Magazzino inizializzato!\n\n nel browser cerca \n\n http://localhost:8080/prodotti \n\n\n");
        }
    }
}