public class Risorsa {
    private String titolo;
    private int anno;
    private int durata;

    public Risorsa(String titolo, int anno) {
        this.titolo = titolo;
        this.anno = anno;
        this.durata = 0;
    }

    public Risorsa(String titolo, int anno, int durata) {
        this.titolo = titolo;
        this.anno = anno;
        this.durata = durata;
    }

    public String getTitolo() {
        return titolo;
    }

    public void stampaDettagli() {
        System.out.println("titolo " + titolo + " anno " + anno + " durata " + durata);
    }
}