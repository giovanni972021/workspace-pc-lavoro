package it.schema31.crm.cto.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity
@Audited(targetAuditMode =RelationTargetAuditMode.NOT_AUDITED)
public class Progetto extends PanacheEntity   {

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = true)
    public String nome;

    @Size(max = 100, min = 2)
    @Column(length = 4000, nullable = false, unique = false)
    public String descrizione;

    public LocalDate dataInizioPresunta;

    public LocalDate dataInizioEffettiva;

    public LocalDate dataFinePresunta;

    public LocalDate dataFineEffettiva;

    public double budgetPreventivato;

    public double budgetEffettivo;

    @ManyToOne
    public StatoProgetto stato;

    // priorita da 1 a 100
    public int priorita;

    // fattibilita da 1 a 100
    public int fattibilita;

    @ManyToMany
    public List<NumberJobTitle> numberJobTitles;
   
    @Embedded
    public CommonEntity commonEntity;
    
    public static Progetto findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<Progetto> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(Progetto entity) {
        return find("nome", entity.nome).count() > 0;
    }
}