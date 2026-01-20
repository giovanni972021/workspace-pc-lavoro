package it.schema31.crm.cto.resources;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import it.schema31.crm.cto.entities.LevelSoftSkill;
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

@Path("/api/level-soft-skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Level Soft Skills", description = "Gestione delle competenze trasversali con livello")
public class LevelSoftSkillResource {

    @GET
    @Operation(summary = "Elenca tutte le level soft skill", description = "Recupera la lista di tutte le soft skill con livello")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista level soft skill recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSoftSkill.class)))
    })
    public List<LevelSoftSkill> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<LevelSoftSkill> query = LevelSoftSkill.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera una level soft skill per ID", description = "Recupera i dettagli di una singola soft skill con livello dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Level soft skill trovata",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSoftSkill.class))),
        @APIResponse(responseCode = "404", description = "Level soft skill non trovata")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID della level soft skill", required = true) Long id
    ) {
        LevelSoftSkill levelSoftSkill = LevelSoftSkill.findById(id);
        if (levelSoftSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(levelSoftSkill).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea una nuova level soft skill", description = "Crea una nuova soft skill con livello nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Level soft skill creata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSoftSkill.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Level soft skill già esistente")
    })
    public Response create(@Valid LevelSoftSkill levelSoftSkill) {
        if (LevelSoftSkill.exists(levelSoftSkill)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Una soft skill con questa competenza e livello esiste già")
                .build();
        }

        levelSoftSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        levelSoftSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
        levelSoftSkill.persist();

        return Response.status(Response.Status.CREATED).entity(levelSoftSkill).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna una level soft skill", description = "Aggiorna i dati di una soft skill con livello esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Level soft skill aggiornata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSoftSkill.class))),
        @APIResponse(responseCode = "404", description = "Level soft skill non trovata")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID della level soft skill", required = true) Long id,
        @Valid LevelSoftSkill levelSoftSkillAggiornata
    ) {
        LevelSoftSkill levelSoftSkill = LevelSoftSkill.findById(id);
        if (levelSoftSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        levelSoftSkill.softSkill = levelSoftSkillAggiornata.softSkill;
        levelSoftSkill.livello = levelSoftSkillAggiornata.livello;

        if (levelSoftSkill.commonEntity == null) {
            levelSoftSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        levelSoftSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(levelSoftSkill).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina una level soft skill", description = "Elimina una soft skill con livello dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Level soft skill eliminata con successo"),
        @APIResponse(responseCode = "404", description = "Level soft skill non trovata")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID della level soft skill", required = true) Long id
    ) {
        LevelSoftSkill levelSoftSkill = LevelSoftSkill.findById(id);
        if (levelSoftSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        levelSoftSkill.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta le level soft skill", description = "Restituisce il numero totale di soft skill con livello nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = LevelSoftSkill.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
