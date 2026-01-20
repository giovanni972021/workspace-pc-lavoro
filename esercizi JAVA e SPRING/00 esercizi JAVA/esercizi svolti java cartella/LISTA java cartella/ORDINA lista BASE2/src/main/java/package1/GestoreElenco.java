import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestoreElenco {

    private List<String> lista;

    public GestoreElenco() {
        lista = new ArrayList<>();
    }

    public void aggiungiElemento(String elemento) {
        if (elemento != null && !elemento.trim().isEmpty()) {
            lista.add(elemento);
        } else {
            System.out.println("Elemento non valido");
        }
    }

    public void ordinaCrescente() {
        if (!lista.isEmpty()) {
            Collections.sort(lista);
        } else {
            System.out.println("La lista è vuota");
        }
    }

    public void ordinaDecrescente() {
        if (!lista.isEmpty()) {
            Collections.sort(lista, Collections.reverseOrder());
        } else {
            System.out.println("La lista è vuota");
        }
    }

    public void stampaElementi() {
        if (lista.isEmpty()) {
            System.out.println("La lista è vuota");
        } else {
            lista.forEach(System.out::println);
        }
    }

}
