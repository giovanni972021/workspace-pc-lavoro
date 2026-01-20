package it.schema31.crm.cto.entities;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Size;

@Entity
public class Ruolo extends PanacheEntity {

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = true)
    public String nome;

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String descrizione;

    @ManyToMany
    @JsonProperty
    public List<Permesso> permessi;
   
    @Embedded
    public CommonEntity commonEntity;
    
    public static Ruolo findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<Ruolo> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(Ruolo entity) {
        return find("nome", entity.nome).count() > 0;
    }
}