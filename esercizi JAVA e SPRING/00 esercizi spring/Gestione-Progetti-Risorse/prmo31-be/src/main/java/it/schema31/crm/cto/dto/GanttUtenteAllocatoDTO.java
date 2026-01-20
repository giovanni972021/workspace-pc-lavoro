package it.schema31.crm.cto.dto;

import java.util.ArrayList;
import java.util.List;

public class GanttUtenteAllocatoDTO {
    public Long utenteId;
    public String nome;
    public String cognome;
    public List<GanttProgettoCellDTO> periods;

    public GanttUtenteAllocatoDTO() {
        this.periods = new ArrayList<>();
    }

    public GanttUtenteAllocatoDTO(Long utenteId, String nome, String cognome) {
        this.utenteId = utenteId;
        this.nome = nome;
        this.cognome = cognome;
        this.periods = new ArrayList<>();
    }
}
