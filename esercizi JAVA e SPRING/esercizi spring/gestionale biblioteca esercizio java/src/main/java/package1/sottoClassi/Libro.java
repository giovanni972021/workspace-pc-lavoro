package sottoClassi;

import model.Prestito;
import model.Risorsa;

// Sottoclasse Libro

// Sottoclasse Libro

public class Libro extends Risorsa implements Prestito {

    private final int pagineLibro;

    public Libro(String titoloRisorsa, int annoPubblicazione, int pagineLibro) {
        super(titoloRisorsa, annoPubblicazione);
        this.pagineLibro = pagineLibro;
    }

    public int getPagineLibro() {
        return pagineLibro;
    }

    @Override
    public void presta() {
        System.out.println("🔵 Prestito LIBRO:");
        System.out.println(this);
    }

    @Override
    public void restituisci() {
        System.out.println("🟢 Restituzione LIBRO:");
        System.out.println(this);
    }

    @Override
    public String toString() {
        return super.toString() + "  Pagine: " + pagineLibro;
    }
}
