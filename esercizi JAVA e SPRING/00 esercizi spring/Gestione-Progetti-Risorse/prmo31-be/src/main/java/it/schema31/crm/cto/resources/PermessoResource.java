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
import it.schema31.crm.cto.entities.Permesso;
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

@Path("/api/permessi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Permessi", description = "Gestione dei permessi del sistema")
public class PermessoResource {

    @GET
    @Operation(summary = "Elenca tutti i permessi", description = "Recupera la lista di tutti i permessi")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista permessi recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Permesso.class)))
    })
    public List<Permesso> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Permesso> query = Permesso.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un permesso per ID", description = "Recupera i dettagli di un singolo permesso dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Permesso trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Permesso.class))),
        @APIResponse(responseCode = "404", description = "Permesso non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del permesso", required = true) Long id
    ) {
        Permesso permesso = Permesso.findById(id);
        if (permesso == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(permesso).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo permesso", description = "Crea un nuovo permesso nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Permesso creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Permesso.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Permesso già esistente")
    })
    public Response create(@Valid Permesso permesso) {
        if (Permesso.exists(permesso)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un permesso con questo nome esiste già")
                .build();
        }

        permesso.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        permesso.commonEntity.updatedAt = java.time.LocalDateTime.now();
        permesso.persist();

        return Response.status(Response.Status.CREATED).entity(permesso).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un permesso", description = "Aggiorna i dati di un permesso esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Permesso aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Permesso.class))),
        @APIResponse(responseCode = "404", description = "Permesso non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del permesso", required = true) Long id,
        @Valid Permesso permessoAggiornato
    ) {
        Permesso permesso = Permesso.findById(id);
        if (permesso == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        permesso.nome = permessoAggiornato.nome;
        permesso.descrizione = permessoAggiornato.descrizione;

        if (permesso.commonEntity == null) {
            permesso.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        permesso.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(permesso).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un permesso", description = "Elimina un permesso dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Permesso eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Permesso non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del permesso", required = true) Long id
    ) {
        Permesso permesso = Permesso.findById(id);
        if (permesso == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        permesso.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i permessi", description = "Restituisce il numero totale di permessi nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Permesso.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
