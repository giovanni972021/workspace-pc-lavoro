package it.schema31.crm.cto.resources;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import it.schema31.crm.cto.dto.ProgettoAllocazioneDTO;
import it.schema31.crm.cto.entities.Progetto;
import it.schema31.crm.cto.entities.UtenteProgetto;
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

@Path("/api/progetti")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Progetti", description = "Gestione dei progetti aziendali")
public class ProgettoResource {

    @GET
    @Operation(summary = "Elenca tutti i progetti", description = "Recupera la lista di tutti i progetti")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista progetti recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Progetto.class)))
    })
    public List<Progetto> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<Progetto> query = Progetto.findAll();
        return query.page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un progetto per ID", description = "Recupera i dettagli di un singolo progetto dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Progetto trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Progetto.class))),
        @APIResponse(responseCode = "404", description = "Progetto non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID del progetto", required = true) Long id
    ) {
        Progetto progetto = Progetto.findById(id);
        if (progetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(progetto).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo progetto", description = "Crea un nuovo progetto nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Progetto creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Progetto.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Progetto già esistente")
    })
    public Response create(@Valid Progetto progetto) {
        if (Progetto.exists(progetto)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un progetto con questo nome esiste già")
                .build();
        }

        // Persist NumberJobTitle entities before persisting Progetto
        if (progetto.numberJobTitles != null) {
            for (it.schema31.crm.cto.entities.NumberJobTitle njt : progetto.numberJobTitles) {
                if (njt.id == null) {
                    njt.persist();
                }
            }
        }

        progetto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        progetto.commonEntity.updatedAt = java.time.LocalDateTime.now();
        progetto.persist();

        return Response.status(Response.Status.CREATED).entity(progetto).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un progetto", description = "Aggiorna i dati di un progetto esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Progetto aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Progetto.class))),
        @APIResponse(responseCode = "404", description = "Progetto non trovato")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID del progetto", required = true) Long id,
        @Valid Progetto progettoAggiornato
    ) {
        Progetto progetto = Progetto.findById(id);
        if (progetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        progetto.nome = progettoAggiornato.nome;
        progetto.descrizione = progettoAggiornato.descrizione;
        progetto.dataInizioPresunta = progettoAggiornato.dataInizioPresunta;
        progetto.dataInizioEffettiva = progettoAggiornato.dataInizioEffettiva;
        progetto.dataFinePresunta = progettoAggiornato.dataFinePresunta;
        progetto.dataFineEffettiva = progettoAggiornato.dataFineEffettiva;
        progetto.budgetPreventivato = progettoAggiornato.budgetPreventivato;
        progetto.budgetEffettivo = progettoAggiornato.budgetEffettivo;
        progetto.stato = progettoAggiornato.stato;
        progetto.priorita = progettoAggiornato.priorita;
        progetto.fattibilita = progettoAggiornato.fattibilita;

        // Delete old NumberJobTitle entities that are no longer present
        if (progetto.numberJobTitles != null) {
            for (it.schema31.crm.cto.entities.NumberJobTitle oldNjt : progetto.numberJobTitles) {
                boolean stillExists = false;
                if (progettoAggiornato.numberJobTitles != null) {
                    for (it.schema31.crm.cto.entities.NumberJobTitle newNjt : progettoAggiornato.numberJobTitles) {
                        if (oldNjt.id != null && oldNjt.id.equals(newNjt.id)) {
                            stillExists = true;
                            break;
                        }
                    }
                }
                if (!stillExists) {
                    oldNjt.delete();
                }
            }
        }

        // Persist new NumberJobTitle entities
        if (progettoAggiornato.numberJobTitles != null) {
            for (it.schema31.crm.cto.entities.NumberJobTitle njt : progettoAggiornato.numberJobTitles) {
                if (njt.id == null) {
                    njt.persist();
                }
            }
        }

        progetto.numberJobTitles = progettoAggiornato.numberJobTitles;

        if (progetto.commonEntity == null) {
            progetto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        progetto.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return Response.ok(progetto).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un progetto", description = "Elimina un progetto dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Progetto eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Progetto non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID del progetto", required = true) Long id
    ) {
        Progetto progetto = Progetto.findById(id);
        if (progetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        progetto.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta i progetti", description = "Restituisce il numero totale di progetti nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Progetto.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }

    @GET
    @Path("/allocazioni")
    @Operation(summary = "Statistiche allocazione progetti", description = "Recupera la percentuale di allocazione per ogni progetto")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Dati allocazione recuperati con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ProgettoAllocazioneDTO.class)))
    })
    public Response getAllocazioni() {
        // Recupera tutti i progetti attivi
        List<Progetto> progetti = Progetto.listAll();

        // Recupera tutte le allocazioni attive (dalla data corrente in poi)
        LocalDate oggi = LocalDate.now();
        List<UtenteProgetto> allocazioni = UtenteProgetto.find(
            "dataFine >= ?1 ORDER BY progetto.nome", oggi
        ).list();

        // Force lazy loading
        allocazioni.forEach(up -> {
            if (up.progetto != null) {
                up.progetto.nome.length();
                if (up.progetto.numberJobTitles != null) {
                    up.progetto.numberJobTitles.size();
                    up.progetto.numberJobTitles.forEach(njt -> {
                        if (njt.jobTitle != null) {
                            njt.jobTitle.nome.length();
                        }
                    });
                }
            }
            if (up.utente != null) {
                up.utente.nome.length();
            }
        });

        // Raggruppa allocazioni per progetto
        Map<Long, List<UtenteProgetto>> allocazioniPerProgetto = allocazioni.stream()
            .collect(Collectors.groupingBy(up -> up.progetto.id));

        // Crea lista di DTO con dati di allocazione
        List<ProgettoAllocazioneDTO> risultati = new ArrayList<>();

        for (Progetto progetto : progetti) {
            // Forza caricamento lazy loading
            if (progetto.numberJobTitles != null) {
                progetto.numberJobTitles.size();
                progetto.numberJobTitles.forEach(njt -> {
                    if (njt.jobTitle != null) {
                        njt.jobTitle.nome.length();
                    }
                });
            }

            // Calcola numero di risorse richieste dal progetto
            int numeroRisorseRichieste = 0;
            if (progetto.numberJobTitles != null && !progetto.numberJobTitles.isEmpty()) {
                numeroRisorseRichieste = progetto.numberJobTitles.stream()
                    .mapToInt(njt -> njt.number != null ? njt.number : 0)
                    .sum();
            }

            // Se il progetto non richiede risorse, salta
            if (numeroRisorseRichieste == 0) {
                continue;
            }

            // Ottieni allocazioni per questo progetto
            List<UtenteProgetto> progettoAllocazioni = allocazioniPerProgetto.getOrDefault(progetto.id, new ArrayList<>());

            // Calcola somma delle percentuali di impegno di tutti gli utenti allocati
            int sommaPercentualiImpegno = progettoAllocazioni.stream()
                .mapToInt(up -> up.percentualeImpegno != null ? up.percentualeImpegno : 0)
                .sum();

            // Calcola numero di risorse attualmente allocate (conteggio utenti unici)
            long numeroRisorseAllocate = progettoAllocazioni.stream()
                .map(up -> up.utente.id)
                .distinct()
                .count();

            // Calcola percentuale di allocazione: (somma percentuali / 100) / numero risorse richieste * 100
            double percentualeAllocazione = 0.0;
            if (numeroRisorseRichieste > 0) {
                percentualeAllocazione = ((double) sommaPercentualiImpegno / 100.0) / numeroRisorseRichieste * 100.0;
                percentualeAllocazione = Math.round(percentualeAllocazione * 100.0) / 100.0; // Arrotonda a 2 decimali
            }

            risultati.add(new ProgettoAllocazioneDTO(
                progetto.id,
                progetto.nome,
                percentualeAllocazione,
                numeroRisorseRichieste,
                (int) numeroRisorseAllocate
            ));
        }

        return Response.ok(risultati).build();
    }
}
