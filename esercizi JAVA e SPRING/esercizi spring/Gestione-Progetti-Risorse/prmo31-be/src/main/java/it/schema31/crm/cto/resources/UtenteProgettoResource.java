package it.schema31.crm.cto.resources;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
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
import it.schema31.crm.cto.dto.GanttAllocationDTO;
import it.schema31.crm.cto.dto.GanttCellDTO;
import it.schema31.crm.cto.dto.GanttPeriodDTO;
import it.schema31.crm.cto.dto.GanttProgettoCellDTO;
import it.schema31.crm.cto.dto.GanttProgettoAllocazioneRowDTO;
import it.schema31.crm.cto.dto.GanttProgettoRowDTO;
import it.schema31.crm.cto.dto.GanttProgettiResponseDTO;
import it.schema31.crm.cto.dto.GanttResponseDTO;
import it.schema31.crm.cto.dto.GanttUtenteAllocatoDTO;
import it.schema31.crm.cto.dto.GanttUtenteAllocationDTO;
import it.schema31.crm.cto.dto.GanttUtenteRowDTO;
import it.schema31.crm.cto.entities.CommonEntity;
import it.schema31.crm.cto.entities.Progetto;
import it.schema31.crm.cto.entities.Utente;
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

@Path("/api/utente-progetto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Utente Progetto", description = "Gestione assegnazione utenti ai progetti")
public class UtenteProgettoResource {

