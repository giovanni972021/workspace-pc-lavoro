// Common types
export interface CommonEntity {
  updatedBy?: string;
  updatedAt?: string;
}

// Current User DTO
export interface CurrentUser {
  id: number;
  username: string;
  nome: string;
  cognome: string;
  profilo?: string;
  ruoli: string[];
  isSuperUser: boolean;
}

// TipoUtente - User type configuration
export interface TipoUtente {
  id?: number;
  tipo: string;
  descrizione?: string;
  commonEntity?: CommonEntity;
}

// JobTitle
export interface JobTitle {
  id?: number;
  nome: string;
  descrizione?: string;
  commonEntity?: CommonEntity;
}

// Permesso
export interface Permesso {
  id?: number;
  nome: string;
  descrizione?: string;
  commonEntity?: CommonEntity;
}

// Ruolo
export interface Ruolo {
  id?: number;
  nome: string;
  descrizione?: string;
  permessi?: Permesso[];
  commonEntity?: CommonEntity;
}

// Profilo
export interface Profilo {
  id?: number;
  nome: string;
  descrizione?: string;
  ruoli?: Ruolo[];
  commonEntity?: CommonEntity;
}

// TipoContatto
export interface TipoContatto {
  id?: number;
  nome: string;
  descrizione?: string;
  commonEntity?: CommonEntity;
}

// Contatto
export interface Contatto {
  id?: number;
  riferimento: string;
  valore: string;
  tipoContatto?: TipoContatto;
  commonEntity?: CommonEntity;
}

// Utente List DTO - returned by /api/utenti list endpoint
export interface UtenteListDTO {
  id?: number;
  nome: string;
  cognome: string;
  username: string;
  codiceFiscale: string;
  email?: string;
  tipoUtente?: string;
  jobTitle?: string;
  attivo?: boolean;
}

// Utente (User) - full entity
export interface Utente {
  id?: number;
  nome: string;
  cognome: string;
  username: string;
  codiceFiscale: string;
  tipoUtente?: TipoUtente | string;
  jobTitle?: JobTitle | string;
  profilo?: Profilo;
  curriculum?: Curriculum;
  contatti?: Contatto[];
  email?: string;
  dataInizioCollaborazione?: string;
  dataFineCollaborazione?: string;
  valutazione?: number;
  note?: string;
  attivo?: boolean;
  commonEntity?: CommonEntity;
}

// Curriculum
export interface Curriculum {
  id?: number;
  dataNascita?: string;
  utente?: Utente;
  skills?: LevelSkill[];
  softSkills?: LevelSoftSkill[];
  certificazioni?: Certificazione[];
  esperienze?: Esperienza[];
  incarichi?: Incarico[];
  pathRelativoCv?: string;
  commonEntity?: CommonEntity;
}

// Skill
export interface Skill {
  id?: number;
  nome: string;
  descrizione?: string;
  categoria?: string;
  common?: CommonEntity;
}

// LevelSkill
export interface LevelSkill {
  id?: number;
  skill: Skill;
  livello: number;
  common?: CommonEntity;
}

// SoftSkill
export interface SoftSkill {
  id?: number;
  nome: string;
  descrizione?: string;
  common?: CommonEntity;
}

// LevelSoftSkill
export interface LevelSoftSkill {
  id?: number;
  softSkill: SoftSkill;
  livello?: number;
  common?: CommonEntity;
}

// Certificazione
export interface Certificazione {
  id?: number;
  nome: string;
  descrizione?: string;
  conseguita?: string;
  commonEntity?: CommonEntity;
}

// Esperienza
export interface Esperienza {
  id?: number;
  azienda: string;
  ruolo: string;
  dataInizio: string;
  dataFine?: string;
  descrizione?: string;
  attivitaSvolte: string;
  tecnologie?: string;
  common?: CommonEntity;
}

// Incarico
export interface Incarico {
  id?: number;
  progetto?: Progetto;
  ruolo: string;
  dataInizio: string;
  dataFine?: string;
  percentualeImpegno?: number;
  descrizione?: string;
  common?: CommonEntity;
}

// StatoProgetto
export interface StatoProgetto {
  id?: number;
  nome: string;
  descrizione?: string;
  commonEntity?: CommonEntity;
}

// NumberJobTitle
export interface NumberJobTitle {
  id?: number;
  number?: number;
  jobTitle?: JobTitle;
}

// Progetto (Project)
export interface Progetto {
  id?: number;
  nome: string;
  descrizione: string;
  dataInizioPresunta?: string;
  dataInizioEffettiva?: string;
  dataFinePresunta?: string;
  dataFineEffettiva?: string;
  budgetPreventivato?: number;
  budgetEffettivo?: number;
  stato?: StatoProgetto;
  priorita?: number;
  fattibilita?: number;
  numberJobTitles?: NumberJobTitle[];
  commonEntity?: CommonEntity;
}

