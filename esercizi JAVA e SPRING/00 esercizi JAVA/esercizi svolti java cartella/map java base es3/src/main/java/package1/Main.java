import java.util.Map;

public class Main {

    public static void main(String[] args) {

        UsciteService service = new UsciteService();

        // CODICE RICHIESTO DALL'ESERCIZIO
        Map<String, Integer> usciteDettaglio = service.getDettaglioUscite();

        System.out.println("\n\nDettaglio delle uscite mensili:\n\n");

        for (Map.Entry<String, Integer> voce : usciteDettaglio.entrySet()) {
            System.out.println(voce.getKey() + ": " + voce.getValue() + " euro");
        }

        System.out.println("\n");
    }
}
