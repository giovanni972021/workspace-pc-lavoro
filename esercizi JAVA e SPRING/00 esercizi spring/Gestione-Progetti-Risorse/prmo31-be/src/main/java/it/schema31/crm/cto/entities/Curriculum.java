package it.schema31.crm.cto.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;


@Entity
public class Curriculum extends PanacheEntity {

    public LocalDate dataNascita;

    @ManyToMany(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JsonProperty
    public List<Certificazione> certificazioni;

    @ManyToMany(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JsonProperty
    public List<LevelSkill> skills;

    @ManyToMany(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JsonProperty
    public List<LevelSoftSkill> softSkills;

    @ManyToMany(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JsonProperty
    public List<Incarico> incarichi;

    @ManyToMany(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
    @JsonProperty
    public List<Esperienza> esperienze;

    @OneToOne
    @JsonProperty
    public Utente utente;

    public String pathRelativoCv;

    @Embedded
    public CommonEntity commonEntity;

    public static Curriculum findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<Curriculum> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(Curriculum entity) {
        return find("nome", entity.utente.nome).count() > 0;
    }

    public static boolean exists(Curriculum entity, Long id) {
        return find("username = ?1 AND id <> ?2", entity.utente.username, id).count() > 0;
    }
}