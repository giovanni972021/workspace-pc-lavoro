package it.schema31.crm.cto.entities;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
/**
 * Entity JobTitle
 * rappresenta una qualifica lavorativa all'interno dell'azienda
 */
@Entity
public class JobTitle extends PanacheEntity {

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = true)
    public String nome;

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String descrizione;
   
    @Embedded
    public CommonEntity commonEntity;

    public static JobTitle findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<JobTitle> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(JobTitle entity) {
        return find("nome", entity.nome).count() > 0;
    }
}