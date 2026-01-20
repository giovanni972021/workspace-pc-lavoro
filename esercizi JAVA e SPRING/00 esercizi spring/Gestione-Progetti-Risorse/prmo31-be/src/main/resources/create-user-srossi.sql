-- ============================================================================
-- Script per creare l'utente srossi@schema31.it con profilo SUPER_USER
-- ============================================================================

SET search_path TO crm;

-- 1. Verifica/Crea il profilo SUPER_USER
INSERT INTO profilo (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'SUPER_USER',
    'Super amministratore con accesso completo',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO NOTHING;

-- 2. Verifica/Crea tipo utente
INSERT INTO tipoutente (tipo, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'DIPENDENTE',
    'Dipendente Schema31',
    NOW(),
    'SYSTEM'
) ON CONFLICT (tipo) DO NOTHING;

-- 3. Verifica/Crea job title
INSERT INTO jobtitle (nome, descrizione, commonentity_updatedat, commonentity_updatedby)
VALUES (
    'CTO',
    'Chief Technology Officer',
    NOW(),
    'SYSTEM'
) ON CONFLICT (nome) DO NOTHING;

-- 4. Crea o aggiorna l'utente Salvatore Rossi
DO $$
DECLARE
    v_profilo_id BIGINT;
    v_tipoutente_id BIGINT;
    v_jobtitle_id BIGINT;
    v_utente_id BIGINT;
BEGIN
    -- Recupera gli ID necessari
    SELECT id INTO v_profilo_id FROM profilo WHERE nome = 'SUPER_USER';
    SELECT id INTO v_tipoutente_id FROM tipoutente WHERE tipo = 'DIPENDENTE';
    SELECT id INTO v_jobtitle_id FROM jobtitle WHERE nome = 'CTO';

    -- Verifica se l'utente esiste già
    SELECT id INTO v_utente_id FROM utente WHERE username = 'srossi@schema31.it';

    IF v_utente_id IS NOT NULL THEN
        -- Aggiorna l'utente esistente
        UPDATE utente SET
            profilo_id = v_profilo_id,
            attivo = true,
            commonentity_updatedat = NOW()
        WHERE id = v_utente_id;

        RAISE NOTICE 'Utente srossi@schema31.it aggiornato con profilo SUPER_USER';
    ELSE
        -- Crea nuovo utente
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
            'Salvatore',
            'Rossi',
            'srossi@schema31.it',
            'RSSSLT80A01H501X',
            true,
            v_profilo_id,
            v_tipoutente_id,
            v_jobtitle_id,
            CURRENT_DATE,
            NOW(),
            'SYSTEM'
        );

        RAISE NOTICE 'Utente srossi@schema31.it creato con profilo SUPER_USER';
    END IF;
END $$;

-- 5. Verifica che l'utente sia stato creato correttamente
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
WHERE u.username = 'srossi@schema31.it';
