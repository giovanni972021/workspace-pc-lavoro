package it.schema31.crm.cto.dto;

import java.util.ArrayList;
import java.util.List;

public class GanttCellDTO {
    public Integer percentualeTotale;
    public List<GanttAllocationDTO> allocazioni;

    public GanttCellDTO() {
        this.percentualeTotale = 0;
        this.allocazioni = new ArrayList<>();
    }

    public GanttCellDTO(Integer percentualeTotale, List<GanttAllocationDTO> allocazioni) {
        this.percentualeTotale = percentualeTotale;
        this.allocazioni = allocazioni != null ? allocazioni : new ArrayList<>();
    }
}
