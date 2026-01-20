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

    public void ordinaCrescentePerNome() {
        if (!lista.isEmpty()) {
            lista.sort((p1, p2) -> p1.getNome().compareTo(p2.getNome()));
        } else {
            System.out.println("La lista è vuota");
        }
    }

    public void ordinaDecrescentePerNome() {
        if (!lista.isEmpty()) {
            lista.sort((p1, p2) -> p2.getNome().compareTo(p1.getNome()));
        } else {
            System.out.println("La lista è vuota");
        }
    }

    public void ordinaCrescentePerEta() {
        if (!lista.isEmpty()) {
            lista.sort((p1, p2) -> Integer.compare(p1.getEta(), p2.getEta()));
        } else {
            System.out.println("La lista è vuota");
        }
    }

    public void ordinaDecrescentePerEta() {
        if (!lista.isEmpty()) {
            lista.sort((p1, p2) -> Integer.compare(p2.getEta(), p1.getEta()));
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
