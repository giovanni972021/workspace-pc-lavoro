import java.util.List;

//classe x STAMPARE lista
// Classe x stampare programma

public class Main {
    public static void main(String[] args) {
        // Creiamo un oggetto di nome manager legato a classe ListaManager
        ListaManager manager = new ListaManager();

        // salvo nella variabile lista di tipo list string il risultato del metodo get
        // elementi
        List<String> elementi = manager.getElementi();

        // Stampiamo la lista usando il metodo stampaLista
        ListaManager.stampaLista(elementi);
    }
}
