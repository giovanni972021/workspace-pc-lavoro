/*
1 dichiaro lista di nome lista e di tipo Persona in quanto la classe persona contiene gli attributi della stessa
        private List<Persona> lista;

-
2 creo lista di nome lista
            lista = new ArrayList<>();

3 creo metodo per aggiungere elementi a lista
    public void aggiungiElemento(Persona persona) {

4 creo metodo per ordine CRESCENTE per nome
        public void ordinaCrescentePerNome() {

5 creo metodo per ordine DECRESCENTE per nome
        public void ordinaDecrescentePerNome() {

*/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestoreElenco {

    private List<Persona> lista;

    public GestoreElenco() {
        lista = new ArrayList<>();
    }

    public void aggiungiElemento(Persona persona) {
        if (persona != null) {
            lista.add(persona);
        } else {
            System.out.println("Persona non valida");
        }
    }

    public void ordinaPerEta() {
        lista.sort(Comparator.comparingInt(Persona::getEta));
    }

    public void stampaElementi() {
        if (lista.isEmpty()) {
            System.out.println("La lista è vuota");
        } else {
            lista.forEach(System.out::println);
        }
    }

}
