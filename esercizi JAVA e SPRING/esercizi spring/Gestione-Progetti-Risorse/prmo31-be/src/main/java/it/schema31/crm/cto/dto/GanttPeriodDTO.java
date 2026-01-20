package it.schema31.crm.cto.dto;

import java.time.LocalDate;

public class GanttPeriodDTO {
    public LocalDate dataInizio;
    public LocalDate dataFine;
    public String label;

    public GanttPeriodDTO() {
    }

    public GanttPeriodDTO(LocalDate dataInizio, LocalDate dataFine, String label) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.label = label;
    }
}
