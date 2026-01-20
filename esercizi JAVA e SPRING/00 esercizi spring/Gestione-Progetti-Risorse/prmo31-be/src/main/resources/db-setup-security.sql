-- ============================================================================
-- Script di Setup per Sistema di Sicurezza
-- ============================================================================
-- Questo script crea le entità necessarie per il sistema di sicurezza JWT:
-- - Profilo SUPER_USER
-- - Ruoli base
-- - Utente di esempio con profilo SUPER_USER
--
-- IMPORTANTE: Modifica il valore 'username_dal_jwt' con la claim 'sub'
--             del tuo token JWT prima di eseguire lo script!
-- ============================================================================

-- Imposta lo schema
SET search_path TO crm;

-- ============================================================================
-- 1. CREAZIONE PROFILI
-- ============================================================================

-- Profilo SUPER_USER (accesso completo)
INSERT INTO profilo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'SUPER_USER',
    'Super amministratore con accesso completo a tutte le funzionalità del sistema',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- Profilo ADMIN (amministratore)
INSERT INTO profilo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'ADMIN',
    'Amministratore con privilegi elevati',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- Profilo MANAGER (manager)
INSERT INTO profilo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'MANAGER',
    'Manager con accesso alle funzionalità di gestione',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- Profilo HR (risorse umane)
INSERT INTO profilo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'HR',
    'Risorse Umane con accesso alla gestione del personale',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- Profilo USER (utente standard)
INSERT INTO profilo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'USER',
    'Utente standard con accesso limitato',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- ============================================================================
-- 2. CREAZIONE RUOLI (opzionale)
-- ============================================================================
-- I ruoli possono essere usati per controlli di accesso più granulari
-- Gli utenti SUPER_USER non necessitano di ruoli aggiuntivi

-- Ruolo per gestione utenti
INSERT INTO ruolo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'GESTIONE_UTENTI',
    'Permesso per gestire gli utenti del sistema',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- Ruolo per gestione progetti
INSERT INTO ruolo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'GESTIONE_PROGETTI',
    'Permesso per gestire i progetti',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- Ruolo per gestione clienti
INSERT INTO ruolo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'GESTIONE_CLIENTI',
    'Permesso per gestire i clienti',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- Ruolo per visualizzazione
INSERT INTO ruolo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'SOLO_LETTURA',
    'Permesso di sola lettura',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- ============================================================================
-- 3. ASSOCIAZIONE RUOLI AI PROFILI (opzionale)
-- ============================================================================
-- Esempio: Associa alcuni ruoli al profilo ADMIN

-- ADMIN ha tutti i ruoli tranne quello di SUPER_USER
INSERT INTO profilo_ruolo (profilo_id, ruoli_id)
SELECT
    p.id,
    r.id
FROM profilo p
CROSS JOIN ruolo r
WHERE p.nome = 'ADMIN'
  AND r.nome IN ('GESTIONE_UTENTI', 'GESTIONE_PROGETTI', 'GESTIONE_CLIENTI')
ON CONFLICT DO NOTHING;

-- MANAGER ha accesso a progetti e clienti
INSERT INTO profilo_ruolo (profilo_id, ruoli_id)
SELECT
    p.id,
    r.id
FROM profilo p
CROSS JOIN ruolo r
WHERE p.nome = 'MANAGER'
  AND r.nome IN ('GESTIONE_PROGETTI', 'GESTIONE_CLIENTI')
ON CONFLICT DO NOTHING;

-- HR ha accesso alla gestione utenti
INSERT INTO profilo_ruolo (profilo_id, ruoli_id)
SELECT
    p.id,
    r.id
FROM profilo p
CROSS JOIN ruolo r
WHERE p.nome = 'HR'
  AND r.nome = 'GESTIONE_UTENTI'
ON CONFLICT DO NOTHING;

-- USER ha solo lettura
INSERT INTO profilo_ruolo (profilo_id, ruoli_id)
SELECT
    p.id,
    r.id
FROM profilo p
CROSS JOIN ruolo r
WHERE p.nome = 'USER'
  AND r.nome = 'SOLO_LETTURA'
ON CONFLICT DO NOTHING;

-- ============================================================================
-- 4. CREAZIONE TIPO UTENTE (se non esiste)
-- ============================================================================

INSERT INTO tipoutente (tipo, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'DIPENDENTE',
    'Dipendente Schema31',
    NOW(),
    'SYSTEM'
) ON CONFLICT (tipo) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- ============================================================================
-- 5. CREAZIONE JOB TITLE (se non esiste)
-- ============================================================================

