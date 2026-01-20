package it.schema31.crm.cto.dto;

import java.util.ArrayList;
import java.util.List;

public class GanttResponseDTO {
    public List<GanttPeriodDTO> periodi;
    public List<GanttUtenteRowDTO> utenti;

    public GanttResponseDTO() {
        this.periodi = new ArrayList<>();
        this.utenti = new ArrayList<>();
    }

    public GanttResponseDTO(List<GanttPeriodDTO> periodi, List<GanttUtenteRowDTO> utenti) {
        this.periodi = periodi != null ? periodi : new ArrayList<>();
        this.utenti = utenti != null ? utenti : new ArrayList<>();
    }
}
