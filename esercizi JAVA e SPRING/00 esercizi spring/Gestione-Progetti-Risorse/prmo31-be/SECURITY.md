# Sistema di Sicurezza - CTO Manager

## Panoramica

Il sistema di sicurezza dell'applicazione CTO Manager utilizza **JWT (JSON Web Token)** per l'autenticazione e l'autorizzazione. L'integrazione con il server OIDC di Schema31 (https://is.schema31.it) garantisce un'autenticazione centralizzata e sicura.

## Architettura

### 1. Validazione JWT

Il token JWT viene validato automaticamente da Quarkus OIDC utilizzando la configurazione in `application.yml`:

```yaml
quarkus:
  oidc:
    enabled: true
    auth-server-url: https://is.schema31.it/oauth2/oidcdiscovery
    client-id: l0P5iJK5YGr9AhUlya_Xf0vzXOYa
    application-type: service
    token:
      issuer: https://is.schema31.it/oauth2/oidcdiscovery
      jwks-path: https://is.schema31.it/oauth2/jwks
      audience: l0P5iJK5YGr9AhUlya_Xf0vzXOYa
```

### 2. Estrazione dell'Utente

Il sistema legge la claim `sub` dal JWT per identificare l'utente:

- **Claim `sub`**: contiene l'username dell'utente
- **SecurityService**: recupera l'utente dal database usando l'username
- **Profilo Utente**: ogni utente ha un profilo che definisce i suoi permessi

### 3. Sistema dei Ruoli

I ruoli vengono assegnati automaticamente in base al profilo dell'utente tramite il **RoleAugmentor**:

1. Il `RoleAugmentor` intercetta ogni richiesta autenticata
2. Recupera l'utente dal database usando l'username dalla claim `sub`
3. Carica il profilo dell'utente con i suoi ruoli
4. Aggiunge i ruoli alla `SecurityIdentity` di Quarkus
5. Se il profilo è `SUPER_USER`, aggiunge automaticamente il ruolo SUPER_USER

### 4. Profilo SUPER_USER

Gli utenti con profilo **SUPER_USER** hanno accesso completo a tutte le API:

- Il profilo `SUPER_USER` viene riconosciuto automaticamente
- Viene assegnato il ruolo `SUPER_USER`
- Questo ruolo garantisce l'accesso a tutte le risorse protette

## Componenti Principali

### SecurityService

`it.schema31.crm.cto.security.SecurityService`

Fornisce metodi di utility per:
- `getCurrentUsername()`: recupera l'username dalla claim `sub` del JWT
- `getCurrentUser()`: recupera l'entità Utente dal database
- `getCurrentUserProfilo()`: recupera il profilo dell'utente corrente
- `isSuperUser()`: verifica se l'utente è un SUPER_USER
- `isAuthenticated()`: verifica se l'utente è autenticato

### RoleAugmentor

`it.schema31.crm.cto.security.RoleAugmentor`

Implementa `SecurityIdentityAugmentor` per arricchire la SecurityIdentity con i ruoli:
- Recupera l'utente dal database
- Carica il profilo e i ruoli associati
- Aggiunge i ruoli alla SecurityIdentity
- Gestisce il ruolo speciale SUPER_USER

### SecurityExceptionMapper

`it.schema31.crm.cto.security.SecurityExceptionMapper`

Gestisce le eccezioni di sicurezza e restituisce risposte HTTP appropriate:
- `401 Unauthorized`: token JWT non valido o mancante
- `403 Forbidden`: permessi insufficienti
- `500 Internal Server Error`: altri errori di sicurezza

## Protezione delle API

### Autenticazione Richiesta

Per proteggere un endpoint REST, utilizzare l'annotazione `@Authenticated`:

```java
@Path("/api/utenti")
@Authenticated
public class UtenteResource {
    // Tutti i metodi richiedono autenticazione
}
```

### Controllo Ruoli

Per limitare l'accesso a specifici ruoli, utilizzare `@RolesAllowed`:

```java
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;

@Path("/api/admin")
@RolesAllowed(Roles.SUPER_USER)
public class AdminResource {
    // Solo SUPER_USER può accedere
}
```

Esempio con ruoli multipli:

```java
@GET
@RolesAllowed({Roles.SUPER_USER, Roles.ADMIN, Roles.HR})
public Response listUsers() {
    // Accessibile a SUPER_USER, ADMIN o HR
}
```

### Controllo Programmatico

Per controlli più complessi, utilizzare `SecurityService`:

```java
@Inject
SecurityService securityService;

@GET
public Response getData() {
    if (securityService.isSuperUser()) {
        // Logica per SUPER_USER
    } else {
        // Logica per altri utenti
    }
}
```

## Configurazione del Client

### Header di Autenticazione

Le richieste devono includere il token JWT nell'header `Authorization`:

```
Authorization: Bearer <jwt_token>
```

### Esempio con cURL

```bash
curl -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..." \
     http://localhost:8080/api/current-user
```

### Esempio con JavaScript/Fetch

```javascript
fetch('http://localhost:8080/api/utenti', {
  headers: {
    'Authorization': `Bearer ${accessToken}`,
    'Content-Type': 'application/json'
  }
})
```

## Flusso di Autenticazione

1. **Client**: Ottiene il token JWT dal server OIDC (is.schema31.it)
2. **Client**: Invia il token nell'header `Authorization: Bearer <token>`
3. **Quarkus OIDC**: Valida il token usando JWKS
4. **RoleAugmentor**: Arricchisce l'identity con i ruoli dal database
5. **Resource**: Verifica `@Authenticated` o `@RolesAllowed`
6. **Response**: Restituisce i dati o errore 401/403

## Gestione degli Errori

### 401 Unauthorized

Restituito quando:
- Token JWT mancante nell'header
- Token JWT scaduto
- Token JWT non valido (firma non corretta)
- Token JWT emesso da un issuer non autorizzato

Risposta:
```json
{
  "error": "unauthorized",
  "message": "Token JWT non valido o mancante"
}
```

### 403 Forbidden

Restituito quando:
- Utente autenticato ma senza permessi sufficienti
- Ruolo richiesto non presente nell'identity

Risposta:
```json
{
  "error": "forbidden",
  "message": "Accesso negato: permessi insufficienti"
}
```

### 404 Not Found

Restituito quando:
- Utente autenticato ma non presente nel database locale

## Best Practices

1. **Sempre usare @Authenticated**: Ogni endpoint che richiede autenticazione deve avere l'annotazione
2. **Principio del minimo privilegio**: Assegnare solo i ruoli necessari agli utenti
3. **Validare i dati**: Non fidarsi solo dell'autenticazione, validare sempre i dati in input
4. **Logging**: Il sistema logga automaticamente operazioni di sicurezza (livello DEBUG)
5. **Gestione errori**: Usare `SecurityExceptionMapper` per fornire messaggi di errore consistenti

## Debugging

Per abilitare il logging dettagliato della sicurezza, modificare `application.yml`:

```yaml
quarkus:
  log:
    category:
      "io.quarkus.oidc":
        level: DEBUG
      "io.quarkus.security":
        level: DEBUG
      "it.schema31.crm.cto.security":
        level: DEBUG
```

## Esempio Completo

```java
package it.schema31.crm.cto.resources;

import io.quarkus.security.Authenticated;
import it.schema31.crm.cto.security.Roles;
import it.schema31.crm.cto.security.SecurityService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/api/example")
@Authenticated
public class ExampleResource {

    @Inject
    SecurityService securityService;

    // Accessibile a tutti gli utenti autenticati
    @GET
    @Path("/public")
    public Response publicEndpoint() {
        String username = securityService.getCurrentUsername();
        return Response.ok("Hello " + username).build();
    }

    // Accessibile solo a SUPER_USER
    @GET
    @Path("/admin")
    @RolesAllowed(Roles.SUPER_USER)
    public Response adminEndpoint() {
        return Response.ok("Admin data").build();
    }

    // Controllo programmatico
    @GET
    @Path("/conditional")
    public Response conditionalEndpoint() {
        if (securityService.isSuperUser()) {
            return Response.ok("Full data").build();
        } else {
            return Response.ok("Limited data").build();
        }
    }
}
```

## Risoluzione Problemi

### Token non valido

**Problema**: Errore 401 anche con token apparentemente valido

**Soluzioni**:
1. Verificare che l'issuer nel token corrisponda alla configurazione
2. Controllare che il JWKS sia raggiungibile
3. Verificare che la configurazione TLS sia corretta (`tls.verification: none` per sviluppo)

### Utente non trovato

**Problema**: Token valido ma errore 404 "Utente non trovato"

**Soluzioni**:
1. Verificare che l'utente esista nel database con lo stesso username della claim `sub`
2. Controllare che il campo `username` dell'utente corrisponda esattamente alla claim `sub`

### Ruoli non assegnati

**Problema**: Utente autenticato ma errore 403

**Soluzioni**:
1. Verificare che l'utente abbia un profilo assegnato
2. Controllare che il profilo abbia i ruoli necessari
3. Verificare i log del `RoleAugmentor` (livello DEBUG)
