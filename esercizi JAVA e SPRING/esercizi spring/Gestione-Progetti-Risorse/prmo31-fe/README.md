# CTO Manager - Frontend

Frontend React completo per l'applicazione CTO Manager di Schema31, un sistema avanzato di gestione delle risorse umane, progetti e CV.

## Stack Tecnologico

- **React 18** con TypeScript
- **Vite** - Build tool e dev server velocissimo
- **React Router** - Routing con nested routes
- **TanStack Query** - Data fetching e state management
- **Axios** - HTTP client con interceptor
- **Lucide React** - Icone moderne
- **Recharts** - Grafici e statistiche
- **jsPDF** - Export PDF
- **XLSX** - Export Excel
- **Date-fns** - Gestione date

## Installazione

```bash
npm install
cp .env.example .env
npm run dev
```

L'applicazione sarà disponibile su **http://localhost:5173**

## Funzionalità Complete

### 📊 Dashboard
- Statistiche in tempo reale (utenti, progetti, clienti, skills, curricula)
- Grafici a barre per progetti per stato
- Grafici a torta per skills per categoria
- Quick actions per creazione rapida
- Tasso di attività utenti

### 👥 Gestione Utenti
- CRUD completo con form validato
- Ricerca real-time
- Dettaglio utente con tutte le informazioni
- Filtro per stato (attivo/non attivo)
- Gestione dati personali, lavorativi e indirizzo

### 💼 Gestione Progetti
- CRUD completo progetti
- Associazione con clienti
- Gestione budget, date, priorità (0-10)
- Stati: In Corso, Completato, Sospeso, Annullato
- Campi fattibilità e figure richieste

### 🏢 Gestione Clienti
- CRUD completo anagrafica clienti
- P.IVA, CF, contatti (email, telefono, PEC)
- Indirizzo completo
- Referente aziendale

### 🎯 Gestione Skills
- CRUD veloce con modal
- Categorizzazione
- Livelli 0-10 con barra di progresso
- Ricerca e filtro

### 🏆 Gestione Certificazioni
- CRUD con modal
- Ente certificatore
- Date di ottenimento e scadenza
- Descrizioni dettagliate

### 📄 Gestione Curricula (COMPLETO)
- CRUD completo curriculum
- Associazione con utente
- Gestione skills con selezione multipla
- Gestione certificazioni
- **Esperienze lavorative** con timeline:
  - Azienda, ruolo, periodo
  - Tecnologie utilizzate
  - Descrizione dettagliata
- **Export PDF** professionale con logo
- **Export Excel** multi-sheet
- Visualizzazione dettagliata con:
  - Skills con livelli e barre di progresso
  - Certificazioni con date
  - Timeline esperienze lavorative
  - Soft skills

### 🔍 Ricerca e Filtri
- **Ricerca real-time** con debouncing su tutte le entità
- **Filtri avanzati** (componente riutilizzabile):
  - Filtri per testo, numero, data, select, boolean
  - Badge conteggio filtri attivi
  - Reset rapido filtri
  - Modal per gestione filtri complessi

### 📊 Paginazione
- **Paginazione server-side** completa:
  - Navigazione prima/ultima pagina
  - Navigazione pagina precedente/successiva
  - Selezione numero elementi per pagina (10, 20, 50, 100)
  - Info "Visualizzati X-Y di Z risultati"
  - Numerazione pagine con ellipsis

### 📤 Export Dati
- **Export PDF**:
  - Curriculum completi con formattazione professionale
  - Logo e intestazione personalizzata
  - Tabelle per skills e certificazioni
  - Timeline esperienze
- **Export Excel**:
  - Multi-sheet (Info, Skills, Certificazioni, Esperienze)
  - Formattazione celle
  - Compatibilità totale con Excel

### 📁 Upload File
- **Componente FileUpload** completo:
  - Drag & drop
  - Click per selezione
  - Validazione dimensione (configurabile)
  - Limite numero file
  - Progress bar per upload
  - Preview file caricati
  - Rimozione file
  - Formati personalizzabili

### 🌗 Dark Mode
- **Tema scuro/chiaro** completo:
  - Toggle nel header
  - Persistenza con localStorage
  - Variabili CSS per tutti i colori
  - Transizioni smooth
  - Supporto completo in tutti i componenti
  - Colori Schema31 ottimizzati per entrambi i temi

### 🎨 UI/UX
- Design moderno con Schema31 branding
- Sidebar collassabile
- Menu laterale con icone
- Logo Schema31 in SVG
- Responsive design
- Transizioni smooth
- Badge e tag colorati
- Card e modal pulite
- Tabelle interattive

## Componenti Riutilizzabili

- **Table**: Tabella generica con sorting e azioni
- **SearchBar**: Ricerca con debouncing
- **Button**: Bottone con varianti e icone
- **Modal**: Dialog modale riutilizzabile
- **Pagination**: Paginazione completa
- **AdvancedFilters**: Sistema filtri avanzati
- **FileUpload**: Upload file con drag & drop

## Sviluppo

```bash
# Avvia dev server
npm run dev

# Build produzione
npm run build

# Preview build
npm run preview

# Lint
npm run lint
```

## Configurazione

File `.env`:
```env
VITE_API_BASE_URL=http://localhost:8080
```

## Integrazione Backend

L'applicazione si integra perfettamente con il backend Quarkus CTO Manager.

Assicurati che il backend sia in esecuzione su **http://localhost:8080**

## Struttura Progetto

```
src/
├── components/       # Componenti riutilizzabili
├── contexts/         # React contexts (Theme)
├── layouts/          # Layout principale
├── pages/            # Pagine per entità
├── services/         # Servizi API
├── styles/           # CSS temi
├── types/            # TypeScript types
└── utils/            # Utility (PDF, Excel export)
```

## Licenza

Proprietario di Schema31
