nella classe astratta campo definisco **COMPORTAMENTI E CARATTERISTICHE COMUNI** a tutti i campi

## dichiaro VARIABILI

## tipo campo

vedi su google drive "variabile di tipo oggetto start"
dichiaro variabile di nome tipoCampo di tipo **TipoCampo**
**TipoCampo** è il nome della classe dentro cui scrivo tutte le caratteristiche del campo

    protected final TipoCampo tipoCampo;

## disponibilita

vedi su google drive **"map start"**
dichiaro una variabile di nome disponibilita di tipo **Map<LocalDate, List<Disponibilita>>**
**Map<LocalDate, List<Disponibilita>>** prende come chiave localdate e come valore la lista di tipo Disponibilita

**Disponibilita** è la classe che definisce tutte le caratteristiche di una prenotazione disponibile

    protected final Map<LocalDate, List<Disponibilita>> disponibilita;

## prenotazioni

dichiaro lista di nome prenotazioni di tipo prenotazione ,

vedi su google drive **"lista di oggetti start"**
protected final List<Prenotazione> prenotazioni;
