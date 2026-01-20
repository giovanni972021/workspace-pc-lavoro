package it.schema31.crm.cto.dto;

/**
 * DTO for listing users - minimal fields to avoid loading all relationships
 */
public class UtenteListDTO {
    public Long id;
    public String nome;
    public String cognome;
    public String username;
    public String codiceFiscale;
    public String email;
    public String tipoUtente;
    public String jobTitle;
    public Boolean attivo;

    public UtenteListDTO() {
    }

    public UtenteListDTO(Long id, String nome, String cognome, String username, String codiceFiscale, String email, String tipoUtente, String jobTitle, Boolean attivo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.tipoUtente = tipoUtente;
        this.jobTitle = jobTitle;
        this.attivo = attivo;
    }
}
