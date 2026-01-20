package it.schema31.crm.cto.dto;

import java.util.List;

public class GanttProgettiResponseDTO {
    public List<GanttPeriodDTO> periodi;
    public List<GanttProgettoAllocazioneRowDTO> progetti;

    public GanttProgettiResponseDTO() {
    }

    public GanttProgettiResponseDTO(List<GanttPeriodDTO> periodi, List<GanttProgettoAllocazioneRowDTO> progetti) {
        this.periodi = periodi;
        this.progetti = progetti;
    }
}
