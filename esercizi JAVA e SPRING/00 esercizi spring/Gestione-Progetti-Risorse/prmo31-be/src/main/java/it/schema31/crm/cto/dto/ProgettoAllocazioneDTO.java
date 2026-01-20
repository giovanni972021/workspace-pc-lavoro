package it.schema31.crm.cto.dto;

public class ProgettoAllocazioneDTO {
    public Long progettoId;
    public String progettoNome;
    public double percentualeAllocazione;
    public int numeroRisorseRichieste;
    public int numeroRisorseAllocate;

    public ProgettoAllocazioneDTO() {
    }

    public ProgettoAllocazioneDTO(Long progettoId, String progettoNome, double percentualeAllocazione,
                                   int numeroRisorseRichieste, int numeroRisorseAllocate) {
        this.progettoId = progettoId;
        this.progettoNome = progettoNome;
        this.percentualeAllocazione = percentualeAllocazione;
        this.numeroRisorseRichieste = numeroRisorseRichieste;
        this.numeroRisorseAllocate = numeroRisorseAllocate;
    }
}
