package it.schema31.crm.cto.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;

/**
 * Filtro CORS personalizzato per garantire che gli header CORS siano sempre presenti,
 * anche per le risposte di errore 401/403 che vengono generate prima della gestione CORS standard.
 *
 * Questo è necessario perché Quarkus OIDC può rifiutare richieste con 401 prima che
 * il filtro CORS standard abbia la possibilità di aggiungere gli header necessari.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(CorsFilter.class);

    @ConfigProperty(name = "quarkus.http.cors.origins", defaultValue = "http://localhost:5173")
    String allowedOrigins;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        // Ottieni l'origine della richiesta
        String origin = requestContext.getHeaderString("Origin");

        // Se non c'è origine (richiesta same-origin), non fare nulla
        if (origin == null || origin.isEmpty()) {
            return;
        }

        // Verifica se l'origine è permessa
        if (isOriginAllowed(origin)) {
            // Aggiungi gli header CORS se non sono già presenti
            if (!responseContext.getHeaders().containsKey("Access-Control-Allow-Origin")) {
                responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
            }

            if (!responseContext.getHeaders().containsKey("Access-Control-Allow-Credentials")) {
                responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            }

            if (!responseContext.getHeaders().containsKey("Access-Control-Allow-Methods")) {
                responseContext.getHeaders().add("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            }

            if (!responseContext.getHeaders().containsKey("Access-Control-Allow-Headers")) {
                responseContext.getHeaders().add("Access-Control-Allow-Headers",
                        "accept, authorization, content-type, x-requested-with");
            }

            if (!responseContext.getHeaders().containsKey("Access-Control-Expose-Headers")) {
                responseContext.getHeaders().add("Access-Control-Expose-Headers",
                        "Content-Disposition");
            }

            LOG.debugf("CORS headers added for origin: %s, status: %d", origin, responseContext.getStatus());
        } else {
            LOG.warnf("Origin not allowed: %s", origin);
        }
    }

    /**
     * Verifica se l'origine è permessa confrontandola con la configurazione.
     *
     * @param origin l'origine della richiesta
     * @return true se l'origine è permessa, false altrimenti
     */
    private boolean isOriginAllowed(String origin) {
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            return false;
        }

        // Supporta wildcard "*" o lista separata da virgole
        if ("*".equals(allowedOrigins.trim())) {
            return true;
        }

        String[] origins = allowedOrigins.split(",");
        for (String allowedOrigin : origins) {
            if (origin.equals(allowedOrigin.trim())) {
                return true;
            }
        }

        return false;
    }
}
