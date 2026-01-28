package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.User; // Importa la classe Entity

public interface UserRepository extends JpaRepository<User, Long> {
    // Puoi aggiungere metodi personalizzati, ma per ora va bene così
}
