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
import io.quarkus.security.Authenticated;
import it.schema31.crm.cto.entities.Profilo;
import jakarta.annotation.security.RolesAllowed;
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

@Path("/api/profili")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Profili", description = "Gestione dei profili utente (gruppi di ruoli)")
@Authenticated
public class ProfiloResource {

    @GET
    @Operation(summary = "Elenca tutti i profili", description = "Recupera la lista di tutti i profili")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista profili recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Profilo.class)))
    })
    public List<Profilo> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Profilo> query = Profilo.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un profilo per ID", description = "Recupera i dettagli di un singolo profilo dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Profilo trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Profilo.class))),
        @APIResponse(responseCode = "404", description = "Profilo non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del profilo", required = true) Long id
    ) {
        Profilo profilo = Profilo.findById(id);
        if (profilo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(profilo).build();
    }

    @POST
    @Transactional
    @RolesAllowed("SUPER_USER")
    @Operation(summary = "Crea un nuovo profilo", description = "Crea un nuovo profilo nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Profilo creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Profilo.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Profilo già esistente")
    })
    public Response create(@Valid Profilo profilo) {
        if (Profilo.exists(profilo)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un profilo con questo nome esiste già")
                .build();
        }

        profilo.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        profilo.commonEntity.updatedAt = java.time.LocalDateTime.now();
        profilo.persist();

        return Response.status(Response.Status.CREATED).entity(profilo).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("SUPER_USER")
    @Operation(summary = "Aggiorna un profilo", description = "Aggiorna i dati di un profilo esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Profilo aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Profilo.class))),
        @APIResponse(responseCode = "404", description = "Profilo non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del profilo", required = true) Long id,
        @Valid Profilo profiloAggiornato
    ) {
        Profilo profilo = Profilo.findById(id);
        if (profilo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        profilo.nome = profiloAggiornato.nome;
        profilo.descrizione = profiloAggiornato.descrizione;
        profilo.ruoli = profiloAggiornato.ruoli;

        if (profilo.commonEntity == null) {
            profilo.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        profilo.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(profilo).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("SUPER_USER")
    @Operation(summary = "Elimina un profilo", description = "Elimina un profilo dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Profilo eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Profilo non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del profilo", required = true) Long id
    ) {
        Profilo profilo = Profilo.findById(id);
        if (profilo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        profilo.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i profili", description = "Restituisce il numero totale di profili nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Profilo.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
