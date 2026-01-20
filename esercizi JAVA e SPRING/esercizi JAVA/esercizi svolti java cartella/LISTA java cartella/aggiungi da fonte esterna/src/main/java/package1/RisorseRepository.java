import java.util.ArrayList;
import java.util.List;

public class RisorseRepository {
    public static List<Risorsa> getRisorseIniziali() {
        List<Risorsa> elenco = new ArrayList<>();

        // creo oggetto di nome elenco per poter aggiungere elementi a lista
        elenco.add(new Risorsa("1984", 1949));
        elenco.add(new Risorsa("Il Signore degli Anelli", 1954));
        elenco.add(new Risorsa("Disco ACDC Live", 1980, 120));
        elenco.add(new Risorsa("Inception", 2010, 148));
        return elenco;
    }
}