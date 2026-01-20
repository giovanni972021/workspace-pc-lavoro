class Risorsa {
    private String titolo;
    private int anno;
    private int durata; // Usato se la risorsa è un DVD

    // Costruttore generico (es. per libri)
    public Risorsa(String titolo, int anno) {
        this.titolo = titolo;
        this.anno = anno;
        this.durata = 0;
    }

    // Costruttore specifico (es. per DVD)
    public Risorsa(String titolo, int anno, int durata) {
        this.titolo = titolo;
        this.anno = anno;
        this.durata = durata;
    }

    public void stampaDettagli() {
        String info = "Titolo: " + titolo + " (" + anno + ")";
        if (durata > 0) {
            info += " - Durata DVD: " + durata + " min";
        }
        System.out.println(info);
    }
}