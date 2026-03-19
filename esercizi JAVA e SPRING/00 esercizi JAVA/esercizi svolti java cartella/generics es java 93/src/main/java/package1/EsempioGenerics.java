public class EsempioGenerics {
  public static void main(String[] args) {

    // Creiamo una scatola per i numeri (Integer)
    Scatola<Integer> scatolaNumerica = new Scatola<>();
    scatolaNumerica.inserisci(123);
    Integer numero = scatolaNumerica.estrai();
    System.out.println("\nContenuto scatola numerica: " + numero + "\n");

    // Creiamo una scatola per il testo (String)
    Scatola<String> scatolaTesto = new Scatola<>();
    scatolaTesto.inserisci("Ciao Java!");
    String testo = scatolaTesto.estrai();
    System.out.println("\nContenuto scatola testo: " + testo + "\n");
  }
}