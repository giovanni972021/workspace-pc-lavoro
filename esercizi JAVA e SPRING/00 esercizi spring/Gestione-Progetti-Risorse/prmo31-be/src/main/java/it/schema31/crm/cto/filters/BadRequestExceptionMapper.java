package it.schema31.crm.cto.filters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for BadRequestException.
 * Catches 400 errors that would otherwise go unlogged.
 */
@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    private static final Logger LOG = Logger.getLogger(BadRequestExceptionMapper.class);

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        "http://localhost:5173",
        "http://localhost:3000"
    );

    @Context
    HttpHeaders headers;

    @Override
    public Response toResponse(BadRequestException exception) {
        LOG.error("Bad request error", exception);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "bad_request");
        errorResponse.put("message", exception.getMessage() != null ? exception.getMessage() : "Richiesta non valida");
        errorResponse.put("type", exception.getClass().getName());

        // Include the response from the original exception if available
        if (exception.getResponse() != null && exception.getResponse().getEntity() != null) {
            errorResponse.put("details", exception.getResponse().getEntity().toString());
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
