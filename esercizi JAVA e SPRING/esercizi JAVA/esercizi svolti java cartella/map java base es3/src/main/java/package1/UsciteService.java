import java.util.HashMap;
import java.util.Map;

public class UsciteService {

    public Map<String, Integer> getDettaglioUscite() {
        Map<String, Integer> uscite = new HashMap<>();

        uscite.put("Affitto", 800);
        uscite.put("Spesa", 250);
        uscite.put("Trasporti", 120);
        uscite.put("Bollette", 180);

        return uscite;
    }
}
