package it.schema31.crm.cto.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@Audited(targetAuditMode =RelationTargetAuditMode.NOT_AUDITED)
public class CostoRisorsa extends PanacheEntity {
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Utente utente;

    @Column(nullable = false, name = "costo_giornaliero")
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public double costoGiornaliero;

    @Column(nullable = false, name = "data_inizio")
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public LocalDateTime dataInizio;
    
    @Column(nullable = true, name = "data_fine")
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    public LocalDateTime dataFine;

    @Embedded
    public CommonEntity commonEntity;
}