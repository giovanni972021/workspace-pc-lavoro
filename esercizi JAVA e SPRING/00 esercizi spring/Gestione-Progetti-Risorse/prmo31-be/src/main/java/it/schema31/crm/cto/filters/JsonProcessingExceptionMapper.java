package it.schema31.crm.cto.filters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for JSON processing/deserialization errors.
 * These errors occur before the request reaches the endpoint method
 * and would otherwise return a 400 without any logging.
 */
@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    private static final Logger LOG = Logger.getLogger(JsonProcessingExceptionMapper.class);

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        "http://localhost:5173",
        "http://localhost:3000"
    );

    @Context
    HttpHeaders headers;

    @Override
    public Response toResponse(JsonProcessingException exception) {
        LOG.error("JSON deserialization error", exception);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "invalid_json");
        errorResponse.put("message", "Errore nella deserializzazione del JSON: " + exception.getOriginalMessage());
        errorResponse.put("type", exception.getClass().getName());

        // Include location info if available
        if (exception.getLocation() != null) {
            Map<String, Object> location = new HashMap<>();
            location.put("line", exception.getLocation().getLineNr());
            location.put("column", exception.getLocation().getColumnNr());
            errorResponse.put("location", location);
        }

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
}
