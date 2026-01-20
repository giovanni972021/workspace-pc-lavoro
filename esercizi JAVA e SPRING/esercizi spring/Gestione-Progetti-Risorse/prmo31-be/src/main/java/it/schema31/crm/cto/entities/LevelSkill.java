package it.schema31.crm.cto.entities;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class LevelSkill extends PanacheEntity {

    @ManyToOne
    public Skill skill;

    //Livello da 1 a 10
    @Column(nullable = false)
    public Integer livello;
   
    @Embedded
    public CommonEntity commonEntity;
    
    public static LevelSkill findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<LevelSkill> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(LevelSkill entity) {
        return find("skill.id = ?1 and livello = ?2", entity.skill.id, entity.livello).count() > 0;
    }
}