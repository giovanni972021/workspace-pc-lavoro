package it.schema31.crm.cto.security;

import io.quarkus.security.identity.SecurityIdentity;
import it.schema31.crm.cto.entities.Profilo;
import it.schema31.crm.cto.entities.Utente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * Servizio per gestire operazioni di sicurezza e recuperare informazioni
 * sull'utente corrente dal token JWT.
 */
@ApplicationScoped
public class SecurityService {

    @Inject
    JsonWebToken jwt;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Recupera l'username dell'utente corrente dalla claim 'sub' del JWT.
     *
     * @return l'username dell'utente corrente o null se non autenticato
     */
    public String getCurrentUsername() {
        if (jwt == null || jwt.getSubject() == null) {
            return null;
        }
        return jwt.getSubject();
    }

    /**
     * Recupera l'utente corrente dal database basandosi sull'username nel JWT.
     *
     * @return l'entità Utente corrente o null se non trovato
     */
    public Utente getCurrentUser() {
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        return Utente.find("username", username).firstResult();
    }

    /**
     * Recupera il profilo dell'utente corrente.
     *
     * @return il Profilo dell'utente corrente o null se non presente
     */
    public Profilo getCurrentUserProfilo() {
        Utente utente = getCurrentUser();
        if (utente == null || utente.profilo == null) {
            return null;
        }

        // Forza il caricamento del profilo completo con i ruoli
        return Profilo.findById(utente.profilo.id);
    }

    /**
     * Verifica se l'utente corrente ha il profilo SUPER_USER.
     * Gli utenti SUPER_USER hanno accesso a tutte le API.
     *
     * @return true se l'utente è un SUPER_USER, false altrimenti
     */
    public boolean isSuperUser() {
        Profilo profilo = getCurrentUserProfilo();
        if (profilo == null) {
            return false;
        }
        return "SUPER_USER".equalsIgnoreCase(profilo.nome);
    }

    /**
     * Verifica se l'utente corrente è autenticato.
     *
     * @return true se l'utente è autenticato, false altrimenti
     */
    public boolean isAuthenticated() {
        return securityIdentity != null && !securityIdentity.isAnonymous();
    }

    /**
     * Recupera il token JWT completo.
     *
     * @return il JsonWebToken corrente
     */
    public JsonWebToken getJwt() {
        return jwt;
    }
}
