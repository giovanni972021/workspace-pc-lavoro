package it.schema31.crm.cto.dto;

import java.time.LocalDate;

public class GanttUtenteAllocationDTO {
    public Long utenteProgettoId;
    public Long utenteId;
    public String utenteNome;
    public String utenteCognome;
    public Integer percentualeImpegno;
    public String note;
    public LocalDate dataInizio;
    public LocalDate dataFine;

    public GanttUtenteAllocationDTO() {
    }

    public GanttUtenteAllocationDTO(Long utenteProgettoId, Long utenteId, String utenteNome, String utenteCognome, Integer percentualeImpegno, String note, LocalDate dataInizio, LocalDate dataFine) {
        this.utenteProgettoId = utenteProgettoId;
        this.utenteId = utenteId;
        this.utenteNome = utenteNome;
        this.utenteCognome = utenteCognome;
        this.percentualeImpegno = percentualeImpegno;
        this.note = note;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }
}
