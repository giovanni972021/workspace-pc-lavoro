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
import it.schema31.crm.cto.entities.Contatto;
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

@Path("/api/contatti")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Contatti", description = "Gestione dei contatti")
public class ContattoResource {

    @GET
    @Operation(summary = "Elenca tutti i contatti", description = "Recupera la lista di tutti i contatti")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista contatti recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Contatto.class)))
    })
    public List<Contatto> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Contatto> query = Contatto.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un contatto per ID", description = "Recupera i dettagli di un singolo contatto dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Contatto trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Contatto.class))),
        @APIResponse(responseCode = "404", description = "Contatto non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del contatto", required = true) Long id
    ) {
        Contatto contatto = Contatto.findById(id);
        if (contatto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(contatto).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo contatto", description = "Crea un nuovo contatto nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Contatto creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Contatto.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Contatto già esistente")
    })
    public Response create(@Valid Contatto contatto) {
        if (Contatto.exists(contatto)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un contatto con questo riferimento esiste già")
                .build();
        }

        contatto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        contatto.commonEntity.updatedAt = java.time.LocalDateTime.now();
        contatto.persist();

        return Response.status(Response.Status.CREATED).entity(contatto).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un contatto", description = "Aggiorna i dati di un contatto esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Contatto aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Contatto.class))),
        @APIResponse(responseCode = "404", description = "Contatto non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del contatto", required = true) Long id,
        @Valid Contatto contattoAggiornato
    ) {
        Contatto contatto = Contatto.findById(id);
        if (contatto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        contatto.riferimento = contattoAggiornato.riferimento;
        contatto.valore = contattoAggiornato.valore;

        if (contatto.commonEntity == null) {
            contatto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        contatto.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(contatto).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un contatto", description = "Elimina un contatto dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Contatto eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Contatto non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del contatto", required = true) Long id
    ) {
        Contatto contatto = Contatto.findById(id);
        if (contatto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        contatto.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i contatti", description = "Restituisce il numero totale di contatti nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Contatto.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
