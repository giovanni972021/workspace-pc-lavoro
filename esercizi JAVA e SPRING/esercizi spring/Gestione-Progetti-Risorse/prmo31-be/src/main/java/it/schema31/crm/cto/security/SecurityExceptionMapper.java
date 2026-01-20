package it.schema31.crm.cto.security;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper per gestire le eccezioni di sicurezza e fornire risposte HTTP appropriate.
 */
@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {

    private static final Logger LOG = Logger.getLogger(SecurityExceptionMapper.class);

    @Override
    public Response toResponse(SecurityException exception) {
        LOG.debugf("Security exception caught: %s", exception.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "security_error");
        errorResponse.put("message", exception.getMessage());

        if (exception instanceof UnauthorizedException || exception instanceof AuthenticationFailedException) {
            errorResponse.put("error", "unauthorized");
            errorResponse.put("message", "Token JWT non valido o mancante");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorResponse)
                    .build();
        }

        if (exception instanceof ForbiddenException) {
            errorResponse.put("error", "forbidden");
            errorResponse.put("message", "Accesso negato: permessi insufficienti");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(errorResponse)
                    .build();
        }

        // Altri errori di sicurezza
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}
