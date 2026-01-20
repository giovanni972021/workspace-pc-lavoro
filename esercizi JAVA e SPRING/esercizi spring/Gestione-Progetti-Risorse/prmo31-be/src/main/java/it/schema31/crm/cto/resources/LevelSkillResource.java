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
import it.schema31.crm.cto.entities.LevelSkill;
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

@Path("/api/level-skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Level Skills", description = "Gestione delle competenze tecniche con livello")
public class LevelSkillResource {

    @GET
    @Operation(summary = "Elenca tutte le level skill", description = "Recupera la lista di tutte le competenze con livello")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista level skill recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSkill.class)))
    })
    public List<LevelSkill> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<LevelSkill> query = LevelSkill.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera una level skill per ID", description = "Recupera i dettagli di una singola competenza con livello dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Level skill trovata",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSkill.class))),
        @APIResponse(responseCode = "404", description = "Level skill non trovata")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID della level skill", required = true) Long id
    ) {
        LevelSkill levelSkill = LevelSkill.findById(id);
        if (levelSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(levelSkill).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea una nuova level skill", description = "Crea una nuova competenza con livello nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Level skill creata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSkill.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Level skill già esistente")
    })
    public Response create(@Valid LevelSkill levelSkill) {
        if (LevelSkill.exists(levelSkill)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Una competenza con questa skill e livello esiste già")
                .build();
        }

        levelSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        levelSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
        levelSkill.persist();

        return Response.status(Response.Status.CREATED).entity(levelSkill).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna una level skill", description = "Aggiorna i dati di una competenza con livello esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Level skill aggiornata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LevelSkill.class))),
        @APIResponse(responseCode = "404", description = "Level skill non trovata")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID della level skill", required = true) Long id,
        @Valid LevelSkill levelSkillAggiornata
    ) {
        LevelSkill levelSkill = LevelSkill.findById(id);
        if (levelSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        levelSkill.skill = levelSkillAggiornata.skill;
        levelSkill.livello = levelSkillAggiornata.livello;

        if (levelSkill.commonEntity == null) {
            levelSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        levelSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(levelSkill).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina una level skill", description = "Elimina una competenza con livello dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Level skill eliminata con successo"),
        @APIResponse(responseCode = "404", description = "Level skill non trovata")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID della level skill", required = true) Long id
    ) {
        LevelSkill levelSkill = LevelSkill.findById(id);
        if (levelSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        levelSkill.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta le level skill", description = "Restituisce il numero totale di competenze con livello nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = LevelSkill.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
