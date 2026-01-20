package main.java.package1;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class IntegerFilter {

    private IntegerFilter() {
    }

    public static List<Integer> filtraNumeriPari(List<Integer> valori) {
        Objects.requireNonNull(valori, "La lista non può essere null");

        return valori.stream()
                .filter(Objects::nonNull)
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
    }
}
