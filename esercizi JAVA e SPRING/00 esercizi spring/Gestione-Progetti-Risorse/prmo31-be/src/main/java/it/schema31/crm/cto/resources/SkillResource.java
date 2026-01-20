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
import it.schema31.crm.cto.entities.Skill;
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

@Path("/api/skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Skills", description = "Gestione delle competenze tecniche")
public class SkillResource {

    @GET
    @Operation(summary = "Elenca tutte le skill", description = "Recupera la lista di tutte le skill")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista skill recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Skill.class)))
    })
    public List<Skill> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Skill> query = Skill.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera una skill per ID", description = "Recupera i dettagli di una singola skill dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Skill trovata",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Skill.class))),
        @APIResponse(responseCode = "404", description = "Skill non trovata")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID della skill", required = true) Long id
    ) {
        Skill skill = Skill.findById(id);
        if (skill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(skill).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea una nuova skill", description = "Crea una nuova skill nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Skill creata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Skill.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Skill già esistente")
    })
    public Response create(@Valid Skill skill) {
        if (Skill.exists(skill)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Una skill con questo nome esiste già")
                .build();
        }

        skill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        skill.commonEntity.updatedAt = java.time.LocalDateTime.now();
        skill.persist();

        return Response.status(Response.Status.CREATED).entity(skill).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna una skill", description = "Aggiorna i dati di una skill esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Skill aggiornata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Skill.class))),
        @APIResponse(responseCode = "404", description = "Skill non trovata")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID della skill", required = true) Long id,
        @Valid Skill skillAggiornata
    ) {
        Skill skill = Skill.findById(id);
        if (skill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        skill.nome = skillAggiornata.nome;
        skill.descrizione = skillAggiornata.descrizione;

        if (skill.commonEntity == null) {
            skill.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        skill.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(skill).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina una skill", description = "Elimina una skill dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Skill eliminata con successo"),
        @APIResponse(responseCode = "404", description = "Skill non trovata")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID della skill", required = true) Long id
    ) {
        Skill skill = Skill.findById(id);
        if (skill == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        skill.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta le skill", description = "Restituisce il numero totale di skill nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Skill.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
