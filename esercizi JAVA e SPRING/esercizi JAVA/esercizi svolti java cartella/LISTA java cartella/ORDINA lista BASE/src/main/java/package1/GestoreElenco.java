import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestoreElenco {

    private List<String> lista;

    public GestoreElenco() {
        lista = new ArrayList<>();
    }

    public void aggiungiElemento(String elemento) {
        lista.add(elemento);
    }

    // Metodo ORDINA elementi
    public void ordina_crescente() {
        Collections.sort(lista);
    }

    public void ordina_decrescente() {
        Collections.sort(lista, Collections.reverseOrder());
    }

    // Metodo per stampare tutti gli elementi
    public void stampaElementi() {
        for (String elemento : lista) {
            System.out.println(elemento);
        }
    }
}
