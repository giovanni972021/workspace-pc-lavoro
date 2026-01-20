package it.schema31.crm.cto.entities;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class LevelSoftSkill extends PanacheEntity {

    @ManyToOne
    public SoftSkill softSkill;

    @Column
    public Integer livello;
   
    @Embedded
    public CommonEntity commonEntity;
    
    public static LevelSoftSkill findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<LevelSoftSkill> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(LevelSoftSkill entity) {
        return find("softSkill.id = ?1 and livello = ?2", entity.softSkill.id, entity.livello).count() > 0;
    }
}