import java.util.ArrayList;
import java.util.List;

public class Archivio {
    // dichiaro lista FUORI da costruttore di nome nomi e di tipo String, quindi
    // questa lista conterra
    // SOLO stringhe
    private List<String> nomi;

    public Archivio() {
        // creo lista DENTRO costruttore
        nomi = new ArrayList<>();
        // nomi è il nome della lista
        nomi.add("Mario");
        nomi.add("Luigi");
    }

    // Metodo per ottenere la lista, GET della lista
    public List<String> getNomi() {
        return nomi;
    }
}
