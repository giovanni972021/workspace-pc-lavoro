package main.java.package1;

//CLASSE DATA
public class DataNascita {
    /*
     * creo variabili private giorno mese e anno
     * esempio variabile privata start, 0variabile privata esempio start
     */
    private int giorno;
    private int mese;
    private int anno;
    private String nome;

    // costruttore vuoto
    public DataNascita() {

    }

    // costruttore con parametri
    DataNascita(int giorno, int mese, int anno, String nome) {
        setGiorno(giorno);
        setMese(mese);
        setAnno(anno);
        setNome(nome);
    }

    // getGiorno: metodo che richiama una variabile di nome giorno di tipo int
    public int getGiorno() {
        return this.giorno;
    }

    // setGiorno: metodo che assegna un valore di tipo intero ad una variabile di
    // nome giorno
    public void setGiorno(int giorno) {
        // se maggiore di 0 E minore uguale a 31
        if (giorno > 0 && giorno <= 31) {
            this.giorno = giorno;
        } else {
            System.out.println("giorno non valido");
        }
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMese() {
        return this.mese;
    }

    public void setMese(int mese) {
        if (mese > 0 && mese <= 12) {
            this.mese = mese;
        } else {
            System.out.println("mese non valido");
        }
    }

    public int getAnno() {
        return this.anno;
    }

    public void setAnno(int anno) {
        if (anno > 0) {
            this.anno = anno;
        } else {
            System.out.println("anno non valido");
        }
    }

    /*
     * metodo di nome DifferenzaAnni che ricevera in pasto una variabile di nome
     * altraData_nascita di tipo DataNascita
     * DataNascita : classe a cui si riferisce oggetto altraData_nascita ; anno :
     * attributo della classe data nascita
     */

    public int differenzaAnni(DataNascita altraData_nascita) {
        return this.anno - altraData_nascita.getAnno();
    }
}
