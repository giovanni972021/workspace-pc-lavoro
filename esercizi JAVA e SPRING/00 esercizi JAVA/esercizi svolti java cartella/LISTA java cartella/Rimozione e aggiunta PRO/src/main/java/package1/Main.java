public class Main {
    public static void main(String[] args) {
        Biblioteca miaBiblioteca = new Biblioteca();

        // Creazione risorse (DVD e Libri usano la stessa classe)
        Risorsa dvd1 = new Risorsa("Disco ACDC", 1980, 12);
        Risorsa libro1 = new Risorsa("1984", 1949);

        // Aggiunta alla lista disponibili
        miaBiblioteca.aggiungi(dvd1);
        miaBiblioteca.aggiungi(libro1);

        // Visualizzazione
        miaBiblioteca.mostraCatalogo();

        // Rimozione
        System.out.println("\n... Rimozione di Disco ACDC ...");
        miaBiblioteca.rimuovi(dvd1);

        // Visualizzazione finale
        miaBiblioteca.mostraCatalogo();
        System.out.println("\n");
    }
}