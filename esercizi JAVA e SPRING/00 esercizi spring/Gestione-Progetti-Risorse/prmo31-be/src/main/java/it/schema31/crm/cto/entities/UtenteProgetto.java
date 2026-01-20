package it.schema31.crm.cto.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@NamedQuery(
    name = "UtenteProgetto.findByDateRange",
    query = "SELECT up FROM UtenteProgetto up WHERE " +
            "(up.dataInizio <= :dataFine AND up.dataFine >= :dataInizio) " +
            "ORDER BY up.utente.cognome, up.utente.nome, up.dataInizio"
)
@NamedQuery(
    name = "UtenteProgetto.findByUtenteAndDateRange",
    query = "SELECT up FROM UtenteProgetto up WHERE up.utente.id = :utenteId AND " +
            "(up.dataInizio <= :dataFine AND up.dataFine >= :dataInizio) " +
            "ORDER BY up.dataInizio"
)
@NamedQuery(
    name = "UtenteProgetto.findActiveByUtente",
    query = "SELECT up FROM UtenteProgetto up WHERE up.utente.id = :utenteId AND " +
            "up.dataFine >= :today ORDER BY up.dataInizio"
)
public class UtenteProgetto extends PanacheEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty
    public Utente utente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty
    public Progetto progetto;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(nullable = false)
    public Integer percentualeImpegno;

    @NotNull
    @Column(nullable = false)
    public LocalDate dataInizio;

    @NotNull
    @Column(nullable = false)
    public LocalDate dataFine;

    @Column(length = 1000)
    public String note;

    @Embedded
    public CommonEntity commonEntity;

    public static UtenteProgetto findById(Long id) {
        return find("id", id).firstResult();
    }

    public static Optional<UtenteProgetto> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public static boolean exists(UtenteProgetto entity) {
        return find("utente.id = ?1 AND progetto.id = ?2 AND dataInizio = ?3",
                   entity.utente.id, entity.progetto.id, entity.dataInizio).count() > 0;
    }

    public static boolean exists(UtenteProgetto entity, Long id) {
        return find("utente.id = ?1 AND progetto.id = ?2 AND dataInizio = ?3 AND id <> ?4",
                   entity.utente.id, entity.progetto.id, entity.dataInizio, id).count() > 0;
    }

    public static List<UtenteProgetto> findByDateRange(LocalDate dataInizio, LocalDate dataFine) {
        return find("#UtenteProgetto.findByDateRange",
                   io.quarkus.panache.common.Parameters
                       .with("dataInizio", dataInizio)
                       .and("dataFine", dataFine))
               .list();
    }

    public static List<UtenteProgetto> findByUtenteAndDateRange(Long utenteId, LocalDate dataInizio, LocalDate dataFine) {
        return find("#UtenteProgetto.findByUtenteAndDateRange",
                   io.quarkus.panache.common.Parameters
                       .with("utenteId", utenteId)
                       .and("dataInizio", dataInizio)
                       .and("dataFine", dataFine))
               .list();
    }

    public static List<UtenteProgetto> findActiveByUtente(Long utenteId) {
        return find("#UtenteProgetto.findActiveByUtente",
                   io.quarkus.panache.common.Parameters
                       .with("utenteId", utenteId)
                       .and("today", LocalDate.now()))
               .list();
    }

    /**
     * Check if there is any overlap for the given user-project combination in the specified date range
     * @param utenteId ID of the user
     * @param progettoId ID of the project
     * @param dataInizio Start date of the allocation
     * @param dataFine End date of the allocation
     * @param excludeId Optional ID to exclude from the check (for updates)
     * @return true if there is an overlap, false otherwise
     */
    public static boolean hasDateOverlap(Long utenteId, Long progettoId, LocalDate dataInizio, LocalDate dataFine, Long excludeId) {
        String query = "SELECT COUNT(up) FROM UtenteProgetto up WHERE " +
                      "up.utente.id = :utenteId AND up.progetto.id = :progettoId AND " +
                      "(up.dataInizio <= :dataFine AND up.dataFine >= :dataInizio)";

        if (excludeId != null) {
            query += " AND up.id <> :excludeId";
        }

        var params = io.quarkus.panache.common.Parameters
            .with("utenteId", utenteId)
            .and("progettoId", progettoId)
            .and("dataInizio", dataInizio)
            .and("dataFine", dataFine);

        if (excludeId != null) {
            params = params.and("excludeId", excludeId);
        }

        long count = find(query, params).count();
        return count > 0;
    }

    /**
     * Check if there is any overlap for the given user-project combination (for new records)
     */
    public static boolean hasDateOverlap(Long utenteId, Long progettoId, LocalDate dataInizio, LocalDate dataFine) {
        return hasDateOverlap(utenteId, progettoId, dataInizio, dataFine, null);
    }
}