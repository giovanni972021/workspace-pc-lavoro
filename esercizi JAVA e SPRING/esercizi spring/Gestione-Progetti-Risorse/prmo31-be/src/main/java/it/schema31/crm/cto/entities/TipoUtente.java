package it.schema31.crm.cto.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
/**
 * Entity TipoUtente
 * rappresenta una tipologia di utente del sistema: consulente, dipendente, timematerial, etc
 */
@Entity
public class TipoUtente extends PanacheEntity {

    public String tipo;

    public String descrizione;

    @Embedded
    public CommonEntity commonEntity;

    public static TipoUtente findByTipo(String tipo) {
        return find("tipo", tipo).firstResult();
    }


}
