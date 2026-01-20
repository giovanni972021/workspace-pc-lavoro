package it.schema31.crm.cto.resources;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import org.hibernate.Hibernate;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import it.schema31.crm.cto.entities.Curriculum;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/curricula")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Curricula", description = "Gestione dei curricula vitae")
public class CurriculumResource {

    private static final Logger LOG = Logger.getLogger(CurriculumResource.class);

    @GET
    @Operation(summary = "Elenca tutti i curricula", description = "Recupera la lista di tutti i curricula")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista curricula recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Curriculum.class)))
    })
    public List<Curriculum> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Curriculum> query = Curriculum.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un curriculum per ID", description = "Recupera i dettagli di un singolo curriculum dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Curriculum trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Curriculum.class))),
        @APIResponse(responseCode = "404", description = "Curriculum non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del curriculum", required = true) Long id
    ) {
        Curriculum curriculum = Curriculum.findById(id);
        if (curriculum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(curriculum).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo curriculum", description = "Crea un nuovo curriculum nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Curriculum creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Curriculum.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi")
    })
    public Response create(@Valid Curriculum curriculum) {
        // Persist LevelSkill entities and their related Skill entities that don't have an ID
        if (curriculum.skills != null) {
            curriculum.skills.forEach(levelSkill -> {
                // If the Skill doesn't have an ID, it's a new skill - persist it first
                if (levelSkill.skill != null && levelSkill.skill.id == null) {
                    levelSkill.skill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                    levelSkill.skill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                    levelSkill.skill.persist();
                }
                // Then persist the LevelSkill if it doesn't have an ID
                if (levelSkill.id == null) {
                    levelSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                    levelSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                    levelSkill.persist();
                }
            });
        }

        // Persist LevelSoftSkill entities and their related SoftSkill entities that don't have an ID
        if (curriculum.softSkills != null) {
            curriculum.softSkills.forEach(levelSoftSkill -> {
                // If the SoftSkill doesn't have an ID, it's a new soft skill - persist it first
                if (levelSoftSkill.softSkill != null && levelSoftSkill.softSkill.id == null) {
                    levelSoftSkill.softSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                    levelSoftSkill.softSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                    levelSoftSkill.softSkill.persist();
                }
                // Then persist the LevelSoftSkill if it doesn't have an ID
                if (levelSoftSkill.id == null) {
                    levelSoftSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                    levelSoftSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                    levelSoftSkill.persist();
                }
            });
        }

        // Persist Certificazione entities that don't have an ID
        if (curriculum.certificazioni != null) {
            curriculum.certificazioni.forEach(certificazione -> {
                if (certificazione.id == null) {
                    certificazione.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                    certificazione.commonEntity.updatedAt = java.time.LocalDateTime.now();
                    certificazione.persist();
                }
            });
        }

        // Persist Esperienza entities that don't have an ID
        if (curriculum.esperienze != null) {
            curriculum.esperienze.forEach(esperienza -> {
                if (esperienza.id == null) {
                    esperienza.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                    esperienza.commonEntity.updatedAt = java.time.LocalDateTime.now();
                    esperienza.persist();
                }
            });
        }

        curriculum.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        curriculum.commonEntity.updatedAt = java.time.LocalDateTime.now();
        curriculum.persist();

        return Response.status(Response.Status.CREATED).entity(curriculum).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un curriculum", description = "Aggiorna i dati di un curriculum esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Curriculum aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Curriculum.class))),
        @APIResponse(responseCode = "404", description = "Curriculum non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del curriculum", required = true) Long id,
        Curriculum curriculumAggiornato
    ) {
        try {
            LOG.infof("Updating curriculum with id: %d", id);

            Curriculum curriculum = Curriculum.findById(id);
            if (curriculum == null) {
                LOG.warnf("Curriculum with id %d not found", id);
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            // Process utente from payload - load from DB to avoid detached entity issues
            if (curriculumAggiornato.utente != null && curriculumAggiornato.utente.id != null) {
                var newUtente = it.schema31.crm.cto.entities.Utente.findById(curriculumAggiornato.utente.id);
                if (newUtente != null) {
                    // Unlink old utente if different
                    if (curriculum.utente != null && !curriculum.utente.id.equals(newUtente.id)) {
                        curriculum.utente.curriculum = null;
                    }
                    // Link new utente
                    curriculum.utente = newUtente;
                    newUtente.curriculum = curriculum;
                }
            }

            // Process LevelSkill entities - load existing ones from DB or persist new ones
            if (curriculumAggiornato.skills != null) {
                for (int i = 0; i < curriculumAggiornato.skills.size(); i++) {
                    var levelSkill = curriculumAggiornato.skills.get(i);

                    // If LevelSkill has an ID, load it from DB
                    if (levelSkill.id != null) {
                        var existingLevelSkill = it.schema31.crm.cto.entities.LevelSkill.findById(levelSkill.id);
                        if (existingLevelSkill != null) {
                            existingLevelSkill.livello = levelSkill.livello;
                            curriculumAggiornato.skills.set(i, existingLevelSkill);
                        }
                    } else {
                        // New LevelSkill - check if Skill exists or create it
                        if (levelSkill.skill != null && levelSkill.skill.id != null) {
                            levelSkill.skill = it.schema31.crm.cto.entities.Skill.findById(levelSkill.skill.id);
                        } else if (levelSkill.skill != null && levelSkill.skill.id == null) {
                            levelSkill.skill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                            levelSkill.skill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                            levelSkill.skill.persist();
                        }
                        levelSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                        levelSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                        levelSkill.persist();
                    }
                }
            }

            // Process LevelSoftSkill entities - load existing ones from DB or persist new ones
            if (curriculumAggiornato.softSkills != null) {
                for (int i = 0; i < curriculumAggiornato.softSkills.size(); i++) {
                    var levelSoftSkill = curriculumAggiornato.softSkills.get(i);

                    // If LevelSoftSkill has an ID, load it from DB
                    if (levelSoftSkill.id != null) {
                        var existingLevelSoftSkill = it.schema31.crm.cto.entities.LevelSoftSkill.findById(levelSoftSkill.id);
                        if (existingLevelSoftSkill != null) {
                            existingLevelSoftSkill.livello = levelSoftSkill.livello;
                            curriculumAggiornato.softSkills.set(i, existingLevelSoftSkill);
                        }
                    } else {
                        // New LevelSoftSkill - check if SoftSkill exists or create it
                        if (levelSoftSkill.softSkill != null && levelSoftSkill.softSkill.id != null) {
                            levelSoftSkill.softSkill = it.schema31.crm.cto.entities.SoftSkill.findById(levelSoftSkill.softSkill.id);
                        } else if (levelSoftSkill.softSkill != null && levelSoftSkill.softSkill.id == null) {
                            levelSoftSkill.softSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                            levelSoftSkill.softSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                            levelSoftSkill.softSkill.persist();
                        }
                        levelSoftSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                        levelSoftSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
                        levelSoftSkill.persist();
                    }
                }
            }

            // Process Certificazione entities - load existing ones from DB or persist new ones
            if (curriculumAggiornato.certificazioni != null) {
                for (int i = 0; i < curriculumAggiornato.certificazioni.size(); i++) {
                    var certificazione = curriculumAggiornato.certificazioni.get(i);
                    if (certificazione.id != null) {
                        var existingCert = it.schema31.crm.cto.entities.Certificazione.findById(certificazione.id);
                        if (existingCert != null) {
                            curriculumAggiornato.certificazioni.set(i, existingCert);
                        }
                    } else {
                        certificazione.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                        certificazione.commonEntity.updatedAt = java.time.LocalDateTime.now();
                        certificazione.persist();
                    }
                }
            }

            // Process Esperienza entities - load existing ones from DB or persist new ones
            if (curriculumAggiornato.esperienze != null) {
                LOG.infof("Processing %d esperienze", curriculumAggiornato.esperienze.size());
                for (int i = 0; i < curriculumAggiornato.esperienze.size(); i++) {
                    var esperienza = curriculumAggiornato.esperienze.get(i);
                    LOG.infof("Processing esperienza %d: id=%s, azienda=%s", i, esperienza.id, esperienza.azienda);
                    if (esperienza.id != null) {
                        var existingExp = it.schema31.crm.cto.entities.Esperienza.findById(esperienza.id);
                        if (existingExp != null) {
                            // Update existing esperienza
                            existingExp.azienda = esperienza.azienda;
                            existingExp.ruolo = esperienza.ruolo;
                            existingExp.dataInizio = esperienza.dataInizio;
                            existingExp.dataFine = esperienza.dataFine;
                            existingExp.descrizione = esperienza.descrizione;
                            existingExp.tecnologie = esperienza.tecnologie;
                            curriculumAggiornato.esperienze.set(i, existingExp);
                        }
                    } else {
                        // New esperienza - persist it
                        if (esperienza.commonEntity == null) {
                            esperienza.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                        }
                        esperienza.commonEntity.updatedAt = java.time.LocalDateTime.now();
                        esperienza.persist();
                        LOG.infof("Persisted new esperienza: %s with id %d", esperienza.azienda, esperienza.id);
                    }
                }
            }

            // Process Incarico entities - load existing ones from DB or persist new ones
            if (curriculumAggiornato.incarichi != null) {
                for (int i = 0; i < curriculumAggiornato.incarichi.size(); i++) {
                    var incarico = curriculumAggiornato.incarichi.get(i);
                    if (incarico.id != null) {
                        var existingInc = it.schema31.crm.cto.entities.Incarico.findById(incarico.id);
                        if (existingInc != null) {
                            curriculumAggiornato.incarichi.set(i, existingInc);
                        }
                    } else {
                        incarico.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                        incarico.commonEntity.updatedAt = java.time.LocalDateTime.now();
                        incarico.persist();
                    }
                }
            }

            curriculum.dataNascita = curriculumAggiornato.dataNascita;

            // Update collections by clearing and re-adding
            if (curriculum.certificazioni != null) {
                curriculum.certificazioni.clear();
            }
            if (curriculumAggiornato.certificazioni != null) {
                if (curriculum.certificazioni == null) {
                    curriculum.certificazioni = new java.util.ArrayList<>();
                }
                curriculum.certificazioni.addAll(curriculumAggiornato.certificazioni);
            }

            if (curriculum.skills != null) {
                curriculum.skills.clear();
            }
            if (curriculumAggiornato.skills != null) {
                if (curriculum.skills == null) {
                    curriculum.skills = new java.util.ArrayList<>();
                }
                curriculum.skills.addAll(curriculumAggiornato.skills);
            }

            if (curriculum.softSkills != null) {
                curriculum.softSkills.clear();
            }
            if (curriculumAggiornato.softSkills != null) {
                if (curriculum.softSkills == null) {
                    curriculum.softSkills = new java.util.ArrayList<>();
                }
                curriculum.softSkills.addAll(curriculumAggiornato.softSkills);
            }

            if (curriculum.incarichi != null) {
                curriculum.incarichi.clear();
            }
            if (curriculumAggiornato.incarichi != null) {
                if (curriculum.incarichi == null) {
                    curriculum.incarichi = new java.util.ArrayList<>();
                }
                curriculum.incarichi.addAll(curriculumAggiornato.incarichi);
            }

            if (curriculum.esperienze != null) {
                curriculum.esperienze.clear();
            }
            if (curriculumAggiornato.esperienze != null) {
                if (curriculum.esperienze == null) {
                    curriculum.esperienze = new java.util.ArrayList<>();
                }
                curriculum.esperienze.addAll(curriculumAggiornato.esperienze);
            }

            curriculum.pathRelativoCv = curriculumAggiornato.pathRelativoCv;

            if (curriculum.commonEntity == null) {
                curriculum.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
            }
            curriculum.commonEntity.updatedAt = java.time.LocalDateTime.now();

            // Initialize lazy properties of utente to avoid LazyInitializationException during serialization
            if (curriculum.utente != null) {
                Hibernate.initialize(curriculum.utente.tipoUtente);
                Hibernate.initialize(curriculum.utente.jobTitle);
                Hibernate.initialize(curriculum.utente.profilo);
                if (curriculum.utente.profilo != null) {
                    Hibernate.initialize(curriculum.utente.profilo.ruoli);
                    if (curriculum.utente.profilo.ruoli != null) {
                        for (var ruolo : curriculum.utente.profilo.ruoli) {
                            Hibernate.initialize(ruolo.permessi);
                        }
                    }
                }
                Hibernate.initialize(curriculum.utente.contatti);
            }

            LOG.infof("Curriculum %d updated successfully", id);
            return Response.ok(curriculum).build();
        } catch (Exception e) {
            LOG.error("Error updating curriculum with id: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un curriculum", description = "Elimina un curriculum dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Curriculum eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Curriculum non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del curriculum", required = true) Long id
    ) {
        Curriculum curriculum = Curriculum.findById(id);
        if (curriculum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        curriculum.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i curricula", description = "Restituisce il numero totale di curricula nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Curriculum.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
