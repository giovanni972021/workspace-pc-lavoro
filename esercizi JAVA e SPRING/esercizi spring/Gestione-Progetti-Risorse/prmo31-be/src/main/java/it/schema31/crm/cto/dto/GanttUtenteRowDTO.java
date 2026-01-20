package it.schema31.crm.cto.dto;

import java.util.ArrayList;
import java.util.List;

public class GanttUtenteRowDTO {
    public Long utenteId;
    public String nome;
    public String cognome;
    public String ruoloPredefinito;
    public List<GanttCellDTO> periods;
    public List<GanttProgettoRowDTO> progetti;

    public GanttUtenteRowDTO() {
        this.periods = new ArrayList<>();
        this.progetti = new ArrayList<>();
    }

    public GanttUtenteRowDTO(Long utenteId, String nome, String cognome, String ruoloPredefinito) {
        this.utenteId = utenteId;
        this.nome = nome;
        this.cognome = cognome;
        this.ruoloPredefinito = ruoloPredefinito;
        this.periods = new ArrayList<>();
        this.progetti = new ArrayList<>();
    }
}
