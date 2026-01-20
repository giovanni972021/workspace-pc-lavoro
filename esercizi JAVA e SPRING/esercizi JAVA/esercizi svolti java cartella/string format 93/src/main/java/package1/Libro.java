
class Libro {
    String stringa;
    int intero;

    // Costruttore per creare il libro
    public Libro(String t, int a) {
        this.stringa = t;
        this.intero = a;
    }

    // Il metodo toString: trasforma l'oggetto in una frase leggibile
    @Override
    public String toString() {
        return String.format("Stringa di nome %s e intero %d", stringa, intero);
    }
}