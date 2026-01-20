/**
 * Package per la gestione della sicurezza e autenticazione JWT.
 *
 * <h2>Componenti Principali:</h2>
 * <ul>
 *   <li><b>SecurityService</b>: Servizio per recuperare informazioni sull'utente corrente dal JWT</li>
 *   <li><b>RoleAugmentor</b>: Arricchisce la SecurityIdentity con i ruoli dal database</li>
 *   <li><b>SecurityExceptionMapper</b>: Gestisce le eccezioni di sicurezza</li>
 *   <li><b>Roles</b>: Costanti per i ruoli dell'applicazione</li>
 * </ul>
 *
 * <h2>Come Funziona:</h2>
 * <ol>
 *   <li>Il token JWT viene validato automaticamente da Quarkus OIDC</li>
 *   <li>La claim 'sub' del JWT contiene l'username dell'utente</li>
 *   <li>Il RoleAugmentor recupera l'utente dal database e carica il suo profilo</li>
 *   <li>I ruoli del profilo vengono aggiunti alla SecurityIdentity</li>
 *   <li>Gli utenti con profilo SUPER_USER hanno accesso completo</li>
 * </ol>
 *
 * <h2>Esempio di Utilizzo:</h2>
 * <pre>
 * {@code
 * @Path("/api/example")
 * @Authenticated
 * public class ExampleResource {
 *
 *     @Inject
 *     SecurityService securityService;
 *
 *     @GET
 *     @RolesAllowed(Roles.SUPER_USER)
 *     public Response adminOnly() {
 *         // Solo SUPER_USER può accedere
 *         return Response.ok().build();
 *     }
 *
 *     @GET
 *     @Path("/me")
 *     public Response getCurrentUser() {
 *         String username = securityService.getCurrentUsername();
 *         Utente utente = securityService.getCurrentUser();
 *         boolean isSuperUser = securityService.isSuperUser();
 *         return Response.ok(utente).build();
 *     }
 * }
 * }
 * </pre>
 *
 * @see it.schema31.crm.cto.security.SecurityService
 * @see it.schema31.crm.cto.security.RoleAugmentor
 * @see io.quarkus.security.Authenticated
 * @see jakarta.annotation.security.RolesAllowed
 */
package it.schema31.crm.cto.security;
