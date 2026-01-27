import java.time.LocalDate;
import java.util.ArrayList;

public class CampoCalcio extends Campo {

    public CampoCalcio() {
        super(TipoCampo.CALCIO);
    }

    @Override
    protected void inizializzaDisponibilita() {
        for (int giorno = 19; giorno <= 21; giorno++) {
            LocalDate data = LocalDate.of(2025, 12, giorno);
            ArrayList<Disponibilita> slot = new ArrayList<>();
            for (int ora = 10; ora < 13; ora++) {
                slot.add(new Disponibilita(ora, ora + 1));
            }
            disponibilita.put(data, slot);
        }
    }
}
