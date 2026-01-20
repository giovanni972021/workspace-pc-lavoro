package it.schema31.crm.cto.dto;

import java.util.List;

/**
 * DTO per le informazioni dell'utente corrente.
 */
public class CurrentUserDTO {
    public Long id;
    public String username;
    public String nome;
    public String cognome;
    public String profilo;
    public List<String> ruoli;
    public boolean isSuperUser;

    public CurrentUserDTO() {
    }

    public CurrentUserDTO(Long id, String username, String nome, String cognome,
                         String profilo, List<String> ruoli, boolean isSuperUser) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.profilo = profilo;
        this.ruoli = ruoli;
        this.isSuperUser = isSuperUser;
    }
}
