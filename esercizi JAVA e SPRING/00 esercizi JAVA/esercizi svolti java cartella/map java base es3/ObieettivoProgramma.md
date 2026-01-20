## OBIETTIVO programma

**stampare lista delle uscite** dove ogni riga contiene
tipo di uscita - importo uscita

## classe USCITE SERVICE

contiene -**definizione della mappa**, che in questo caso è composta da stringhe e da interi
public Map<String, Integer> getDettaglioUscite() {
Map<String, Integer> uscite = new HashMap<>();

-**inserimento** di **valori**
uscite.put("Affitto", 800);

## classe MAIN

**creo oggetto** service
UsciteService service = new UsciteService();

**richiamo la mappa**
Map<String, Integer> usciteDettaglio = service.getDettaglioUscite();

**stampo** tutta la **mappa**
for (Map.Entry<String, Integer> voce : usciteDettaglio.entrySet()) {
System.out.println(voce.getKey() + ": " + voce.getValue() + " euro");
}
