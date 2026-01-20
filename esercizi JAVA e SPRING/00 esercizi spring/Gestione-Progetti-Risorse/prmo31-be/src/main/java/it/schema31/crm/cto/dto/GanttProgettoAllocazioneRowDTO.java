package it.schema31.crm.cto.dto;

import java.util.ArrayList;
import java.util.List;

public class GanttProgettoAllocazioneRowDTO {
    public Long progettoId;
    public String progettoNome;
    public List<GanttProgettoCellDTO> periods;
    public List<GanttUtenteAllocatoDTO> utenti;

    public GanttProgettoAllocazioneRowDTO() {
        this.periods = new ArrayList<>();
        this.utenti = new ArrayList<>();
    }

    public GanttProgettoAllocazioneRowDTO(Long progettoId, String progettoNome) {
        this.progettoId = progettoId;
        this.progettoNome = progettoNome;
        this.periods = new ArrayList<>();
        this.utenti = new ArrayList<>();
    }
}
