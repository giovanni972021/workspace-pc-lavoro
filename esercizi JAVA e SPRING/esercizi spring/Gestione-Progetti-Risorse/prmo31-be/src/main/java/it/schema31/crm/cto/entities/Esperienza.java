package it.schema31.crm.cto.entities;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;

@Entity
public class Esperienza extends PanacheEntity {

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String azienda;

    @Size(max = 100)
    @Column(length = 100)
    public String ruolo;

    @Column
    public java.time.LocalDate dataInizio;

    @Column
    public java.time.LocalDate dataFine;

    @Column(length = 4000)
    public String descrizione;

    @Column(length = 4000, nullable = false)
    public String attivitaSvolte;

    @Column(length = 500)
    public String tecnologie;

    @Embedded
    public CommonEntity commonEntity;
    
    public static Esperienza findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<Esperienza> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(Esperienza entity) {
        return find("nome", entity.azienda).count() > 0;
    }
}