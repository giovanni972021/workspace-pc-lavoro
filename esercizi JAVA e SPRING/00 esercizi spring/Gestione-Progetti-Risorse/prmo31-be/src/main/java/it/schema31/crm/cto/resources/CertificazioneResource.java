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
import it.schema31.crm.cto.entities.Certificazione;
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

@Path("/api/certificazioni")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Certificazioni", description = "Gestione delle certificazioni professionali")
public class CertificazioneResource {

    @GET
    @Operation(summary = "Elenca tutte le certificazioni", description = "Recupera la lista di tutte le certificazioni")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista certificazioni recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Certificazione.class)))
    })
    public List<Certificazione> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Certificazione> query = Certificazione.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Cerca certificazioni per nome", description = "Cerca certificazioni il cui nome contiene la stringa specificata")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Risultati della ricerca",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Certificazione.class)))
    })
    public List<Certificazione> searchByName(
        @QueryParam("q") @Parameter(description = "Nome da cercare", required = true) String name
    ) {
        return Certificazione.list("nome like ?1", "%" + name + "%");
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera una certificazione per ID", description = "Recupera i dettagli di una singola certificazione dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Certificazione trovata",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Certificazione.class))),
        @APIResponse(responseCode = "404", description = "Certificazione non trovata")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID della certificazione", required = true) Long id
    ) {
        Certificazione certificazione = Certificazione.findById(id);
        if (certificazione == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(certificazione).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea una nuova certificazione", description = "Crea una nuova certificazione nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Certificazione creata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Certificazione.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Certificazione già esistente")
    })
    public Response create(@Valid Certificazione certificazione) {
        if (Certificazione.exists(certificazione)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Una certificazione con questo nome esiste già")
                .build();
        }

        certificazione.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        certificazione.commonEntity.updatedAt = java.time.LocalDateTime.now();
        certificazione.persist();

        return Response.status(Response.Status.CREATED).entity(certificazione).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna una certificazione", description = "Aggiorna i dati di una certificazione esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Certificazione aggiornata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Certificazione.class))),
        @APIResponse(responseCode = "404", description = "Certificazione non trovata")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID della certificazione", required = true) Long id,
        @Valid Certificazione certificazioneAggiornata
    ) {
        Certificazione certificazione = Certificazione.findById(id);
        if (certificazione == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        certificazione.nome = certificazioneAggiornata.nome;
        certificazione.descrizione = certificazioneAggiornata.descrizione;
        certificazione.conseguita = certificazioneAggiornata.conseguita;

        if (certificazione.commonEntity == null) {
            certificazione.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        certificazione.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(certificazione).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina una certificazione", description = "Elimina una certificazione dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Certificazione eliminata con successo"),
        @APIResponse(responseCode = "404", description = "Certificazione non trovata")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID della certificazione", required = true) Long id
    ) {
        Certificazione certificazione = Certificazione.findById(id);
        if (certificazione == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        certificazione.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta le certificazioni", description = "Restituisce il numero totale di certificazioni nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Certificazione.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
