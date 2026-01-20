package it.schema31.crm.cto.resources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import it.schema31.crm.cto.dto.UtenteDetailDTO;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.security.Authenticated;
import it.schema31.crm.cto.entities.Utente;
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

@Path("/api/utenti")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Utenti", description = "Gestione degli utenti del sistema")
@Authenticated
public class UtenteResource {

    @GET
    @Operation(summary = "Elenca tutti gli utenti", description = "Recupera la lista di tutti gli utenti del sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista utenti recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = it.schema31.crm.cto.dto.UtenteListDTO.class)))
    })
    @Transactional
    public List<it.schema31.crm.cto.dto.UtenteListDTO> list(
        @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
        @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size
    ) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        return Utente.<Utente>find(
                "SELECT DISTINCT u FROM Utente u " +
                "LEFT JOIN FETCH u.jobTitle " +
                "LEFT JOIN FETCH u.tipoUtente")
            .page(pageNumber, pageSize)
            .stream()
            .map(u -> {
                String email = extractEmail(u);
                String tipoUtente = extractTipoUtente(u);
                String jobTitle = extractJobTitle(u);
                return new it.schema31.crm.cto.dto.UtenteListDTO(
                    u.id, u.nome, u.cognome, u.username, u.codiceFiscale, email, tipoUtente, jobTitle, u.attivo
                );
            })
            .toList();
    }

    @GET
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Recupera un utente per ID", description = "Recupera i dettagli di un singolo utente dato il suo ID")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Utente trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UtenteDetailDTO.class))),
        @APIResponse(responseCode = "404", description = "Utente non trovato")
    })
    public Response getById(
        @PathParam("id") @Parameter(description = "ID dell'utente", required = true) Long id
    ) {
        Utente utente = Utente.findById(id);
        if (utente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Convert to DTO to avoid lazy loading issues during JSON serialization
        UtenteDetailDTO dto = mapToDetailDTO(utente);

        return Response.ok(dto).build();
    }

    /**
     * Maps an Utente entity to UtenteDetailDTO, safely handling lazy loading
     */
    private UtenteDetailDTO mapToDetailDTO(Utente utente) {
        UtenteDetailDTO dto = new UtenteDetailDTO();
        dto.id = utente.id;
        dto.nome = utente.nome;
        dto.cognome = utente.cognome;
        dto.username = utente.username;
        dto.codiceFiscale = utente.codiceFiscale;
        dto.dataInizioCollaborazione = utente.dataInizioCollaborazione;
        dto.dataFineCollaborazione = utente.dataFineCollaborazione;
        dto.valutazione = utente.valutazione;
        dto.note = utente.note;
        dto.attivo = utente.attivo;

        if (utente.commonEntity != null) {
            dto.updatedAt = utente.commonEntity.updatedAt;
            if (utente.commonEntity.updatedBy != null) {
                dto.updatedBy = new UtenteDetailDTO.UpdatedByDTO(
                    utente.commonEntity.updatedBy.id,
                    utente.commonEntity.updatedBy.nome,
                    utente.commonEntity.updatedBy.cognome
                );
            }
        }

        // Map tipoUtente
        if (utente.tipoUtente != null) {
            dto.tipoUtente = new UtenteDetailDTO.TipoUtenteDTO(
                utente.tipoUtente.id,
                utente.tipoUtente.tipo
            );
        }

        // Map jobTitle
        if (utente.jobTitle != null) {
            dto.jobTitle = new UtenteDetailDTO.JobTitleDTO(
                utente.jobTitle.id,
                utente.jobTitle.nome
            );
        }

        // Map profilo with ruoli
        if (utente.profilo != null) {
            dto.profilo = new UtenteDetailDTO.ProfiloDTO(
                utente.profilo.id,
                utente.profilo.nome,
                utente.profilo.descrizione
            );
            // Map ruoli if present
            if (utente.profilo.ruoli != null) {
                dto.profilo.ruoli = new ArrayList<>();
                for (var ruolo : utente.profilo.ruoli) {
                    dto.profilo.ruoli.add(new UtenteDetailDTO.RuoloDTO(ruolo.id, ruolo.nome));
                }
            }
        }

        // Map contatti
        if (utente.contatti != null) {
            dto.contatti = new ArrayList<>();
            for (var contatto : utente.contatti) {
                UtenteDetailDTO.ContattoDTO contattoDTO = new UtenteDetailDTO.ContattoDTO(
                    contatto.id,
                    contatto.riferimento,
                    contatto.valore
                );
                if (contatto.tipoContatto != null) {
                    contattoDTO.tipoContatto = new UtenteDetailDTO.TipoContattoDTO(
                        contatto.tipoContatto.id,
                        contatto.tipoContatto.nome
                    );
                }
                dto.contatti.add(contattoDTO);
            }
        }

        return dto;
    }

    @GET
    @Path("/search")
    @Transactional
    @Operation(summary = "Cerca utenti per nome", description = "Cerca utenti il cui nome contiene la stringa specificata")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Risultati della ricerca",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = it.schema31.crm.cto.dto.UtenteListDTO.class)))
    })
    public List<it.schema31.crm.cto.dto.UtenteListDTO> searchByName(
        @QueryParam("q") @Parameter(description = "Nome da cercare", required = true) String name
    ) {
        return Utente.<Utente>find(
                "SELECT DISTINCT u FROM Utente u " +
                "LEFT JOIN FETCH u.jobTitle " +
                "LEFT JOIN FETCH u.tipoUtente " +
                "WHERE u.nome LIKE CONCAT('%', CONCAT(:name, '%'))",
                java.util.Map.of("name", name))
            .stream()
            .map(u -> {
                String email = extractEmail(u);
                String tipoUtente = extractTipoUtente(u);
                String jobTitle = extractJobTitle(u);
                return new it.schema31.crm.cto.dto.UtenteListDTO(
                    u.id, u.nome, u.cognome, u.username, u.codiceFiscale, email, tipoUtente, jobTitle, u.attivo
                );
            })
            .toList();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo utente", description = "Crea un nuovo utente nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Utente creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Utente.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Utente già esistente")
    })
    public Response create(@Valid Utente utente) {
        if (Utente.exists(utente)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un utente con questo username esiste già")
                .build();
        }

        // Gestisci i contatti e i loro tipi
        if (utente.contatti != null && !utente.contatti.isEmpty()) {
            processContatti(utente.contatti);
        }

        utente.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        utente.commonEntity.updatedAt = java.time.LocalDateTime.now();
        utente.persist();

        // Inizializza le collezioni lazy per la serializzazione
        if (utente.contatti != null) {
            utente.contatti.size(); // Forza il caricamento della collezione
        }
        if (utente.tipoUtente != null && utente.tipoUtente.tipo != null) {
            utente.tipoUtente.tipo.length(); // Forza il caricamento dell'entità
        }
        if (utente.jobTitle != null && utente.jobTitle.nome != null) {
            utente.jobTitle.nome.length(); // Forza il caricamento dell'entità
        }
        if (utente.profilo != null && utente.profilo.nome != null) {
            utente.profilo.nome.length(); // Forza il caricamento dell'entità
        }

        return Response.status(Response.Status.CREATED).entity(utente).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un utente", description = "Aggiorna i dati di un utente esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Utente aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Utente.class))),
        @APIResponse(responseCode = "404", description = "Utente non trovato"),
        @APIResponse(responseCode = "409", description = "Conflitto con utente esistente")
    })
    public Response update(
        @PathParam("id") @Parameter(description = "ID dell'utente", required = true) Long id,
        @Valid Utente utenteAggiornato
    ) {
        Utente utente = Utente.findById(id);
        if (utente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (Utente.exists(utenteAggiornato, id)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Un utente con questo username esiste già")
                .build();
        }

        utente.nome = utenteAggiornato.nome;
        utente.cognome = utenteAggiornato.cognome;
        utente.username = utenteAggiornato.username;
        utente.codiceFiscale = utenteAggiornato.codiceFiscale;
        utente.tipoUtente = utenteAggiornato.tipoUtente;
        utente.jobTitle = utenteAggiornato.jobTitle;
        utente.profilo = utenteAggiornato.profilo;
        utente.dataInizioCollaborazione = utenteAggiornato.dataInizioCollaborazione;
        utente.dataFineCollaborazione = utenteAggiornato.dataFineCollaborazione;
        utente.valutazione = utenteAggiornato.valutazione;
        utente.note = utenteAggiornato.note;
        utente.attivo = utenteAggiornato.attivo;

        // Aggiorna i contatti (email, telefono, indirizzo)
        if (utenteAggiornato.contatti != null) {
            // Merge contacts: update existing ones and add new ones
            if (utente.contatti == null) {
                utente.contatti = new java.util.ArrayList<>();
            }

            for (it.schema31.crm.cto.entities.Contatto nuovoContatto : utenteAggiornato.contatti) {
                if (nuovoContatto.id != null) {
                    // Update existing contact
                    it.schema31.crm.cto.entities.Contatto contattoEsistente =
                        it.schema31.crm.cto.entities.Contatto.findById(nuovoContatto.id);
                    if (contattoEsistente != null) {
                        contattoEsistente.valore = nuovoContatto.valore;
                        contattoEsistente.riferimento = nuovoContatto.riferimento;
                        processContatto(contattoEsistente);
                    }
                } else {
                    // New contact - process it and add to the list
                    it.schema31.crm.cto.entities.Contatto contattoProcessato = processContatto(nuovoContatto);
                    if (!utente.contatti.contains(contattoProcessato)) {
                        utente.contatti.add(contattoProcessato);
                    }
                }
            }
        }

        if (utente.commonEntity == null) {
            utente.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        utente.commonEntity.updatedAt = java.time.LocalDateTime.now();

        // Inizializza le collezioni lazy per la serializzazione
        if (utente.contatti != null) {
            utente.contatti.size(); // Forza il caricamento della collezione
        }
        if (utente.tipoUtente != null && utente.tipoUtente.tipo != null) {
            utente.tipoUtente.tipo.length(); // Forza il caricamento dell'entità
        }
        if (utente.jobTitle != null && utente.jobTitle.nome != null) {
            utente.jobTitle.nome.length(); // Forza il caricamento dell'entità
        }
        if (utente.profilo != null && utente.profilo.nome != null) {
            utente.profilo.nome.length(); // Forza il caricamento dell'entità
        }

        return Response.ok(utente).build();
    }

    /**
     * Processa un singolo contatto per trovare o creare il TipoContatto associato
     * e gestisce contatti duplicati in base al riferimento
     */
    private it.schema31.crm.cto.entities.Contatto processContatto(it.schema31.crm.cto.entities.Contatto contatto) {
        // Prima controlla se esiste già un contatto con lo stesso riferimento
        if (contatto.riferimento != null) {
            it.schema31.crm.cto.entities.Contatto contattoEsistente =
                it.schema31.crm.cto.entities.Contatto.find("riferimento", contatto.riferimento).firstResult();

            if (contattoEsistente != null) {
                // Aggiorna il contatto esistente con i nuovi valori
                contattoEsistente.valore = contatto.valore;
                if (contatto.tipoContatto != null) {
                    contattoEsistente.tipoContatto = processTipoContatto(contatto.tipoContatto);
                }
                contattoEsistente.commonEntity.updatedAt = java.time.LocalDateTime.now();
                return contattoEsistente;
            }
        }

        // Processa il TipoContatto
        if (contatto.tipoContatto != null) {
            contatto.tipoContatto = processTipoContatto(contatto.tipoContatto);
        }

        // Imposta commonEntity per il nuovo contatto
        if (contatto.commonEntity == null) {
            contatto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
        }
        contatto.commonEntity.updatedAt = java.time.LocalDateTime.now();

        return contatto;
    }

    /**
     * Processa un TipoContatto per trovare quello esistente o crearne uno nuovo
     */
    private it.schema31.crm.cto.entities.TipoContatto processTipoContatto(it.schema31.crm.cto.entities.TipoContatto tipoContatto) {
        if (tipoContatto.nome != null) {
            // Cerca il TipoContatto esistente per nome
            it.schema31.crm.cto.entities.TipoContatto tipoEsistente =
                it.schema31.crm.cto.entities.TipoContatto.find("nome", tipoContatto.nome).firstResult();

            if (tipoEsistente != null) {
                // Usa il tipo esistente
                return tipoEsistente;
            } else {
                // Crea un nuovo tipo se non esiste
                if (tipoContatto.commonEntity == null) {
                    tipoContatto.commonEntity = new it.schema31.crm.cto.entities.CommonEntity();
                }
                tipoContatto.commonEntity.updatedAt = java.time.LocalDateTime.now();
                return tipoContatto;
            }
        }
        return tipoContatto;
    }

    /**
     * Processa i contatti per trovare o creare i TipoContatto associati
     * e gestire contatti duplicati
     */
    private void processContatti(java.util.List<it.schema31.crm.cto.entities.Contatto> contatti) {
        if (contatti == null) return;

        for (int i = 0; i < contatti.size(); i++) {
            contatti.set(i, processContatto(contatti.get(i)));
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un utente", description = "Elimina un utente dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Utente eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Utente non trovato")
    })
    public Response delete(
        @PathParam("id") @Parameter(description = "ID dell'utente", required = true) Long id
    ) {
        Utente utente = Utente.findById(id);
        if (utente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        utente.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta gli utenti", description = "Restituisce il numero totale di utenti nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = Utente.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }

    /**
     * Estrae l'email dai contatti dell'utente
     * Cerca un contatto di tipo "Email" o "email" nella lista dei contatti
     * Forza il caricamento della collezione lazy se necessario
     */
    private String extractEmail(Utente utente) {
        if (utente.contatti == null) {
            return null;
        }

        // Forza il caricamento della collezione lazy
        try {
            if (utente.contatti.isEmpty()) {
                return null;
            }

            return utente.contatti.stream()
                .filter(c -> c.tipoContatto != null &&
                            c.tipoContatto.nome != null &&
                            c.tipoContatto.nome.equalsIgnoreCase("Email"))
                .map(c -> c.valore)
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            // In caso di errore nel caricamento lazy, ritorna null
            return null;
        }
    }

    /**
     * Estrae il tipo utente
     */
    private String extractTipoUtente(Utente utente) {
        if (utente.tipoUtente == null) {
            return null;
        }
        return utente.tipoUtente.tipo;
    }

    /**
     * Estrae il nome del JobTitle dell'utente
     */
    private String extractJobTitle(Utente utente) {
        if (utente.jobTitle == null) {
            return null;
        }
        return utente.jobTitle.nome;
    }
}
