package it.schema31.crm.cto.dto;

import java.util.ArrayList;
import java.util.List;

public class GanttProgettoRowDTO {
    public Long progettoId;
    public String progettoNome;
    public List<GanttCellDTO> periods;

    public GanttProgettoRowDTO() {
        this.periods = new ArrayList<>();
    }

    public GanttProgettoRowDTO(Long progettoId, String progettoNome) {
        this.progettoId = progettoId;
        this.progettoNome = progettoNome;
        this.periods = new ArrayList<>();
    }
}
