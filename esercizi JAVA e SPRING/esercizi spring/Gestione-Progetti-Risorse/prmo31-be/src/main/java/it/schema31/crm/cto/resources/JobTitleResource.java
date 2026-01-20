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
import it.schema31.crm.cto.entities.JobTitle;
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

@Path("/api/job-titles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Job Titles", description = "Gestione delle qualifiche lavorative")
public class JobTitleResource {

    @GET
    @Operation(summary = "Elenca tutti i job title", description = "Recupera la lista di tutti i job title")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista job title recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JobTitle.class)))
    })
    public List<JobTitle> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<JobTitle> query = JobTitle.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un job title per ID", description = "Recupera i dettagli di un singolo job title dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Job title trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JobTitle.class))),
        @APIResponse(responseCode = "404", description = "Job title non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del job title", required = true) Long id
    ) {
        JobTitle jobTitle = JobTitle.findById(id);
        if (jobTitle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(jobTitle).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo job title", description = "Crea un nuovo job title nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Job title creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JobTitle.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Job title già esistente")
    })
    public Response create(@Valid JobTitle jobTitle) {
        if (JobTitle.exists(jobTitle)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un job title con questo nome esiste già")
                .build();
        }

        jobTitle.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        jobTitle.commonEntity.updatedAt = java.time.LocalDateTime.now();
        jobTitle.persist();

        return Response.status(Response.Status.CREATED).entity(jobTitle).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un job title", description = "Aggiorna i dati di un job title esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Job title aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = JobTitle.class))),
        @APIResponse(responseCode = "404", description = "Job title non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del job title", required = true) Long id,
        @Valid JobTitle jobTitleAggiornato
    ) {
        JobTitle jobTitle = JobTitle.findById(id);
        if (jobTitle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        jobTitle.nome = jobTitleAggiornato.nome;
        jobTitle.descrizione = jobTitleAggiornato.descrizione;

        if (jobTitle.commonEntity == null) {
            jobTitle.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        jobTitle.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(jobTitle).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un job title", description = "Elimina un job title dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Job title eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Job title non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del job title", required = true) Long id
    ) {
        JobTitle jobTitle = JobTitle.findById(id);
        if (jobTitle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        jobTitle.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i job title", description = "Restituisce il numero totale di job title nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = JobTitle.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
