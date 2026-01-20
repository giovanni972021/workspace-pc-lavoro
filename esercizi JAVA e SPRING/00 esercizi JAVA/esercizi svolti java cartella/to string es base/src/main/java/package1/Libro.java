
class Libro {
    String titolo;
    String autore;

    // Costruttore per creare il libro
    public Libro(String t, String a) {
        this.titolo = t;
        this.autore = a;
    }

    // Il metodo toString: trasforma l'oggetto in una frase leggibile
    @Override
    public String toString() {
        return "Libro: " + titolo + " - Scritto da: " + autore;
    }
}