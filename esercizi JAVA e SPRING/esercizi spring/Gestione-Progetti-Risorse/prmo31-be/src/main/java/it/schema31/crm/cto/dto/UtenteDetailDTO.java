package it.schema31.crm.cto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for user detail - contains all fields needed for edit form
 */
public class UtenteDetailDTO {
    public Long id;
    public String nome;
    public String cognome;
    public String username;
    public String codiceFiscale;
    public LocalDate dataInizioCollaborazione;
    public LocalDate dataFineCollaborazione;
    public Integer valutazione;
    public String note;
    public Boolean attivo;
    public LocalDateTime updatedAt;
    public UpdatedByDTO updatedBy;

    // Related entities as nested DTOs
    public TipoUtenteDTO tipoUtente;
    public JobTitleDTO jobTitle;
    public ProfiloDTO profilo;
    public List<ContattoDTO> contatti;

    public UtenteDetailDTO() {
    }

    // Nested DTOs
    public static class TipoUtenteDTO {
        public Long id;
        public String tipo;

        public TipoUtenteDTO() {
        }

        public TipoUtenteDTO(Long id, String tipo) {
            this.id = id;
            this.tipo = tipo;
        }
    }

    public static class JobTitleDTO {
        public Long id;
        public String nome;

        public JobTitleDTO() {
        }

        public JobTitleDTO(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }
    }

    public static class ProfiloDTO {
        public Long id;
        public String nome;
        public String descrizione;
        public List<RuoloDTO> ruoli;

        public ProfiloDTO() {
        }

        public ProfiloDTO(Long id, String nome, String descrizione) {
            this.id = id;
            this.nome = nome;
            this.descrizione = descrizione;
        }
    }

    public static class RuoloDTO {
        public Long id;
        public String nome;

        public RuoloDTO() {
        }

        public RuoloDTO(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }
    }

    public static class ContattoDTO {
        public Long id;
        public String riferimento;
        public String valore;
        public TipoContattoDTO tipoContatto;

        public ContattoDTO() {
        }

        public ContattoDTO(Long id, String riferimento, String valore) {
            this.id = id;
            this.riferimento = riferimento;
            this.valore = valore;
        }
    }

    public static class TipoContattoDTO {
        public Long id;
        public String nome;

        public TipoContattoDTO() {
        }

        public TipoContattoDTO(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }
    }

    public static class UpdatedByDTO {
        public Long id;
        public String nome;
        public String cognome;

        public UpdatedByDTO() {
        }

        public UpdatedByDTO(Long id, String nome, String cognome) {
            this.id = id;
            this.nome = nome;
            this.cognome = cognome;
        }
    }
}
