procedura classe ESEGUI PROGRAMMA
.
.
OBIETTIVO eseguire il programma quindi stampare tutta la lista delle risorse disponibili
.
dichiaro metodo public static VOID MAIN
public static void main(String[] args) {

creo NUOVO OGGETTO
GestoreRisorse gestore = new GestoreRisorse();

aggiungo risorse alle risorse disponibili
gestore.aggiungiRisorsa(new Risorsa("Computer", 10));

STAMPO tutte le risorse disponibili
gestore.stampaRisorseDisponibili();
