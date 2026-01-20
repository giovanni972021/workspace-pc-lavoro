package it.schema31.crm.cto.filters;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception mapper to catch all unhandled exceptions,
 * log them, and return proper error responses with CORS headers.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        "http://localhost:5173",
        "http://localhost:3000"
    );

    @Context
    HttpHeaders headers;

    @Override
    public Response toResponse(Throwable exception) {
        // Log the full stack trace
        LOG.error("Unhandled exception caught", exception);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "internal_server_error");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("type", exception.getClass().getName());

        // Get the root cause
        Throwable rootCause = getRootCause(exception);
        if (rootCause != exception) {
            errorResponse.put("rootCause", rootCause.getMessage());
            errorResponse.put("rootCauseType", rootCause.getClass().getName());
        }

        // Build response with CORS headers
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse);

        // Add CORS headers
        String origin = headers.getHeaderString("Origin");
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            responseBuilder.header("Access-Control-Allow-Origin", origin);
            responseBuilder.header("Access-Control-Allow-Credentials", "true");
        }
        responseBuilder.header("Access-Control-Allow-Headers",
                "accept, authorization, content-type, x-requested-with");
        responseBuilder.header("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, PATCH");

        return responseBuilder.build();
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}
