package it.schema31.crm.cto.dto.gitlab;

/**
 * Simple DTO for project autocomplete in GitLab filters
 */
public class SimpleProgettoDTO {
    public Long id;
    public String nome;

    public SimpleProgettoDTO() {
    }

    public SimpleProgettoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
