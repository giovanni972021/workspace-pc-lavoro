package it.schema31.crm.cto.filters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for Bean Validation constraint violations.
 * Provides detailed error messages about which fields failed validation.
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        "http://localhost:5173",
        "http://localhost:3000"
    );

    @Context
    HttpHeaders headers;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        LOG.error("Constraint violation error", exception);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "validation_error");
        errorResponse.put("message", "Errore di validazione dei dati");

        List<Map<String, String>> violations = exception.getConstraintViolations().stream()
            .map(this::mapViolation)
            .collect(Collectors.toList());

        errorResponse.put("violations", violations);

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
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

    private Map<String, String> mapViolation(ConstraintViolation<?> violation) {
        Map<String, String> result = new HashMap<>();
        result.put("field", violation.getPropertyPath().toString());
        result.put("message", violation.getMessage());
        if (violation.getInvalidValue() != null) {
            result.put("invalidValue", violation.getInvalidValue().toString());
        }
        return result;
    }
}
