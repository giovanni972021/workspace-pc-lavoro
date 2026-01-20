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
import it.schema31.crm.cto.entities.StatoProgetto;
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

@Path("/api/stati-progetto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Stati Progetto", description = "Gestione degli stati dei progetti")
public class StatoProgettoResource {

    @GET
    @Operation(summary = "Elenca tutti gli stati progetto", description = "Recupera la lista di tutti gli stati progetto")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista stati progetto recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatoProgetto.class)))
    })
    public List<StatoProgetto> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<StatoProgetto> query = StatoProgetto.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera uno stato progetto per ID", description = "Recupera i dettagli di un singolo stato progetto dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Stato progetto trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatoProgetto.class))),
        @APIResponse(responseCode = "404", description = "Stato progetto non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID dello stato progetto", required = true) Long id
    ) {
        StatoProgetto statoProgetto = StatoProgetto.findById(id);
        if (statoProgetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(statoProgetto).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo stato progetto", description = "Crea un nuovo stato progetto nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Stato progetto creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatoProgetto.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Stato progetto già esistente")
    })
    public Response create(@Valid StatoProgetto statoProgetto) {
        if (StatoProgetto.exists(statoProgetto)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Uno stato progetto con questo nome esiste già")
                .build();
        }

        statoProgetto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        statoProgetto.commonEntity.updatedAt = java.time.LocalDateTime.now();
        statoProgetto.persist();

        return Response.status(Response.Status.CREATED).entity(statoProgetto).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna uno stato progetto", description = "Aggiorna i dati di uno stato progetto esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Stato progetto aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatoProgetto.class))),
        @APIResponse(responseCode = "404", description = "Stato progetto non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID dello stato progetto", required = true) Long id,
        @Valid StatoProgetto statoProgettoAggiornato
    ) {
        StatoProgetto statoProgetto = StatoProgetto.findById(id);
        if (statoProgetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        statoProgetto.nome = statoProgettoAggiornato.nome;
        statoProgetto.descrizione = statoProgettoAggiornato.descrizione;

        if (statoProgetto.commonEntity == null) {
            statoProgetto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        statoProgetto.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(statoProgetto).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina uno stato progetto", description = "Elimina uno stato progetto dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Stato progetto eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Stato progetto non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID dello stato progetto", required = true) Long id
    ) {
        StatoProgetto statoProgetto = StatoProgetto.findById(id);
        if (statoProgetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        statoProgetto.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta gli stati progetto", description = "Restituisce il numero totale di stati progetto nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = StatoProgetto.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
