package it.schema31.crm.cto.security;

/**
 * Costanti per i ruoli utilizzati nell'applicazione.
 * I ruoli sono definiti nel database e associati ai profili utente.
 */
public final class Roles {

    /**
     * Ruolo SUPER_USER: accesso completo a tutte le API.
     * Questo ruolo viene assegnato automaticamente agli utenti con profilo SUPER_USER.
     */
    public static final String SUPER_USER = "SUPER_USER";

    /**
     * Esempio di altri ruoli che possono essere definiti nel sistema:
     * - ADMIN: amministratori con privilegi elevati
     * - USER: utenti standard
     * - MANAGER: manager con accesso a funzionalità di gestione
     * - HR: risorse umane
     */
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String MANAGER = "MANAGER";
    public static final String HR = "HR";

    private Roles() {
        // Utility class - private constructor
    }
}
