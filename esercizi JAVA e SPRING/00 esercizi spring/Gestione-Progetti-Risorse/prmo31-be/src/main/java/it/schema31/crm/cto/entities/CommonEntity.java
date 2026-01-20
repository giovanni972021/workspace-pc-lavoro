package it.schema31.crm.cto.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class CommonEntity {

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Utente updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public java.time.LocalDateTime updatedAt;

    // Constructors
    public CommonEntity() {
    }

    public CommonEntity(Utente updatedBy, java.time.LocalDateTime updatedAt) {
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
