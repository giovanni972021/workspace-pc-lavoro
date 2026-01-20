
public class Main {
    public static void main(String[] args) {
        // Creiamo un'istanza della classe Libro
        Libro mioLibro = new Libro("Il Signore degli Anelli", "J.R.R. Tolkien");

        // Stampiamo l'oggetto: Java chiamerà AUTOMATICAMENTE il metodo toString()
        System.out.println("\n");
        System.out.println(mioLibro);
        System.out.println("\n");

    }
}