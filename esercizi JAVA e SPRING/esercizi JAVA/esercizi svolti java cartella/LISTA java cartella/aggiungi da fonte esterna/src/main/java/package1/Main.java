import java.util.List;

public class Main {
    public static void main(String[] args) {
        Biblioteca miaBiblioteca = new Biblioteca();

        // l'oggetto dati esterni richiama tutti gli elementi aggiunti alla lista nella
        // classe risorse repository
        List<Risorsa> datiEsterni = RisorseRepository.getRisorseIniziali();
        miaBiblioteca.aggiungiTutte(datiEsterni);

        miaBiblioteca.mostraCatalogo();

        if (!datiEsterni.isEmpty()) {
            Risorsa daRimuovere = datiEsterni.get(0);
            System.out.println("Rimuovo: " + daRimuovere.getTitolo());
            miaBiblioteca.rimuovi(daRimuovere);
        }

        miaBiblioteca.mostraCatalogo();
    }
}