COME TESTARE PROGRAMMA?
.
scrivi nel terminale
./mvnw spring-boot:run

### 1. GET mapping LEGGI dati

- **Obiettivo:** Vedere la lista dei giochi.

apri chrome e incolla url

- **URL:** `http://localhost:8080/giochi/tutti`

SE TUTTO OK vedrai apparire il JSON: `["Elden Ring", "FIFA", "Zelda"]`.

- **Perché:** Stai solo chiedendo dati, non stai inviando nulla di nuovo.

### 2. POST mapping AFGGIUNGI dati

- **Obiettivo:** Aggiungere un gioco (es. "Halo") alla lista.
- **Strumento:** cerca su chrome
  chrome-extension://aejoelaoggembcahagimdiliamlcdmfm/index.html#requests

- **URL:**
  http://localhost:8080/giochi/aggiungi

- **Metodo:** Seleziona **POST**.

COME aggiungo SEPARATAMENTE i giochi "gioco2, gioco3" ecc?

nel **body** scrivi
["gioco1", "gioco2", "gioco3"]

in alto a dx premi su send
SE TUTTO ok

nel web ricarico la pagina
http://localhost:8080/giochi/tutti

e vedo i giochi appena aggiunti ossia gioco1, gioco2 ecc

### 3. PUT mapping AGGIORNA UN dato CON INDICE

- **Obiettivo:** Cambiare il nome di un gioco esistente.
  su chrome vai su
  chrome-extension://aejoelaoggembcahagimdiliamlcdmfm/index.html#requests

- **URL:** `

http://localhost:8080/giochi/modifica/1

`(Il numero`1` è l'indice del gioco che vuoi cambiare ossia il 2o da sinistra

- **Metodo:** Seleziona **PUT**.

1. Vai su **Body** -> **raw**.
2. Scrivi il nuovo nome:

GTA VI

SE tutto ok cerco
http://localhost:8080/giochi/tutti
e vedo modifica

### 4. DELETE mapping per ELIMINARE

- **Obiettivo:** Cancellare un gioco.
- **URL:** `

http://localhost:8080/giochi/elimina/0

` (Cancella il primo gioco, Elden Ring).

- **Metodo:** Seleziona **DELETE**.
- **body vuoto** Per cancellare non serve inviare dati nel corpo, basta l'ID nell'URL.

SE tutto ok cerco
http://localhost:8080/giochi/tutti
e vedo modifica
