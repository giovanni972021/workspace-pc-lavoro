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
import it.schema31.crm.cto.entities.TipoUtente;
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

@Path("/api/tipi-utente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Tipi Utente", description = "Gestione delle tipologie di utente (consulente, dipendente, time&material, etc)")
public class TipoUtenteResource {

    @GET
    @Operation(summary = "Elenca tutti i tipi di utente", description = "Recupera la lista di tutti i tipi di utente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista tipi utente recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TipoUtente.class)))
    })
    public List<TipoUtente> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<TipoUtente> query = TipoUtente.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un tipo utente per ID", description = "Recupera i dettagli di un singolo tipo utente dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Tipo utente trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TipoUtente.class))),
        @APIResponse(responseCode = "404", description = "Tipo utente non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del tipo utente", required = true) Long id
    ) {
        TipoUtente tipoUtente = TipoUtente.findById(id);
        if (tipoUtente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tipoUtente).build();
    }

    @GET
    @Path("/tipo/{tipo}")
    @Operation(summary = "Recupera un tipo utente per tipo", description = "Recupera i dettagli di un tipo utente dato il suo tipo")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Tipo utente trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TipoUtente.class))),
        @APIResponse(responseCode = "404", description = "Tipo utente non trovato")
    })
    public Response getByTipo(
        @PathParam("tipo") @Parameter(description = "Tipo dell'utente", required = true) String tipo
    ) {
        TipoUtente tipoUtente = TipoUtente.findByTipo(tipo);
        if (tipoUtente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tipoUtente).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo tipo utente", description = "Crea un nuovo tipo utente nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Tipo utente creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TipoUtente.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi")
    })
    public Response create(@Valid TipoUtente tipoUtente) {
        tipoUtente.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        tipoUtente.commonEntity.updatedAt = java.time.LocalDateTime.now();
        tipoUtente.persist();

        return Response.status(Response.Status.CREATED).entity(tipoUtente).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un tipo utente", description = "Aggiorna i dati di un tipo utente esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Tipo utente aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TipoUtente.class))),
        @APIResponse(responseCode = "404", description = "Tipo utente non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del tipo utente", required = true) Long id,
        @Valid TipoUtente tipoUtenteAggiornato
    ) {
        TipoUtente tipoUtente = TipoUtente.findById(id);
        if (tipoUtente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        tipoUtente.tipo = tipoUtenteAggiornato.tipo;
        tipoUtente.descrizione = tipoUtenteAggiornato.descrizione;

        if (tipoUtente.commonEntity == null) {
            tipoUtente.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        tipoUtente.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(tipoUtente).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un tipo utente", description = "Elimina un tipo utente dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Tipo utente eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Tipo utente non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del tipo utente", required = true) Long id
    ) {
        TipoUtente tipoUtente = TipoUtente.findById(id);
        if (tipoUtente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        tipoUtente.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i tipi utente", description = "Restituisce il numero totale di tipi utente nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = TipoUtente.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
