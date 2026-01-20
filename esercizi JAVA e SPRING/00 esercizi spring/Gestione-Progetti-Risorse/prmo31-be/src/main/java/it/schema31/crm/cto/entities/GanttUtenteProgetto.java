package it.schema31.crm.cto.entities;

import java.util.List;
import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class GanttUtenteProgetto extends PanacheEntity   {

    @ManyToMany
    List<UtenteProgetto> utenteProgetti;

    @Embedded
    public CommonEntity commonEntity;
    
    public static GanttUtenteProgetto findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<GanttUtenteProgetto> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }
}