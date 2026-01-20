import java.time.LocalDate;
import java.util.ArrayList;

public class CampoBasket extends Campo {

    public CampoBasket() {
        super(TipoCampo.CALCIO);
    }

    @Override
    protected void inizializzaDisponibilita() {
        for (int giorno = 10; giorno <= 15; giorno++) {
            LocalDate data = LocalDate.of(2025, 12, giorno);

            // crea lista slot dove salvare orari
            ArrayList<Disponibilita> slot = new ArrayList<>();

            // se ora compresa tra 10 e 13 allora aggiungi disponibilita alla lista di nome
            // slot
            for (int ora = 10; ora < 13; ora++) {
                slot.add(new Disponibilita(ora, ora + 1));
            }
            disponibilita.put(data, slot);
        }
    }
}
