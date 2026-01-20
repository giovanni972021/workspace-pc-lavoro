# CTO Manager Frontend - Funzionalità Implementate

## Panoramica

Applicazione React TypeScript per la gestione completa delle risorse umane e progetti di Schema31.

## Architettura

### Layout e Navigazione

- **MainLayout**: Layout principale con sidebar collassabile
- **Logo Schema31**: Logo aziendale personalizzato in SVG
- **Menu laterale**: Navigazione tra le sezioni principali
- **Titolo applicazione**: "Gestione delle risorse e progetti"

### Componenti Riutilizzabili

1. **Table**: Tabella generica con supporto TypeScript
   - Click su riga per dettagli
   - Colonne personalizzabili con render function
   - Stati di loading e empty

2. **SearchBar**: Barra di ricerca con debouncing
   - Icona lucide-react
   - Placeholder personalizzabile

3. **Button**: Bottone con varianti
   - Varianti: primary, secondary, danger
   - Supporto icone
   - Stati disabled

4. **Modal**: Dialog modale
   - Header con titolo e pulsante chiusura
   - Body scrollabile
   - Footer personalizzabile

## Gestione Entità

### 1. Utenti (Utente)

**Pagine**:
- `UtentiList`: Lista con ricerca e azioni
- `UtenteForm`: Form creazione/modifica
- `UtenteDetail`: Visualizzazione dettaglio

**Campi gestiti**:
- Dati personali: nome, cognome, email, telefono, data nascita, CF
- Dati lavorativi: ruolo, data assunzione, stato attivo
- Indirizzo completo: via, città, provincia, CAP

**Funzionalità**:
- Ricerca real-time per nome, cognome, email
- Badge colorato per stato attivo/non attivo
- Form multi-sezione con validazione

### 2. Progetti (Progetto)

**Pagine**:
- `ProgettiList`: Lista progetti
- `ProgettoForm`: Creazione/modifica

**Campi gestiti**:
- Info generali: codice, nome, descrizione, stato
- Date: inizio, fine prevista, fine effettiva
- Gestione: budget, priorità (0-10)
- Analisi: fattibilità, figure richieste
- Relazioni: cliente associato

**Funzionalità**:
- Selezione cliente da dropdown
- Gestione stati progetto (In Corso, Completato, Sospeso, Annullato)
- Formattazione budget in Euro

### 3. Clienti (Cliente)

**Pagine**:
- `ClientiList`: Lista clienti

**Campi gestiti**:
- Ragione sociale, P.IVA, Codice Fiscale
- Contatti: email, telefono, PEC
- Indirizzo completo
- Referente aziendale

**Funzionalità**:
- Ricerca clienti
- Visualizzazione dati aziendali

### 4. Skills

**Pagine**:
- `SkillsList`: Lista con modal per CRUD

**Campi gestiti**:
- Nome, descrizione
- Categoria
- Livello (0-10)

**Funzionalità**:
- Modal per creazione/modifica rapida
- Categorizzazione skills
- Livello di competenza

### 5. Certificazioni

**Pagine**:
- `CertificazioniList`: Lista con modal

**Campi gestiti**:
- Nome certificazione
- Ente certificatore
- Data ottenimento, data scadenza
- Descrizione

**Funzionalità**:
- Gestione date di validità
- Modal per CRUD veloce

## Servizi API

Ogni entità ha il proprio service con metodi standard:

```typescript
- getAll(params?: SearchParams): Promise<T[]>
- getById(id: number): Promise<T>
- create(entity: T): Promise<T>
- update(id: number, entity: T): Promise<T>
- delete(id: number): Promise<void>
- search(searchTerm: string): Promise<T[]>
```

## Configurazione API

- **Client Axios**: Configurato con interceptor per autenticazione
- **Base URL**: Configurabile via `VITE_API_BASE_URL`
- **Token Management**: JWT in localStorage
- **Error Handling**: Redirect automatico su 401

## Styling

- **Colori primari**: 
  - Schema31 teal: `#00d4aa`
  - Background: `#f5f7fa`
  - Sidebar: Gradient `#1a1a2e` → `#16213e`

- **Layout responsive**: Grid CSS e flexbox
- **Transizioni**: Smooth animations su hover e focus
- **Tipografia**: System fonts per performance

## TypeScript

Tipi completi per:
- Tutte le entità del backend
- Props dei componenti
- Parametri di ricerca
- Risposte paginate

## React Router

Routing completo con:
- Layout nesting
- Route parametriche (:id)
- Navigate redirect
- Route protette (ready per auth)

## TanStack Query

- **QueryClient** configurato
- Cache management
- Retry policy
- Refetch disabilitato su window focus

## Prossimi Sviluppi

1. **Autenticazione OIDC**: Integrazione completa con is.schema31.it
2. **Curriculum CRUD**: Interfaccia completa per gestione CV
3. **Dashboard**: Statistiche e grafici
4. **Export**: Esportazione dati in PDF/Excel
5. **Filtri avanzati**: Filtri multipli per le liste
6. **Paginazione**: Supporto server-side pagination
7. **Upload file**: Gestione upload CV e documenti
8. **Dark mode**: Tema scuro opzionale

## Note Tecniche

- **Vite HMR**: Hot Module Replacement per sviluppo rapido
- **ESLint**: Configurazione TypeScript
- **Code splitting**: Lazy loading delle route
- **Tree shaking**: Build ottimizzato per produzione
