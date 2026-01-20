package it.schema31.crm.cto.security;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import it.schema31.crm.cto.entities.Profilo;
import it.schema31.crm.cto.entities.Ruolo;
import it.schema31.crm.cto.entities.Utente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Augmentor per arricchire la SecurityIdentity con i ruoli dell'utente
 * recuperati dal database in base al profilo.
 *
 * Gli utenti con profilo SUPER_USER ottengono automaticamente accesso completo.
 */
@ApplicationScoped
public class RoleAugmentor implements SecurityIdentityAugmentor {

    private static final Logger LOG = Logger.getLogger(RoleAugmentor.class);
    private static final String SUPER_USER_ROLE = "SUPER_USER";

    @Inject
    RoleAugmentorHelper helper;

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        if (identity.isAnonymous()) {
            return Uni.createFrom().item(identity);
        }

        return context.runBlocking(() -> {
            try {
                String username = identity.getPrincipal().getName();
                LOG.debugf("Augmenting security identity for user: %s", username);

                Set<String> roles = helper.loadUserRoles(username);

                if (roles == null) {
                    LOG.warnf("User not found in database: %s", username);
                    return identity;
                }

                // Se non ci sono ruoli, l'utente ha comunque accesso base (autenticato)
                if (roles.isEmpty()) {
                    LOG.debugf("User %s has no specific roles", username);
                    return identity;
                }

                // Crea una nuova identity con i ruoli aggiunti
                QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
                for (String role : roles) {
                    builder.addRole(role);
                }

                SecurityIdentity augmentedIdentity = builder.build();
                LOG.debugf("Security identity augmented for user %s with roles: %s", username, roles);

                return augmentedIdentity;

            } catch (Exception e) {
                LOG.errorf(e, "Error augmenting security identity");
                return identity;
            }
        });
    }

    @ApplicationScoped
    public static class RoleAugmentorHelper {

        @ActivateRequestContext
        public Set<String> loadUserRoles(String username) {
            // Recupera l'utente dal database
            Utente utente = Utente.find("username", username).firstResult();

            if (utente == null) {
                return null;
            }

            Set<String> roles = new HashSet<>();

            // Verifica se l'utente ha un profilo
            if (utente.profilo != null) {
                // Carica il profilo completo con i ruoli
                Profilo profilo = Profilo.findById(utente.profilo.id);

                if (profilo != null) {
                    Logger.getLogger(RoleAugmentor.class).debugf("User %s has profile: %s", username, profilo.nome);

                    // Se il profilo è SUPER_USER, aggiungi il ruolo speciale
                    if (SUPER_USER_ROLE.equalsIgnoreCase(profilo.nome)) {
                        roles.add(SUPER_USER_ROLE);
                        Logger.getLogger(RoleAugmentor.class).debugf("User %s is a SUPER_USER", username);
                    }

                    // Aggiungi tutti i ruoli associati al profilo
                    if (profilo.ruoli != null) {
                        profilo.ruoli.size(); // Inizializza la collezione lazy

                        for (Ruolo ruolo : profilo.ruoli) {
                            if (ruolo != null && ruolo.nome != null) {
                                roles.add(ruolo.nome);
                                Logger.getLogger(RoleAugmentor.class).debugf("Added role %s to user %s", ruolo.nome, username);
                            }
                        }
                    }
                }
            }

            return roles;
        }
    }
}
