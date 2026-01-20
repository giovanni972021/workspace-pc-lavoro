import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrdineService {
    private List<Ordine> lista;

    public OrdineService() {
        // Simuliamo il caricamento dati (es. da un database)
        this.lista = new ArrayList<>();
        caricaDatiEsempio();
    }

    private void caricaDatiEsempio() {
        lista.add(new Ordine("ORD-001", 150.0, "SPEDITO"));
        lista.add(new Ordine("ORD-002", 45.0, "SPEDITO"));
        lista.add(new Ordine("ORD-003", 200.0, "IN_ATTESA"));
        lista.add(new Ordine("ORD-004", 310.0, "SPEDITO"));
    }

    /**
     * Il modo più professionale: Metodo dedicato al filtraggio
     * 
     * @param sogliaPrezzo Il prezzo minimo per il filtro
     * @param statoFiltro  Lo stato richiesto (es. "SPEDITO")
     */
    public List<Ordine> filtraOrdini(double sogliaPrezzo, String statoFiltro) {
        return lista.stream()
                .filter(o -> o.getStato().equalsIgnoreCase(statoFiltro))
                .filter(o -> o.getImporto() > sogliaPrezzo)
                .collect(Collectors.toList());
    }

    public List<Ordine> getTutti() {
        return new ArrayList<>(lista); // Ritorna una copia per sicurezza
    }
}