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
import it.schema31.crm.cto.entities.Incarico;
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

@Path("/api/incarichi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Incarichi", description = "Gestione degli incarichi professionali")
public class IncaricoResource {

    @GET
    @Operation(summary = "Elenca tutti gli incarichi", description = "Recupera la lista di tutti gli incarichi")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista incarichi recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Incarico.class)))
    })
    public List<Incarico> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Incarico> query = Incarico.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un incarico per ID", description = "Recupera i dettagli di un singolo incarico dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Incarico trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Incarico.class))),
        @APIResponse(responseCode = "404", description = "Incarico non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID dell'incarico", required = true) Long id
    ) {
        Incarico incarico = Incarico.findById(id);
        if (incarico == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(incarico).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo incarico", description = "Crea un nuovo incarico nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Incarico creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Incarico.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Incarico già esistente")
    })
    public Response create(@Valid Incarico incarico) {
        if (Incarico.exists(incarico)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un incarico con questo nome esiste già")
                .build();
        }

        incarico.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        incarico.commonEntity.updatedAt = java.time.LocalDateTime.now();
        incarico.persist();

        return Response.status(Response.Status.CREATED).entity(incarico).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un incarico", description = "Aggiorna i dati di un incarico esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Incarico aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Incarico.class))),
        @APIResponse(responseCode = "404", description = "Incarico non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID dell'incarico", required = true) Long id,
        @Valid Incarico incaricoAggiornato
    ) {
        Incarico incarico = Incarico.findById(id);
        if (incarico == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        incarico.nome = incaricoAggiornato.nome;
        incarico.descrizione = incaricoAggiornato.descrizione;

        if (incarico.commonEntity == null) {
            incarico.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        incarico.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(incarico).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un incarico", description = "Elimina un incarico dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Incarico eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Incarico non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID dell'incarico", required = true) Long id
    ) {
        Incarico incarico = Incarico.findById(id);
        if (incarico == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        incarico.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta gli incarichi", description = "Restituisce il numero totale di incarichi nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Incarico.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
