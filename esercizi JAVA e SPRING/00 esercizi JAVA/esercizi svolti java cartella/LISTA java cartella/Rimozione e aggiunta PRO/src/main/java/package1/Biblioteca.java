import java.util.ArrayList;
import java.util.List;

class Biblioteca {
    private List<Risorsa> disponibili = new ArrayList<>();

    public void aggiungi(Risorsa r) {
        disponibili.add(r);
    }

    public void rimuovi(Risorsa r) {
        disponibili.remove(r);
    }

    

    public void mostraCatalogo() {
        System.out.println("\n--- RISORSE DISPONIBILI IN BIBLIOTECA ---");
        if (disponibili.isEmpty()) {
            System.out.println("Nessuna risorsa presente.");
        } else {
            for (Risorsa r : disponibili) {
                r.stampaDettagli();
            }
        }
    }
}