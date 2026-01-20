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
import it.schema31.crm.cto.entities.Esperienza;
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

@Path("/api/esperienze")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Esperienze", description = "Gestione delle esperienze lavorative")
public class EsperienzaResource {

    @GET
    @Operation(summary = "Elenca tutte le esperienze", description = "Recupera la lista di tutte le esperienze lavorative")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista esperienze recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Esperienza.class)))
    })
    public List<Esperienza> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Esperienza> query = Esperienza.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un'esperienza per ID", description = "Recupera i dettagli di una singola esperienza dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Esperienza trovata",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Esperienza.class))),
        @APIResponse(responseCode = "404", description = "Esperienza non trovata")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID dell'esperienza", required = true) Long id
    ) {
        Esperienza esperienza = Esperienza.findById(id);
        if (esperienza == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(esperienza).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea una nuova esperienza", description = "Crea una nuova esperienza lavorativa nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Esperienza creata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Esperienza.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Esperienza già esistente")
    })
    public Response create(@Valid Esperienza esperienza) {
        if (Esperienza.exists(esperienza)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un'esperienza presso questa azienda esiste già")
                .build();
        }

        esperienza.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        esperienza.commonEntity.updatedAt = java.time.LocalDateTime.now();
        esperienza.persist();

        return Response.status(Response.Status.CREATED).entity(esperienza).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un'esperienza", description = "Aggiorna i dati di un'esperienza esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Esperienza aggiornata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Esperienza.class))),
        @APIResponse(responseCode = "404", description = "Esperienza non trovata")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID dell'esperienza", required = true) Long id,
        @Valid Esperienza esperienzaAggiornata
    ) {
        Esperienza esperienza = Esperienza.findById(id);
        if (esperienza == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        esperienza.azienda = esperienzaAggiornata.azienda;
        esperienza.ruolo = esperienzaAggiornata.ruolo;
        esperienza.dataInizio = esperienzaAggiornata.dataInizio;
        esperienza.dataFine = esperienzaAggiornata.dataFine;
        esperienza.descrizione = esperienzaAggiornata.descrizione;
        esperienza.tecnologie = esperienzaAggiornata.tecnologie;

        if (esperienza.commonEntity == null) {
            esperienza.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        esperienza.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(esperienza).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un'esperienza", description = "Elimina un'esperienza dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Esperienza eliminata con successo"),
        @APIResponse(responseCode = "404", description = "Esperienza non trovata")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID dell'esperienza", required = true) Long id
    ) {
        Esperienza esperienza = Esperienza.findById(id);
        if (esperienza == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        esperienza.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta le esperienze", description = "Restituisce il numero totale di esperienze nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Esperienza.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
