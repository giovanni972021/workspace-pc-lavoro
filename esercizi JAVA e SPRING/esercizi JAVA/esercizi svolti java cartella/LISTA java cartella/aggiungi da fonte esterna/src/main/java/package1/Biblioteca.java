import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private List<Risorsa> disponibili = new ArrayList<>();

    public void aggiungiTutte(List<Risorsa> risorse) {
        disponibili.addAll(risorse);
    }

    public void rimuovi(Risorsa r) {
        disponibili.remove(r);
    }

    public void mostraCatalogo() {
        System.out.println("\n--- CATALOGO BIBLIOTECA ---");
        if (disponibili.isEmpty()) {
            System.out.println("La biblioteca è vuota.");
        } else {
            for (Risorsa r : disponibili) {
                r.stampaDettagli();
            }
        }
        System.out.println("---------------------------\n");
    }
}