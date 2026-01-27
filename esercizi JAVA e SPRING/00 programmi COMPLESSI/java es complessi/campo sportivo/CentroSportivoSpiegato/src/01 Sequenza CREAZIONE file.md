PROCEDURA CREAZIONE FILE JAVA

### **1️⃣TIPO DI CAMPO `TipoCampo.java`**

Quali sono i TIPI DI CAMPO disponibili o prenotabili?

- Definisce i tipi di campo disponibili (`BASKET`, `CALCIO`)
- Serve a più classi, quindi va creato subito

### **2️⃣DISPONIBILITA del campo `Disponibilita.java`**

👉 **Modello di base**

QUANDO è prenotabile un campo?

- Rappresenta una fascia oraria disponibile

### **3️⃣ `Prenotazione.java`**

👉 **Entità principale**

quali CARATTERISTICHE deve avere ogni prenotazione?

- ora inizio , ora fine, data ecc
- Rappresenta una prenotazione reale
- Dipende da `TipoCampo`

### **4️⃣ `Campo.java`**

-CARATTERISTICHE di ogni campo come tipo di campo
-COMPORTAMENTO di ogni campo come prenotabile disponibile ecc

👉 **Classe astratta (cuore del progetto)**

- Definisce il comportamento comune dei campi
- Usa `Disponibilita`, `Prenotazione` e `TipoCampo`

---

### **5️⃣ `CampoBasket.java`**

caratteristiche e camportamenti SPECIFICHE del campo basket
👉 **Specializzazione**

- Estende `Campo`
- Implementa le disponibilità del campo da basket

---

### **6️⃣ `CampoCalcio.java`**

caratteristiche e camportamenti SPECIFICHE del campo calcio

👉 **Specializzazione**

- Estende `Campo`
- Implementa le disponibilità del campo da calcio

---

### **7️⃣ `GestorePrenotazioni.java`**

- GESTIONE delle prenotazioni
  👉 **Logica applicativa**

- Coordina i campi
- Fornisce i servizi usati dal programma principale

---

### **8️⃣ `EseguiProgramma.java`**

- ESEGUI il programma
  👉 **Per ultimo**

- Contiene il `main`
- Gestisce input/output
- Usa tutte le altre classi
