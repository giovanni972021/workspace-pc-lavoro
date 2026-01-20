import java.util.List;

public class Main {
    public static void main(String[] args) {
        ListaManager manager = new ListaManager();

        // Aggiungo qualche frutto in più
        manager.aggiungiFrutta(new Frutta("Banana", 5));
        manager.aggiungiFrutta(new Frutta("Arancia", 8));

        List<Frutta> frutti = manager.getFrutti();

        ListaManager.stampaLista(frutti);
    }
}
