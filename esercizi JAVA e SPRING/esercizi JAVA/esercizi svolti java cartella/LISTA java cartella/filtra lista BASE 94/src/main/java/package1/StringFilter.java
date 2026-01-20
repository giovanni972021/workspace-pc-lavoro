package main.java.package1;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class StringFilter {

    private StringFilter() {
        // Costruttore privato: classe utility
    }

    /**
     * Filtra una lista di stringhe restituendo
     * solo i nomi che iniziano con la lettera "R".
     *
     * @param nomi lista di nomi
     * @return lista filtrata
     */
    public static List<String> filtraNomiCheInizianoConR(List<String> nomi) {
        Objects.requireNonNull(nomi, "La lista non può essere null");

        return nomi.stream()
                // Elimina eventuali valori null
                .filter(Objects::nonNull)

                // Tiene solo le stringhe che iniziano con "R"
                .filter(nome -> nome.startsWith("R"))

                // Raccoglie il risultato in una nuova lista
                .collect(Collectors.toList());
    }
}
