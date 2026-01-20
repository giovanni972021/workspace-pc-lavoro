Per testare a fondo la tua conoscenza delle Liste in Java (in particolare ArrayList), ho preparato un esercizio strutturato come un piccolo progetto di gestione dati. Questo copre la creazione, l'aggiunta, la rimozione, l'ordinamento e la manipolazione tramite Stream.

---

Esercizio: Il Gestore della Biblioteca

Immagina di dover gestire una lista di libri. Ogni libro è un oggetto con: Titolo (String), Autore (String), Prezzo (double) e Anno di Pubblicazione (int).

1. Preparazione
   Crea una classe Libro con i campi sopra elencati, un costruttore, i getter e il metodo toString().
2. Le Sfide (Tutte su una List<Libro>)

Esegui le seguenti operazioni nel tuo metodo main:

OK 1. Inizializzazione: Crea una ArrayList e aggiungi 5 libri a tua scelta. 2. Inserimento Strategico: Aggiungi un nuovo libro in seconda posizione (indice 1).

ok 3. Ricerca: Verifica se un libro con un titolo specifico è presente nella lista (usa contains o un ciclo).

OK 4. Sostituzione: Modifica il prezzo del terzo libro nella lista.

5. Rimozione Condizionale: Rimuovi tutti i libri che costano più di 50 euro (usa removeIf).

6. Ordinamento Naturale: Ordina la lista per Anno di Pubblicazione (dal più vecchio al più nuovo) usando sort e Comparator.
7. Conversione: Trasforma la lista in un Array classico di oggetti Libro.
8. Sotto-lista: Estrai i primi 2 libri della lista ordinata e inseriscili in una nuova subList.

9. Bonus: Stream API (Avanzato)
   • Crea una nuova lista che contenga solo i nomi degli autori in maiuscolo, senza duplicati.
   • Calcola la media dei prezzi di tutti i libri rimasti nella lista.

---

---

Esempio di Struttura Dati
Per aiutarti a visualizzare come funziona la memoria dietro una ArrayList, ricorda che essa si basa su un array dinamico: quando aggiungi un elemento al centro, Java deve "spostare" tutti i successivi.

---