// Cliente (Client)
export interface Cliente {
  id?: number;
  nome: string;
  descrizione?: string;
  feedbackScore?: number;
  partitaIva?: string;
  codiceFiscale?: string;
  indirizzo?: string;
  citta?: string;
  provincia?: string;
  cap?: string;
  telefono?: string;
  email?: string;
  pec?: string;
  referente?: string;
  progetti?: Progetto[];
  commonEntity?: CommonEntity;
}

// UtenteProgetto - User to Project assignment
export interface UtenteProgetto {
  id?: number;
  utente: Utente;
  progetto: Progetto;
  percentualeImpegno: number;
  dataInizio: string;
  dataFine: string;
  note?: string;
  commonEntity?: CommonEntity;
}

// Gantt DTOs
export interface GanttPeriodDTO {
  dataInizio: string;
  dataFine: string;
  label: string;
}

export interface GanttAllocationDTO {
  utenteProgettoId: number;
  progettoId: number;
  progettoNome: string;
  percentualeImpegno: number;
  note?: string;
  dataInizio: string;
  dataFine: string;
}

export interface GanttCellDTO {
  percentualeTotale: number;
  allocazioni: GanttAllocationDTO[];
}

export interface GanttProgettoRowDTO {
  progettoId: number;
  progettoNome: string;
  periods: GanttCellDTO[];
}

export interface GanttUtenteRowDTO {
  utenteId: number;
  nome: string;
  cognome: string;
  ruoloPredefinito: string;
  periods: GanttCellDTO[];
  progetti: GanttProgettoRowDTO[];
}

export interface GanttResponseDTO {
  periodi: GanttPeriodDTO[];
  utenti: GanttUtenteRowDTO[];
}

// Gantt DTOs for Project Allocation View
export interface GanttUtenteAllocationDTO {
  utenteProgettoId: number;
  utenteId: number;
  utenteNome: string;
  utenteCognome: string;
  percentualeImpegno: number;
  note?: string;
  dataInizio: string;
  dataFine: string;
}

export interface GanttProgettoCellDTO {
  percentualeTotale: number;
  allocazioni: GanttUtenteAllocationDTO[];
  percentualeAllocazione?: number;
}

export interface GanttUtenteAllocatoDTO {
  utenteId: number;
  nome: string;
  cognome: string;
  periods: GanttProgettoCellDTO[];
}

export interface GanttProgettoAllocazioneRowDTO {
  progettoId: number;
  progettoNome: string;
  periods: GanttProgettoCellDTO[];
  utenti: GanttUtenteAllocatoDTO[];
}

export interface GanttProgettiResponseDTO {
  periodi: GanttPeriodDTO[];
  progetti: GanttProgettoAllocazioneRowDTO[];
}

export type GanttGroupBy = 'day' | 'week' | 'month' | 'year';

export interface GanttParams {
  groupBy?: GanttGroupBy;
  startDate?: string;
  periods?: number;
}

// Progetto Allocazione DTO for Dashboard
export interface ProgettoAllocazioneDTO {
  progettoId: number;
  progettoNome: string;
  percentualeAllocazione: number;
  numeroRisorseRichieste: number;
  numeroRisorseAllocate: number;
}

// Search/Filter types
export interface SearchParams {
  page?: number;
  size?: number;
  sort?: string;
  search?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// GitLab Types - Simple DTOs for autocomplete
export interface SimpleUtente {
  id: number;
  nome: string;
  cognome: string;
  email?: string;
}

export interface SimpleProgetto {
  id: number;
  nome: string;
}

export interface GitLabProject {
  id: number;
  name: string;
  nameWithNamespace: string;
  path: string;
  pathWithNamespace: string;
  description?: string;
  webUrl: string;
  defaultBranch?: string;
  visibility: string;
}

export interface UserStats {
  name: string;
  email: string;
  commits: number;
  additions: number;
  deletions: number;
  totalLines: number;
}

export interface UserProjectStats {
  userName: string;
  userEmail: string;
  projectId: number;
  projectName: string;
  commits: number;
  additions: number;
  deletions: number;
  totalLines: number;
}

export interface GitLabStatsResponse {
  userStats: UserStats[];
  userProjectStats: UserProjectStats[];
  startDate: string;
  endDate: string;
  totalProjects: number;
  totalCommits: number;
  totalAdditions: number;
  totalDeletions: number;
  totalLines: number;
}

export interface MergeRequestStats {
  userName: string;
  userEmail: string;
  projectId: number;
  projectName: string;
  opened: number;
  merged: number;
  closed: number;
  total: number;
}

export interface IssueStats {
  userName: string;
  userEmail: string;
  projectId: number;
  projectName: string;
  opened: number;
  closed: number;
  assigned: number;
  total: number;
}
