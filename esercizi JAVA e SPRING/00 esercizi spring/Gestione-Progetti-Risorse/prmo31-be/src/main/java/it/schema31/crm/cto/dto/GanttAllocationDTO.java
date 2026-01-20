package it.schema31.crm.cto.dto;

import java.time.LocalDate;

public class GanttAllocationDTO {
    public Long utenteProgettoId;
    public Long progettoId;
    public String progettoNome;
    public Integer percentualeImpegno;
    public String note;
    public LocalDate dataInizio;
    public LocalDate dataFine;

    public GanttAllocationDTO() {
    }

    public GanttAllocationDTO(Long utenteProgettoId, Long progettoId, String progettoNome, Integer percentualeImpegno, String note, LocalDate dataInizio, LocalDate dataFine) {
        this.utenteProgettoId = utenteProgettoId;
        this.progettoId = progettoId;
        this.progettoNome = progettoNome;
        this.percentualeImpegno = percentualeImpegno;
        this.note = note;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }
}
