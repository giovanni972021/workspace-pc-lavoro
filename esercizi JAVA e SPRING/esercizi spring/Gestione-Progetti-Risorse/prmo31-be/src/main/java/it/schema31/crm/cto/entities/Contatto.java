package it.schema31.crm.cto.entities;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity
public class Contatto extends PanacheEntity {

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String riferimento;

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String valore;
   
    @ManyToOne(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JsonProperty
    public TipoContatto tipoContatto;

    @Embedded
    public CommonEntity commonEntity;
    
    public static Contatto findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<Contatto> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(Contatto entity) {
        return find("riferimento", entity.riferimento).count() > 0;
    }
}