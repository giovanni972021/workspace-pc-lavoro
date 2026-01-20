public class Main {
    public static void main(String[] args) {
        Archivio archivio = new Archivio();

        // Recupero la lista di stringhe
        // stampa tutta la lista
        for (String nome : archivio.getNomi()) {
            System.out.println(nome);
        }
    }
}
