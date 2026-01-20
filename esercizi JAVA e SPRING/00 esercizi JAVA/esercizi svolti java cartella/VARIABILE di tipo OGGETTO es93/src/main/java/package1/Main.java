package main.java.package1;

public class Main {

    public static void main(String[] args) {

        // VARIABILE DI TIPO OGGETTO (dichiarazione)
        // variabile di nome persona di tipo Persona
        // Persona è la classe dove dichiaro tutte le caratteristiche della persona
        Persona persona;

        // Creazione dell'oggetto e assegnazione alla variabile
        persona = new Persona("Mario", 16);

        // Uso della variabile oggetto
        System.out.println("Nome: " + persona.nome);
        System.out.println("Età: " + persona.eta);
    }
}
