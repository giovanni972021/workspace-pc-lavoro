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
import it.schema31.crm.cto.entities.Cliente;
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

@Path("/api/clienti")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clienti", description = "Gestione dei clienti aziendali")
public class ClienteResource {

    @GET
    @Operation(summary = "Elenca tutti i clienti", description = "Recupera la lista di tutti i clienti")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista clienti recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Cliente.class)))
    })
    public List<Cliente> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Cliente> query = Cliente.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un cliente per ID", description = "Recupera i dettagli di un singolo cliente dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Cliente trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Cliente.class))),
        @APIResponse(responseCode = "404", description = "Cliente non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del cliente", required = true) Long id
    ) {
        Cliente cliente = Cliente.findById(id);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cliente).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo cliente", description = "Crea un nuovo cliente nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Cliente creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Cliente.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Cliente già esistente")
    })
    public Response create(@Valid Cliente cliente) {
        if (Cliente.exists(cliente)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un cliente con questo nome esiste già")
                .build();
        }

        cliente.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        cliente.commonEntity.updatedAt = java.time.LocalDateTime.now();
        cliente.persist();

        return Response.status(Response.Status.CREATED).entity(cliente).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un cliente", description = "Aggiorna i dati di un cliente esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Cliente aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Cliente.class))),
        @APIResponse(responseCode = "404", description = "Cliente non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del cliente", required = true) Long id,
        @Valid Cliente clienteAggiornato
    ) {
        Cliente cliente = Cliente.findById(id);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        cliente.nome = clienteAggiornato.nome;
        cliente.descrizione = clienteAggiornato.descrizione;
        cliente.feedbackScore = clienteAggiornato.feedbackScore;
        cliente.partitaIva = clienteAggiornato.partitaIva;
        cliente.codiceFiscale = clienteAggiornato.codiceFiscale;
        cliente.indirizzo = clienteAggiornato.indirizzo;
        cliente.citta = clienteAggiornato.citta;
        cliente.provincia = clienteAggiornato.provincia;
        cliente.cap = clienteAggiornato.cap;
        cliente.telefono = clienteAggiornato.telefono;
        cliente.email = clienteAggiornato.email;
        cliente.pec = clienteAggiornato.pec;
        cliente.referente = clienteAggiornato.referente;

        if (cliente.commonEntity == null) {
            cliente.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        cliente.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(cliente).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un cliente", description = "Elimina un cliente dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Cliente eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Cliente non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del cliente", required = true) Long id
    ) {
        Cliente cliente = Cliente.findById(id);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        cliente.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i clienti", description = "Restituisce il numero totale di clienti nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Cliente.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }
}
