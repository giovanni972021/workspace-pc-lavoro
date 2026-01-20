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
import it.schema31.crm.cto.entities.SoftSkill;
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

@Path("/api/soft-skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Soft Skills", description = "Gestione delle competenze trasversali")
public class SoftSkillResource {

    @GET
    @Operation(summary = "Elenca tutte le soft skill", description = "Recupera la lista di tutte le soft skill")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista soft skill recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SoftSkill.class)))
    })
    public List<SoftSkill> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<SoftSkill> query = SoftSkill.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera una soft skill per ID", description = "Recupera i dettagli di una singola soft skill dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Soft skill trovata",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SoftSkill.class))),
        @APIResponse(responseCode = "404", description = "Soft skill non trovata")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID della soft skill", required = true) Long id
    ) {
        SoftSkill softSkill = SoftSkill.findById(id);
        if (softSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(softSkill).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea una nuova soft skill", description = "Crea una nuova soft skill nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Soft skill creata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SoftSkill.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Soft skill già esistente")
    })
    public Response create(@Valid SoftSkill softSkill) {
        if (SoftSkill.exists(softSkill)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Una soft skill con questo nome esiste già")
                .build();
        }

        softSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        softSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();
        softSkill.persist();

        return Response.status(Response.Status.CREATED).entity(softSkill).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna una soft skill", description = "Aggiorna i dati di una soft skill esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Soft skill aggiornata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SoftSkill.class))),
        @APIResponse(responseCode = "404", description = "Soft skill non trovata")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID della soft skill", required = true) Long id,
        @Valid SoftSkill softSkillAggiornata
    ) {
        SoftSkill softSkill = SoftSkill.findById(id);
        if (softSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        softSkill.nome = softSkillAggiornata.nome;
        softSkill.descrizione = softSkillAggiornata.descrizione;

        if (softSkill.commonEntity == null) {
            softSkill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        softSkill.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(softSkill).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina una soft skill", description = "Elimina una soft skill dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Soft skill eliminata con successo"),
        @APIResponse(responseCode = "404", description = "Soft skill non trovata")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID della soft skill", required = true) Long id
    ) {
        SoftSkill softSkill = SoftSkill.findById(id);
        if (softSkill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        softSkill.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta le soft skill", description = "Restituisce il numero totale di soft skill nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = SoftSkill.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
