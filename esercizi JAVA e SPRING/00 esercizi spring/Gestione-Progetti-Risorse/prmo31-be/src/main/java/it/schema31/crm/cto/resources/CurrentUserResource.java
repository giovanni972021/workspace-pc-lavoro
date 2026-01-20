package it.schema31.crm.cto.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.security.Authenticated;
import it.schema31.crm.cto.dto.CurrentUserDTO;
import it.schema31.crm.cto.entities.Profilo;
import it.schema31.crm.cto.entities.Ruolo;
import it.schema31.crm.cto.entities.Utente;
import it.schema31.crm.cto.security.SecurityService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resource per recuperare le informazioni dell'utente corrente.
 */
@Path("/api/current-user")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Current User", description = "Informazioni sull'utente corrente")
@Authenticated
public class CurrentUserResource {

    @Inject
    SecurityService securityService;

    @GET
    @Transactional
    @Operation(summary = "Recupera i dati dell'utente corrente",
               description = "Recupera le informazioni dell'utente corrente inclusi profilo e ruoli")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Dati utente recuperati con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = CurrentUserDTO.class))),
        @APIResponse(responseCode = "404", description = "Utente non trovato")
    })
    public Response getCurrentUser() {
        if (!securityService.isAuthenticated()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Utente non autenticato")
                .build();
        }

        Utente utente = securityService.getCurrentUser();

        if (utente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Utente non trovato nel database")
                .build();
        }

        // Inizializza le relazioni lazy
        String profiloNome = null;
        Set<String> ruoli = new HashSet<>();

        if (utente.profilo != null) {
            Profilo profilo = Profilo.findById(utente.profilo.id);
            if (profilo != null) {
                profiloNome = profilo.nome;

                if (profilo.ruoli != null) {
                    profilo.ruoli.size(); // Inizializza la collezione lazy

                    for (Ruolo ruolo : profilo.ruoli) {
                        if (ruolo != null && ruolo.nome != null) {
                            ruoli.add(ruolo.nome);
                        }
                    }
                }
            }
        }

        CurrentUserDTO dto = new CurrentUserDTO(
            utente.id,
            utente.username,
            utente.nome,
            utente.cognome,
            profiloNome,
            new ArrayList<>(ruoli),
            securityService.isSuperUser()
        );

        return Response.ok(dto).build();
    }
}
