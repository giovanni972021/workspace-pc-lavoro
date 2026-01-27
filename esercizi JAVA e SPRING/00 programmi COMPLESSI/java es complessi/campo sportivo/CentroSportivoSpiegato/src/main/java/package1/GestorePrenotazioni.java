import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorePrenotazioni {

    private final Campo basket;
    private final Campo calcio;

    public GestorePrenotazioni() {
        basket = new CampoBasket();
        calcio = new CampoCalcio();
    }

    private Campo getCampo(TipoCampo tipo) {
        return tipo == TipoCampo.BASKET ? basket : calcio;
    }

    public boolean prenota(TipoCampo tipo, LocalDate data, int inizio, int fine, String nome) {
        return getCampo(tipo).prenota(data, inizio, fine, nome);
    }

    public boolean rimuovi(TipoCampo tipo, String nome, LocalDate data, int inizio, int fine) {
        return getCampo(tipo).rimuoviPrenotazione(nome, data, inizio, fine);
    }

    public List<Prenotazione> cercaPerNome(String nome) {
        List<Prenotazione> risultati = new ArrayList<>();
        risultati.addAll(basket.cercaPerNome(nome));
        risultati.addAll(calcio.cercaPerNome(nome));
        return risultati;
    }

    public void stampaDisponibili(TipoCampo tipo) {
        getCampo(tipo).stampaDisponibili();
    }

    public void stampaPrenotate(TipoCampo tipo) {
        getCampo(tipo).stampaPrenotate();
    }
}