INSERT INTO jobtitle (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'AMMINISTRATORE',
    'Amministratore di sistema',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO UPDATE SET
    descrizione = EXCLUDED.descrizione,
    commonentity_updatedat = NOW();

-- ============================================================================
-- 6. CREAZIONE UTENTE DI ESEMPIO
-- ============================================================================
-- IMPORTANTE: Sostituisci 'username_dal_jwt' con la claim 'sub' del tuo JWT!
--
-- Per trovare la claim 'sub':
-- 1. Fai login nel frontend
-- 2. Apri DevTools > Application > Local Storage
-- 3. Cerca la chiave che inizia con 'oidc.user:'
-- 4. Decodifica il JSON e cerca il campo 'profile.sub'
-- ============================================================================

-- Variabile per lo username (MODIFICA QUESTO VALORE!)
DO $$
DECLARE
    v_username VARCHAR := 'username_dal_jwt';  -- <-- CAMBIA QUESTO VALORE!
    v_profilo_id BIGINT;
    v_tipoutente_id BIGINT;
    v_jobtitle_id BIGINT;
BEGIN
    -- Recupera gli ID necessari
    SELECT id INTO v_profilo_id FROM profilo WHERE nome = 'SUPER_USER';
    SELECT id INTO v_tipoutente_id FROM tipoutente WHERE tipo = 'DIPENDENTE';
    SELECT id INTO v_jobtitle_id FROM jobtitle WHERE nome = 'AMMINISTRATORE';

    -- Crea l'utente se non esiste
    INSERT INTO utente (
        nome,
        cognome,
        username,
        codicefiscale,
        attivo,
        profilo_id,
        tipoutente_id,
        jobtitle_id,
        datainizioCollaborazione,
        commonentity_updatedat,
        commonentity_updatedby
    )
    VALUES (
        'Super',
        'Admin',
        v_username,
        'SPRADM80A01H501X',  -- Codice fiscale di esempio
        true,
        v_profilo_id,
        v_tipoutente_id,
        v_jobtitle_id,
        CURRENT_DATE,
        NOW(),
        'SYSTEM'
    )
    ON CONFLICT (codicefiscale) DO UPDATE SET
        username = EXCLUDED.username,
        profilo_id = EXCLUDED.profilo_id,
        attivo = EXCLUDED.attivo,
        commonentity_updatedat = NOW();

    RAISE NOTICE 'Utente % creato/aggiornato con profilo SUPER_USER', v_username;
END $$;

-- ============================================================================
-- 7. VERIFICA CONFIGURAZIONE
-- ============================================================================

-- Mostra i profili creati
SELECT
    id,
    nome,
    descrizione,
    commonentity_updatedat
FROM profilo
ORDER BY nome;

-- Mostra i ruoli creati
SELECT
    id,
    nome,
    descrizione,
    commonentity_updatedat
FROM ruolo
ORDER BY nome;

-- Mostra l'associazione profili-ruoli
SELECT
    p.nome as profilo,
    r.nome as ruolo
FROM profilo p
LEFT JOIN profilo_ruolo pr ON p.id = pr.profilo_id
LEFT JOIN ruolo r ON pr.ruoli_id = r.id
ORDER BY p.nome, r.nome;

-- Mostra l'utente creato con il suo profilo
SELECT
    u.id,
    u.nome,
    u.cognome,
    u.username,
    u.codicefiscale,
    u.attivo,
    p.nome as profilo,
    t.tipo as tipo_utente,
    j.nome as job_title
FROM utente u
LEFT JOIN profilo p ON u.profilo_id = p.id
LEFT JOIN tipoutente t ON u.tipoutente_id = t.id
LEFT JOIN jobtitle j ON u.jobtitle_id = j.id
WHERE u.commonentity_updatedby = 'SYSTEM'
ORDER BY u.id;

-- ============================================================================
-- FINE SCRIPT
-- ============================================================================

-- Note:
-- 1. Ricorda di modificare 'username_dal_jwt' prima di eseguire lo script
-- 2. Se il vincolo UNIQUE su 'nome' non esiste per profilo/ruolo,
--    puoi avere duplicati. In tal caso, usa ON CONFLICT DO NOTHING
--    e gestisci manualmente gli aggiornamenti
-- 3. Questo script è idempotente: può essere eseguito più volte senza errori