    @GET
    @Operation(summary = "Elenca tutti gli assegnamenti utente-progetto", description = "Recupera la lista di tutti gli assegnamenti con paginazione")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UtenteProgetto.class)))
    })
    public List<UtenteProgetto> list(
            @QueryParam("page") @Parameter(description = "Numero di pagina (default 0)") Integer page,
            @QueryParam("size") @Parameter(description = "Dimensione pagina (default 20)") Integer size) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PanacheQuery<UtenteProgetto> query = UtenteProgetto.find(
            "SELECT DISTINCT up FROM UtenteProgetto up " +
            "LEFT JOIN FETCH up.utente u " +
            "LEFT JOIN FETCH up.progetto p " +
            "ORDER BY u.cognome, u.nome, up.dataInizio"
        );

        List<UtenteProgetto> results = query.page(pageNumber, pageSize).list();

        // Force lazy loading of nested associations to avoid LazyInitializationException
        results.forEach(up -> {
            if (up.utente != null) {
                up.utente.nome.length();
                if (up.utente.tipoUtente != null) {
                    up.utente.tipoUtente.tipo.length();
                }
                if (up.utente.jobTitle != null) {
                    up.utente.jobTitle.nome.length();
                }
                if (up.utente.profilo != null) {
                    up.utente.profilo.nome.length();
                }
                if (up.utente.contatti != null) {
                    up.utente.contatti.size();
                }
            }
            if (up.progetto != null) {
                up.progetto.nome.length();
                if (up.progetto.numberJobTitles != null) {
                    up.progetto.numberJobTitles.size();
                }
            }
        });

        return results;
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Recupera un assegnamento per ID", description = "Recupera i dettagli di un singolo assegnamento utente-progetto")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Assegnamento trovato",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UtenteProgetto.class))),
        @APIResponse(responseCode = "404", description = "Assegnamento non trovato")
    })
    public Response getById(@PathParam("id") @Parameter(description = "ID dell'assegnamento", required = true) Long id) {
        UtenteProgetto utenteProgetto = UtenteProgetto.findById(id);
        if (utenteProgetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Force lazy loading of nested associations to avoid LazyInitializationException
        if (utenteProgetto.utente != null) {
            utenteProgetto.utente.nome.length();
            if (utenteProgetto.utente.tipoUtente != null) {
                utenteProgetto.utente.tipoUtente.tipo.length();
            }
            if (utenteProgetto.utente.jobTitle != null) {
                utenteProgetto.utente.jobTitle.nome.length();
            }
            if (utenteProgetto.utente.profilo != null) {
                utenteProgetto.utente.profilo.nome.length();
            }
            if (utenteProgetto.utente.contatti != null) {
                utenteProgetto.utente.contatti.size();
            }
        }
        if (utenteProgetto.progetto != null) {
            utenteProgetto.progetto.nome.length();
            if (utenteProgetto.progetto.numberJobTitles != null) {
                utenteProgetto.progetto.numberJobTitles.size();
            }
        }

        return Response.ok(utenteProgetto).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Crea un nuovo assegnamento", description = "Crea un nuovo assegnamento utente-progetto")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Assegnamento creato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UtenteProgetto.class))),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "404", description = "Utente o Progetto non trovato"),
        @APIResponse(responseCode = "409", description = "Sovrapposizione di date per utente-progetto")
    })
    public Response create(@Valid UtenteProgetto utenteProgetto) {
        // Validate utente and progetto exist
        if (utenteProgetto.utente == null || utenteProgetto.utente.id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Utente è obbligatorio\"}")
                .build();
        }
        if (utenteProgetto.progetto == null || utenteProgetto.progetto.id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Progetto è obbligatorio\"}")
                .build();
        }

        // Validate dates
        if (utenteProgetto.dataInizio == null || utenteProgetto.dataFine == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Data inizio e data fine sono obbligatori\"}")
                .build();
        }

        if (utenteProgetto.dataFine.isBefore(utenteProgetto.dataInizio)) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"La data fine non può essere precedente alla data inizio\"}")
                .build();
        }

        Utente utente = Utente.findById(utenteProgetto.utente.id);
        if (utente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Utente non trovato\"}")
                .build();
        }

        Progetto progetto = Progetto.findById(utenteProgetto.progetto.id);
        if (progetto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Progetto non trovato\"}")
                .build();
        }

        // Check for date overlap
        if (UtenteProgetto.hasDateOverlap(utenteProgetto.utente.id, utenteProgetto.progetto.id,
                                          utenteProgetto.dataInizio, utenteProgetto.dataFine)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("{\"message\": \"Esiste già un'allocazione per questo utente e progetto nelle date specificate\"}")
                .build();
        }

        utenteProgetto.utente = utente;
        utenteProgetto.progetto = progetto;

        // Initialize CommonEntity
        utenteProgetto.commonEntity = new CommonEntity();
        utenteProgetto.commonEntity.updatedAt = java.time.LocalDateTime.now();

        utenteProgetto.persist();

        // Force lazy loading of nested associations to avoid LazyInitializationException
        if (utenteProgetto.utente != null) {
            utenteProgetto.utente.nome.length();
            if (utenteProgetto.utente.tipoUtente != null) {
                utenteProgetto.utente.tipoUtente.tipo.length();
            }
            if (utenteProgetto.utente.jobTitle != null) {
                utenteProgetto.utente.jobTitle.nome.length();
            }
            if (utenteProgetto.utente.profilo != null) {
                utenteProgetto.utente.profilo.nome.length();
            }
            if (utenteProgetto.utente.contatti != null) {
                utenteProgetto.utente.contatti.size();
            }
        }
        if (utenteProgetto.progetto != null) {
            utenteProgetto.progetto.nome.length();
            if (utenteProgetto.progetto.numberJobTitles != null) {
                utenteProgetto.progetto.numberJobTitles.size();
            }
        }

        return Response.status(Response.Status.CREATED).entity(utenteProgetto).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Aggiorna un assegnamento", description = "Aggiorna i dati di un assegnamento utente-progetto esistente")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Assegnamento aggiornato con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UtenteProgetto.class))),
        @APIResponse(responseCode = "404", description = "Assegnamento non trovato"),
        @APIResponse(responseCode = "400", description = "Dati non validi"),
        @APIResponse(responseCode = "409", description = "Sovrapposizione di date per utente-progetto")
    })
    public Response update(
            @PathParam("id") @Parameter(description = "ID dell'assegnamento", required = true) Long id,
            @Valid UtenteProgetto updatedUtenteProgetto) {

        UtenteProgetto utenteProgetto = UtenteProgetto.findById(id);
        if (utenteProgetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Validate dates
        if (updatedUtenteProgetto.dataInizio == null || updatedUtenteProgetto.dataFine == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Data inizio e data fine sono obbligatori\"}")
                .build();
        }

        if (updatedUtenteProgetto.dataFine.isBefore(updatedUtenteProgetto.dataInizio)) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"La data fine non può essere precedente alla data inizio\"}")
                .build();
        }

        // Update utente if changed
        if (updatedUtenteProgetto.utente != null && updatedUtenteProgetto.utente.id != null) {
            Utente utente = Utente.findById(updatedUtenteProgetto.utente.id);
            if (utente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Utente non trovato\"}")
                    .build();
            }
            utenteProgetto.utente = utente;
        }

        // Update progetto if changed
        if (updatedUtenteProgetto.progetto != null && updatedUtenteProgetto.progetto.id != null) {
            Progetto progetto = Progetto.findById(updatedUtenteProgetto.progetto.id);
            if (progetto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Progetto non trovato\"}")
                    .build();
            }
            utenteProgetto.progetto = progetto;
        }

        // Check for date overlap (excluding current record)
        if (UtenteProgetto.hasDateOverlap(utenteProgetto.utente.id, utenteProgetto.progetto.id,
                                          updatedUtenteProgetto.dataInizio, updatedUtenteProgetto.dataFine, id)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("{\"message\": \"Esiste già un'allocazione per questo utente e progetto nelle date specificate\"}")
                .build();
        }

        // Update fields
        utenteProgetto.percentualeImpegno = updatedUtenteProgetto.percentualeImpegno;
        utenteProgetto.dataInizio = updatedUtenteProgetto.dataInizio;
        utenteProgetto.dataFine = updatedUtenteProgetto.dataFine;
        utenteProgetto.note = updatedUtenteProgetto.note;

        // Update audit info
        if (utenteProgetto.commonEntity == null) {
            utenteProgetto.commonEntity = new CommonEntity();
        }
        utenteProgetto.commonEntity.updatedAt = java.time.LocalDateTime.now();

        // Force lazy loading of nested associations to avoid LazyInitializationException
        if (utenteProgetto.utente != null) {
            utenteProgetto.utente.nome.length();
            if (utenteProgetto.utente.tipoUtente != null) {
                utenteProgetto.utente.tipoUtente.tipo.length();
            }
            if (utenteProgetto.utente.jobTitle != null) {
                utenteProgetto.utente.jobTitle.nome.length();
            }
            if (utenteProgetto.utente.profilo != null) {
                utenteProgetto.utente.profilo.nome.length();
            }
            if (utenteProgetto.utente.contatti != null) {
                utenteProgetto.utente.contatti.size();
            }
        }
        if (utenteProgetto.progetto != null) {
            utenteProgetto.progetto.nome.length();
            if (utenteProgetto.progetto.numberJobTitles != null) {
                utenteProgetto.progetto.numberJobTitles.size();
            }
        }

        return Response.ok(utenteProgetto).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Elimina un assegnamento", description = "Elimina un assegnamento utente-progetto dal sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Assegnamento eliminato con successo"),
        @APIResponse(responseCode = "404", description = "Assegnamento non trovato")
    })
    public Response delete(@PathParam("id") @Parameter(description = "ID dell'assegnamento", required = true) Long id) {
        UtenteProgetto utenteProgetto = UtenteProgetto.findById(id);
        if (utenteProgetto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        utenteProgetto.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Operation(summary = "Conta gli assegnamenti", description = "Restituisce il numero totale di assegnamenti utente-progetto nel sistema")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Conteggio recuperato con successo")
    })
    public Response count() {
        long count = UtenteProgetto.count();
        return Response.ok().entity("{\"count\":" + count + "}").build();
    }

    @GET
    @Path("/gantt")
    @Operation(summary = "Vista Gantt delle allocazioni utenti", description = "Recupera la vista Gantt delle allocazioni utenti sui progetti con raggruppamento per settimane, mesi o anni")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Vista Gantt recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GanttResponseDTO.class))),
        @APIResponse(responseCode = "400", description = "Parametri non validi")
    })
    public Response gantt(
            @QueryParam("groupBy") @Parameter(description = "Raggruppamento: 'day', 'week', 'month', 'year' (default: 'week')") String groupBy,
            @QueryParam("startDate") @Parameter(description = "Data di inizio (formato: yyyy-MM-dd, default: inizio settimana/mese/anno corrente)") String startDateStr,
            @QueryParam("periods") @Parameter(description = "Numero di periodi da visualizzare (default: 52 per week, 12 per month, 3 per year)") Integer periods) {

        // Parse and validate parameters
        String groupByNormalized = groupBy != null ? groupBy.toLowerCase() : "week";
        if (!groupByNormalized.equals("day") && !groupByNormalized.equals("week") && !groupByNormalized.equals("month") && !groupByNormalized.equals("year")) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"groupBy deve essere 'day', 'week', 'month' o 'year'\"}")
                .build();
        }

        // Determine default periods based on groupBy
        int periodCount = periods != null ? periods : getDefaultPeriodCount(groupByNormalized);

        // Parse start date or use default
        LocalDate startDate = parseStartDate(startDateStr, groupByNormalized);

        // Generate periods
        List<GanttPeriodDTO> periodiList = generatePeriods(startDate, periodCount, groupByNormalized);

        // Get all active users
        List<Utente> utentiAttivi = Utente.find("attivo = true ORDER BY cognome, nome").list();

        // Calculate date range for the entire gantt
        LocalDate ganttStart = periodiList.get(0).dataInizio;
        LocalDate ganttEnd = periodiList.get(periodiList.size() - 1).dataFine;

        // Get all allocations in the date range
        List<UtenteProgetto> allocazioni = UtenteProgetto.findByDateRange(ganttStart, ganttEnd);

        // Force lazy loading
        allocazioni.forEach(up -> {
            if (up.progetto != null) {
                up.progetto.nome.length();
            }
        });

        // Group allocations by user
        Map<Long, List<UtenteProgetto>> allocazioniPerUtente = allocazioni.stream()
            .collect(Collectors.groupingBy(up -> up.utente.id));

        // Build response
        List<GanttUtenteRowDTO> utentiRows = new ArrayList<>();
        for (Utente utente : utentiAttivi) {
            GanttUtenteRowDTO row = new GanttUtenteRowDTO(
                utente.id,
                utente.nome,
                utente.cognome,
                utente.jobTitle != null ? utente.jobTitle.nome : "non definito"
            );

            List<UtenteProgetto> utenteAllocazioni = allocazioniPerUtente.getOrDefault(utente.id, new ArrayList<>());

            // Group allocations by project for this user
            Map<Long, List<UtenteProgetto>> allocazioniPerProgetto = utenteAllocazioni.stream()
                .collect(Collectors.groupingBy(up -> up.progetto.id));

            // For each period, calculate total user allocations (sum of all projects)
            for (GanttPeriodDTO period : periodiList) {
                GanttCellDTO cell = calculateCellAllocations(utenteAllocazioni, period.dataInizio, period.dataFine);
                row.periods.add(cell);
            }

            // For each project, create a project row with its allocations
            for (Map.Entry<Long, List<UtenteProgetto>> entry : allocazioniPerProgetto.entrySet()) {
                Long progettoId = entry.getKey();
                List<UtenteProgetto> progettoAllocazioni = entry.getValue();

                // Get project name from first allocation
                String progettoNome = progettoAllocazioni.isEmpty() ? "" : progettoAllocazioni.get(0).progetto.nome;

                GanttProgettoRowDTO progettoRow = new GanttProgettoRowDTO(progettoId, progettoNome);

                // For each period, calculate project allocations
                for (GanttPeriodDTO period : periodiList) {
                    GanttCellDTO cell = calculateCellAllocations(progettoAllocazioni, period.dataInizio, period.dataFine);
                    progettoRow.periods.add(cell);
                }

                row.progetti.add(progettoRow);
            }

            utentiRows.add(row);
        }

        GanttResponseDTO response = new GanttResponseDTO(periodiList, utentiRows);
        return Response.ok(response).build();
    }

    private int getDefaultPeriodCount(String groupBy) {
        switch (groupBy) {
            case "day":
                return 30;
            case "week":
                return 52;
            case "month":
                return 12;
            case "year":
                return 3;
            default:
                return 52;
        }
    }

    private LocalDate parseStartDate(String startDateStr, String groupBy) {
        if (startDateStr != null && !startDateStr.trim().isEmpty()) {
            try {
                return LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                // Fall through to default
            }
        }

        // Default to start of current period
        LocalDate now = LocalDate.now();
        switch (groupBy) {
            case "day":
                return now;
            case "week":
                return now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case "month":
                return now.withDayOfMonth(1);
            case "year":
                return now.withDayOfYear(1);
            default:
                return now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }
    }

    private List<GanttPeriodDTO> generatePeriods(LocalDate startDate, int count, String groupBy) {
        List<GanttPeriodDTO> periods = new ArrayList<>();
        LocalDate currentStart = startDate;

        for (int i = 0; i < count; i++) {
            LocalDate currentEnd;
            String label;

            switch (groupBy) {
                case "day":
                    currentEnd = currentStart;
                    label = currentStart.format(DateTimeFormatter.ofPattern("dd/MM"));
                    break;
                case "week":
                    currentEnd = currentStart.plusWeeks(1).minusDays(1);
                    label = "Settimana " + currentStart.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    break;
                case "month":
                    YearMonth yearMonth = YearMonth.from(currentStart);
                    currentEnd = yearMonth.atEndOfMonth();
                    label = "Settimana " + currentStart.getMonthValue();
                    break;
                case "year":
                    currentEnd = currentStart.withDayOfYear(currentStart.lengthOfYear());
                    label = String.valueOf(currentStart.getYear());
                    break;
                default:
                    currentEnd = currentStart.plusWeeks(1).minusDays(1);
                    label = "Settimana " + i;
            }

            periods.add(new GanttPeriodDTO(currentStart, currentEnd, label));

            // Move to next period
            switch (groupBy) {
                case "day":
                    currentStart = currentStart.plusDays(1);
                    break;
                case "week":
                    currentStart = currentStart.plusWeeks(1);
                    break;
                case "month":
                    currentStart = currentStart.plusMonths(1);
                    break;
                case "year":
                    currentStart = currentStart.plusYears(1);
                    break;
            }
        }

        return periods;
    }

    private GanttCellDTO calculateCellAllocations(List<UtenteProgetto> allocazioni, LocalDate periodStart, LocalDate periodEnd) {
        GanttCellDTO cell = new GanttCellDTO();

        for (UtenteProgetto up : allocazioni) {
            // Check if allocation overlaps with period
            if (up.dataInizio.isAfter(periodEnd) || up.dataFine.isBefore(periodStart)) {
                continue; // No overlap
            }

            // Calculate overlap
            LocalDate overlapStart = up.dataInizio.isBefore(periodStart) ? periodStart : up.dataInizio;
            LocalDate overlapEnd = up.dataFine.isAfter(periodEnd) ? periodEnd : up.dataFine;

            long totalDays = ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
            long overlapDays = ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;

            // Calculate weighted percentage for this period
            double weightedPercentage = (up.percentualeImpegno * overlapDays) / (double) totalDays;

            cell.percentualeTotale += (int) Math.round(weightedPercentage);
            cell.allocazioni.add(new GanttAllocationDTO(
                up.id,
                up.progetto.id,
                up.progetto.nome,
                up.percentualeImpegno,
                up.note,
                up.dataInizio,
                up.dataFine
            ));
        }

        return cell;
    }

    @GET
    @Path("/gantt-progetti")
    @Operation(summary = "Vista Gantt delle allocazioni progetti", description = "Recupera la vista Gantt delle allocazioni raggruppate per progetti con raggruppamento per settimane, mesi o anni")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Vista Gantt recuperata con successo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GanttProgettiResponseDTO.class))),
        @APIResponse(responseCode = "400", description = "Parametri non validi")
    })
    public Response ganttProgetti(
            @QueryParam("groupBy") @Parameter(description = "Raggruppamento: 'day', 'week', 'month', 'year' (default: 'week')") String groupBy,
            @QueryParam("startDate") @Parameter(description = "Data di inizio (formato: yyyy-MM-dd, default: inizio settimana/mese/anno corrente)") String startDateStr,
            @QueryParam("periods") @Parameter(description = "Numero di periodi da visualizzare (default: 52 per week, 12 per month, 3 per year)") Integer periods) {

        // Parse and validate parameters
        String groupByNormalized = groupBy != null ? groupBy.toLowerCase() : "week";
        if (!groupByNormalized.equals("day") && !groupByNormalized.equals("week") && !groupByNormalized.equals("month") && !groupByNormalized.equals("year")) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"groupBy deve essere 'day', 'week', 'month' o 'year'\"}")
                .build();
        }

        // Determine default periods based on groupBy
        int periodCount = periods != null ? periods : getDefaultPeriodCount(groupByNormalized);

        // Parse start date or use default
        LocalDate startDate = parseStartDate(startDateStr, groupByNormalized);

        // Generate periods
        List<GanttPeriodDTO> periodiList = generatePeriods(startDate, periodCount, groupByNormalized);

        // Calculate date range for the entire gantt
        LocalDate ganttStart = periodiList.get(0).dataInizio;
        LocalDate ganttEnd = periodiList.get(periodiList.size() - 1).dataFine;

        // Get all allocations in the date range
        List<UtenteProgetto> allocazioni = UtenteProgetto.findByDateRange(ganttStart, ganttEnd);

        // Force lazy loading
        allocazioni.forEach(up -> {
            if (up.progetto != null) {
                up.progetto.nome.length();
            }
            if (up.utente != null) {
                up.utente.nome.length();
                up.utente.cognome.length();
            }
        });

        // Group allocations by project
        Map<Long, List<UtenteProgetto>> allocazioniPerProgetto = allocazioni.stream()
            .collect(Collectors.groupingBy(up -> up.progetto.id));

        // Build response
        List<GanttProgettoAllocazioneRowDTO> progettiRows = new ArrayList<>();
        for (Map.Entry<Long, List<UtenteProgetto>> entry : allocazioniPerProgetto.entrySet()) {
            Long progettoId = entry.getKey();
            List<UtenteProgetto> progettoAllocazioni = entry.getValue();

            // Get project and force lazy loading
            Progetto progetto = null;
            if (!progettoAllocazioni.isEmpty()) {
                progetto = Progetto.findById(progettoId);
                if (progetto != null && progetto.numberJobTitles != null) {
                    progetto.numberJobTitles.size(); // Force lazy loading
                }
            }

            // Get project name from first allocation
            String progettoNome = progettoAllocazioni.isEmpty() ? "" : progettoAllocazioni.get(0).progetto.nome;

            GanttProgettoAllocazioneRowDTO row = new GanttProgettoAllocazioneRowDTO(progettoId, progettoNome);

            // For each period, calculate total project allocations (sum of all users) with percentuale allocazione
            for (GanttPeriodDTO period : periodiList) {
                GanttProgettoCellDTO cell = calculateProgettoCellAllocations(progettoAllocazioni, period.dataInizio, period.dataFine, progetto);
                row.periods.add(cell);
            }

            // Group allocations by user for this project
            Map<Long, List<UtenteProgetto>> allocazioniPerUtente = progettoAllocazioni.stream()
                .collect(Collectors.groupingBy(up -> up.utente.id));

            // For each user, create a user row with its allocations
            for (Map.Entry<Long, List<UtenteProgetto>> userEntry : allocazioniPerUtente.entrySet()) {
                Long utenteId = userEntry.getKey();
                List<UtenteProgetto> utenteAllocazioni = userEntry.getValue();

                // Get user info from first allocation
                if (!utenteAllocazioni.isEmpty()) {
                    Utente utente = utenteAllocazioni.get(0).utente;
                    GanttUtenteAllocatoDTO utenteRow = new GanttUtenteAllocatoDTO(utenteId, utente.nome, utente.cognome);

                    // For each period, calculate user allocations
                    for (GanttPeriodDTO period : periodiList) {
                        GanttProgettoCellDTO cell = calculateProgettoCellAllocations(utenteAllocazioni, period.dataInizio, period.dataFine);
                        utenteRow.periods.add(cell);
                    }

                    row.utenti.add(utenteRow);
                }
            }

            progettiRows.add(row);
        }

        GanttProgettiResponseDTO response = new GanttProgettiResponseDTO(periodiList, progettiRows);
        return Response.ok(response).build();
    }

    private GanttProgettoCellDTO calculateProgettoCellAllocations(List<UtenteProgetto> allocazioni, LocalDate periodStart, LocalDate periodEnd) {
        return calculateProgettoCellAllocations(allocazioni, periodStart, periodEnd, null);
    }

    private GanttProgettoCellDTO calculateProgettoCellAllocations(List<UtenteProgetto> allocazioni, LocalDate periodStart, LocalDate periodEnd, Progetto progetto) {
        GanttProgettoCellDTO cell = new GanttProgettoCellDTO();

        for (UtenteProgetto up : allocazioni) {
            // Check if allocation overlaps with period
            if (up.dataInizio.isAfter(periodEnd) || up.dataFine.isBefore(periodStart)) {
                continue; // No overlap
            }

            // Calculate overlap
            LocalDate overlapStart = up.dataInizio.isBefore(periodStart) ? periodStart : up.dataInizio;
            LocalDate overlapEnd = up.dataFine.isAfter(periodEnd) ? periodEnd : up.dataFine;

            long totalDays = ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
            long overlapDays = ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;

            // Calculate weighted percentage for this period
            double weightedPercentage = (up.percentualeImpegno * overlapDays) / (double) totalDays;

            cell.percentualeTotale += (int) Math.round(weightedPercentage);
            cell.allocazioni.add(new GanttUtenteAllocationDTO(
                up.id,
                up.utente.id,
                up.utente.nome,
                up.utente.cognome,
                up.percentualeImpegno,
                up.note,
                up.dataInizio,
                up.dataFine
            ));
        }

        // Calculate percentualeAllocazione for this period
        if (progetto != null && progetto.numberJobTitles != null && !progetto.numberJobTitles.isEmpty()) {
            int totalRequiredResources = progetto.numberJobTitles.stream()
                .mapToInt(njt -> njt.number != null ? njt.number : 0)
                .sum();

            if (totalRequiredResources > 0 && !cell.allocazioni.isEmpty()) {
                // Sum the percentages of all users allocated in this period
                int totalAllocatedPercentages = cell.allocazioni.stream()
                    .mapToInt(alloc -> alloc.percentualeImpegno != null ? alloc.percentualeImpegno : 0)
                    .sum();

                // Calculate percentage: (sum of user percentages / 100) / total required resources * 100
                double percentuale = ((double) totalAllocatedPercentages / 100.0) / totalRequiredResources * 100.0;
                cell.percentualeAllocazione = Math.round(percentuale * 100.0) / 100.0; // Round to 2 decimals
            }
        }

        return cell;
    }
}
