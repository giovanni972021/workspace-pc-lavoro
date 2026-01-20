package it.schema31.crm.cto.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.security.Authenticated;
import it.schema31.crm.cto.dto.gitlab.*;
import it.schema31.crm.cto.entities.Progetto;
import it.schema31.crm.cto.entities.Utente;
import it.schema31.crm.cto.services.GitLabService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/gitlab")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "GitLab", description = "Integrazione con GitLab per statistiche di sviluppo")
@Authenticated
public class GitLabResource {

    @Inject
    GitLabService gitLabService;

    @GET
    @Path("/projects")
    @Operation(summary = "Elenca tutti i progetti GitLab", description = "Recupera la lista di tutti i progetti GitLab accessibili")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista progetti recuperata con successo"),
            @APIResponse(responseCode = "500", description = "Errore di comunicazione con GitLab")
    })
    public Response getProjects() {
        try {
            List<GitLabProjectDTO> projects = gitLabService.getProjects();
            return Response.ok(projects).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/stats/commits")
    @Operation(summary = "Statistiche commit per utente", description = "Recupera le statistiche dei commit (righe aggiunte/rimosse) raggruppate per utente e per progetto")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Statistiche recuperate con successo"),
            @APIResponse(responseCode = "500", description = "Errore di comunicazione con GitLab")
    })
    public Response getCommitStats(
            @QueryParam("startDate") @Parameter(description = "Data inizio (YYYY-MM-DD)", required = true) String startDate,
            @QueryParam("endDate") @Parameter(description = "Data fine (YYYY-MM-DD)", required = true) String endDate,
            @QueryParam("userEmail") @Parameter(description = "Email utente da filtrare") String userEmail,
            @QueryParam("projectId") @Parameter(description = "ID progetto GitLab da filtrare") Long projectId) {
        try {
            GitLabStatsResponseDTO stats = gitLabService.getCommitStats(startDate, endDate, userEmail, projectId);
            return Response.ok(stats).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/stats/merge-requests")
    @Operation(summary = "Statistiche Merge Request per utente", description = "Recupera le statistiche delle Merge Request raggruppate per utente e progetto")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Statistiche MR recuperate con successo"),
            @APIResponse(responseCode = "500", description = "Errore di comunicazione con GitLab")
    })
    public Response getMergeRequestStats(
            @QueryParam("startDate") @Parameter(description = "Data inizio (YYYY-MM-DD)") String startDate,
            @QueryParam("endDate") @Parameter(description = "Data fine (YYYY-MM-DD)") String endDate,
            @QueryParam("userEmail") @Parameter(description = "Email utente da filtrare") String userEmail,
            @QueryParam("projectId") @Parameter(description = "ID progetto GitLab da filtrare") Long projectId) {
        try {
            List<MergeRequestStatsDTO> stats = gitLabService.getMergeRequestStats(startDate, endDate, userEmail, projectId);
            return Response.ok(stats).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/stats/issues")
    @Operation(summary = "Statistiche Issue per utente", description = "Recupera le statistiche delle Issue raggruppate per utente e progetto")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Statistiche Issue recuperate con successo"),
            @APIResponse(responseCode = "500", description = "Errore di comunicazione con GitLab")
    })
    public Response getIssueStats(
            @QueryParam("startDate") @Parameter(description = "Data inizio (YYYY-MM-DD)") String startDate,
            @QueryParam("endDate") @Parameter(description = "Data fine (YYYY-MM-DD)") String endDate,
            @QueryParam("userEmail") @Parameter(description = "Email utente da filtrare") String userEmail,
            @QueryParam("projectId") @Parameter(description = "ID progetto GitLab da filtrare") Long projectId) {
        try {
            List<IssueStatsDTO> stats = gitLabService.getIssueStats(startDate, endDate, userEmail, projectId);
            return Response.ok(stats).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/utenti")
    @Transactional
    @Operation(summary = "Lista utenti per autocomplete", description = "Recupera la lista di utenti filtrabili per nome per l'autocomplete nei filtri GitLab")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista utenti recuperata con successo")
    })
    public Response getUtenti(
            @QueryParam("q") @Parameter(description = "Testo per filtrare nome/cognome") String query) {
        try {
            List<SimpleUtenteDTO> utenti;
            if (query != null && !query.trim().isEmpty()) {
                String searchTerm = "%" + query.toLowerCase() + "%";
                utenti = Utente.<Utente>find(
                        "SELECT u FROM Utente u " +
                        "LEFT JOIN u.contatti c LEFT JOIN c.tipoContatto tc " +
                        "WHERE LOWER(u.nome) LIKE ?1 OR LOWER(u.cognome) LIKE ?1 " +
                        "OR LOWER(CONCAT(u.nome, ' ', u.cognome)) LIKE ?1",
                        searchTerm)
                    .stream()
                    .map(u -> new SimpleUtenteDTO(u.id, u.nome, u.cognome, extractEmail(u)))
                    .distinct()
                    .limit(20)
                    .collect(Collectors.toList());
            } else {
                utenti = Utente.<Utente>findAll()
                    .stream()
                    .map(u -> new SimpleUtenteDTO(u.id, u.nome, u.cognome, extractEmail(u)))
                    .limit(50)
                    .collect(Collectors.toList());
            }
            return Response.ok(utenti).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/progetti-crm")
    @Transactional
    @Operation(summary = "Lista progetti CRM per autocomplete", description = "Recupera la lista di progetti CRM filtrabili per nome per l'autocomplete nei filtri GitLab")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista progetti recuperata con successo")
    })
    public Response getProgettiCRM(
            @QueryParam("q") @Parameter(description = "Testo per filtrare nome progetto") String query) {
        try {
            List<SimpleProgettoDTO> progetti;
            if (query != null && !query.trim().isEmpty()) {
                String searchTerm = "%" + query.toLowerCase() + "%";
                progetti = Progetto.<Progetto>find(
                        "LOWER(nome) LIKE ?1", searchTerm)
                    .stream()
                    .map(p -> new SimpleProgettoDTO(p.id, p.nome))
                    .limit(20)
                    .collect(Collectors.toList());
            } else {
                progetti = Progetto.<Progetto>findAll()
                    .stream()
                    .map(p -> new SimpleProgettoDTO(p.id, p.nome))
                    .limit(50)
                    .collect(Collectors.toList());
            }
            return Response.ok(progetti).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    private String extractEmail(Utente utente) {
        if (utente.contatti == null) {
            return null;
        }
        try {
            return utente.contatti.stream()
                .filter(c -> c.tipoContatto != null &&
                            c.tipoContatto.nome != null &&
                            c.tipoContatto.nome.equalsIgnoreCase("Email"))
                .map(c -> c.valore)
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
