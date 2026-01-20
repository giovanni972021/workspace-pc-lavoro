package it.schema31.crm.cto.entities;

import java.util.List;
import java.util.Optional;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Audited(targetAuditMode =RelationTargetAuditMode.NOT_AUDITED)
@NamedQuery(name = "Utente.containsInName", query = "from Utente u where u.nome like CONCAT('%', CONCAT(:name, '%'))")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Utente extends PanacheEntity {

    @NotBlank
    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String nome;

    @NotBlank
    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String cognome;

    @NotBlank
    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = false)
    public String username;

    @NotBlank
    @Size(max = 16, min = 16)
    @Column(length = 16, nullable = false, unique = true)
    public String codiceFiscale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty
    public TipoUtente tipoUtente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty
    public JobTitle jobTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty
    public Profilo profilo;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    public Curriculum curriculum;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JsonProperty
    public List<Contatto> contatti;

    @Column
    public java.time.LocalDate dataInizioCollaborazione;

    @Column
    public java.time.LocalDate dataFineCollaborazione;

    @Column
    public Integer valutazione;

    @Column(length = 4000)
    public String note;

    @Column(columnDefinition = "boolean default true")
    public Boolean attivo;
    
    @Embedded
    public CommonEntity commonEntity;

    public static Utente findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<Utente> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(Utente entity) {
        return find("nome", entity.username).count() > 0;
    }

    public static boolean exists(Utente entity, Long id) {
        return find("username = ?1 AND id <> ?2", entity.username, id).count() > 0;
    }

    public static List<Utente> list(String query, Object... params) {
        return find(query, params).list();
    }

}

