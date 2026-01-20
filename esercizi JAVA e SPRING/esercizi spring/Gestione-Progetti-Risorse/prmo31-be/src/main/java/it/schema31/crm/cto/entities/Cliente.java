package it.schema31.crm.cto.entities;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;

@Entity
public class Cliente extends PanacheEntity   {

    @Size(max = 100, min = 2)
    @Column(length = 100, nullable = false, unique = true)
    public String nome;

    @Size(max = 255, min = 2)
    @Column(length = 255, nullable = true)
    public String descrizione;

    // feedBack da 1 -> 10
    public int feedbackScore;

    @Size(max = 16)
    @Column(length = 16, nullable = true)
    public String partitaIva;

    @Size(max = 16)
    @Column(length = 16, nullable = true)
    public String codiceFiscale;

    @Size(max = 255)
    @Column(length = 255, nullable = true)
    public String indirizzo;

    @Size(max = 100)
    @Column(length = 100, nullable = true)
    public String citta;

    @Size(max = 2)
    @Column(length = 2, nullable = true)
    public String provincia;

    @Size(max = 10)
    @Column(length = 10, nullable = true)
    public String cap;

    @Size(max = 50)
    @Column(length = 50, nullable = true)
    public String telefono;

    @Size(max = 100)
    @Column(length = 100, nullable = true)
    public String email;

    @Size(max = 100)
    @Column(length = 100, nullable = true)
    public String pec;

    @Size(max = 100)
    @Column(length = 100, nullable = true)
    public String referente;

    @Embedded
    public CommonEntity commonEntity;

    public static Cliente findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<Cliente> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(Cliente entity) {
        return find("nome", entity.nome).count() > 0;
    }
}