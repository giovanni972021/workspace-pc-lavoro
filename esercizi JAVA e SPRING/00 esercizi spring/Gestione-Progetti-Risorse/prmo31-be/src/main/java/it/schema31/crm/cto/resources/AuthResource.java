package it.schema31.crm.cto.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClients;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Resource per la gestione dell'autenticazione OAuth2/OIDC.
 * Funge da proxy per lo scambio del codice di autorizzazione con i token,
 * evitando problemi CORS nel browser.
 */
@Path("/api/auth")
@Tag(name = "Authentication", description = "Endpoint per l'autenticazione")
public class AuthResource {

    @Inject
    OidcClients oidcClients;

    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Scambia il codice di autorizzazione per i token",
               description = "Endpoint proxy per lo scambio del codice OAuth2 con i token, evitando problemi CORS")
    public Uni<Response> exchangeCodeForToken(
            @FormParam("code") String code,
            @FormParam("redirect_uri") String redirectUri,
            @FormParam("code_verifier") String codeVerifier) {

        if (code == null || code.isEmpty()) {
            return Uni.createFrom().item(
                Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "missing_code", "error_description", "Authorization code is required"))
                    .build()
            );
        }

        OidcClient client = oidcClients.getClient();

        // Costruisci i parametri per lo scambio del codice
        Map<String, String> tokenParams = new HashMap<>();
        tokenParams.put("grant_type", "authorization_code");
        tokenParams.put("code", code);
        tokenParams.put("redirect_uri", redirectUri != null ? redirectUri : "http://localhost:5173/callback");

        if (codeVerifier != null && !codeVerifier.isEmpty()) {
            tokenParams.put("code_verifier", codeVerifier);
        }

        return client.getTokens(tokenParams)
            .onItem().transform(tokens -> {
                Map<String, Object> response = new HashMap<>();
                response.put("access_token", tokens.getAccessToken());

                if (tokens.getRefreshToken() != null) {
                    response.put("refresh_token", tokens.getRefreshToken());
                }

                // Note: Quarkus OIDC Client Tokens may not expose ID token directly
                // The ID token is typically included in the access token response
                response.put("token_type", "Bearer");

                return Response.ok(response).build();
            })
            .onFailure().recoverWithItem(error -> {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "token_exchange_failed");
                errorResponse.put("error_description", error.getMessage());

                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
            });
    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Rinnova i token usando il refresh token",
               description = "Endpoint per rinnovare l'access token usando il refresh token")
    public Uni<Response> refreshToken(@FormParam("refresh_token") String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            return Uni.createFrom().item(
                Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "missing_refresh_token", "error_description", "Refresh token is required"))
                    .build()
            );
        }

        OidcClient client = oidcClients.getClient();

        Map<String, String> refreshParams = new HashMap<>();
        refreshParams.put("grant_type", "refresh_token");
        refreshParams.put("refresh_token", refreshToken);

        return client.getTokens(refreshParams)
            .onItem().transform(tokens -> {
                Map<String, Object> response = new HashMap<>();
                response.put("access_token", tokens.getAccessToken());

                if (tokens.getRefreshToken() != null) {
                    response.put("refresh_token", tokens.getRefreshToken());
                }

                response.put("token_type", "Bearer");

                return Response.ok(response).build();
            })
            .onFailure().recoverWithItem(error -> {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "refresh_failed");
                errorResponse.put("error_description", error.getMessage());

                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorResponse)
                    .build();
            });
    }
}
