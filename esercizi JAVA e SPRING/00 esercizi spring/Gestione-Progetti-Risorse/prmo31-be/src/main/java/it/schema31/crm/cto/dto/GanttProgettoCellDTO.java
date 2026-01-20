package it.schema31.crm.cto.dto;

import java.util.ArrayList;
import java.util.List;

public class GanttProgettoCellDTO {
    public Integer percentualeTotale;
    public List<GanttUtenteAllocationDTO> allocazioni;
    public Double percentualeAllocazione;

    public GanttProgettoCellDTO() {
        this.percentualeTotale = 0;
        this.allocazioni = new ArrayList<>();
        this.percentualeAllocazione = 0.0;
    }

    public GanttProgettoCellDTO(Integer percentualeTotale, List<GanttUtenteAllocationDTO> allocazioni) {
        this.percentualeTotale = percentualeTotale;
        this.allocazioni = allocazioni != null ? allocazioni : new ArrayList<>();
        this.percentualeAllocazione = 0.0;
    }
}
