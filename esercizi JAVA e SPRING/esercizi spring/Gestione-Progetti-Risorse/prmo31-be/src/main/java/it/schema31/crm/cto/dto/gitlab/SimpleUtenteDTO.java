package it.schema31.crm.cto.dto.gitlab;

/**
 * Simple DTO for user autocomplete in GitLab filters
 */
public class SimpleUtenteDTO {
    public Long id;
    public String nome;
    public String cognome;
    public String email;

    public SimpleUtenteDTO() {
    }

    public SimpleUtenteDTO(Long id, String nome, String cognome, String email) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public String getFullName() {
        return nome + " " + cognome;
    }
}
