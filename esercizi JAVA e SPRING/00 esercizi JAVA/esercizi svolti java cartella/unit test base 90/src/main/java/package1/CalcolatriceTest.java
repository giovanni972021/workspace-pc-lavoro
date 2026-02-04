// File: CalcolatriceTest.java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CalcolatriceTest {

    @Test
    @DisplayName("Test della somma: 2 + 2 deve fare 4")
    void testSommaSemplice() {
        // 1. ARRANGE (Prepara)
        Calcolatrice calc = new Calcolatrice();

        // 2. ACT (Esegui)
        int risultato = calc.somma(2, 2);

        // 3. ASSERT (Verifica)
        // Se risultato è 4, compare il verde. Se è diverso, "allarme"!
        assertEquals(4, risultato, "ERRORE: La somma di 2+2 non fa 4!");
    }

    @Test
    @DisplayName("Test limite: Divisione per zero")
    void testDivisionePerZero() {
        Calcolatrice calc = new Calcolatrice();

        // Verifichiamo che il programma "lanci l'allarme" (eccezione) 
        // se proviamo a dividere per zero
        assertThrows(IllegalArgumentException.class, () -> {
            calc.divisione(10, 0);
        });
    }
}