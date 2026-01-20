package it.schema31.crm.cto.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * Entity NumberJobTitle
 * rappresenta il numero di risorse per una specifica qualifica lavorativa all'interno dell'azienda
 */
@Entity
public class NumberJobTitle extends PanacheEntity {

    public Integer number;

    @ManyToOne
    public JobTitle jobTitle;
}
