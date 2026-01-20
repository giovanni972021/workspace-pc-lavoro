# Guida al Testing - Sistema JWT Security

## Prerequisiti

Prima di testare l'applicazione, assicurati di avere:

1. **Database PostgreSQL** in esecuzione su `localhost:5432`
   - Database: `cto`
   - Schema: `crm`
   - Username: `srossi`
   - Password: `srossi`

2. **Utente nel database** con username corrispondente alla claim `sub` del JWT
   - L'utente deve avere un profilo assegnato
   - Per testare l'accesso completo, crea un profilo chiamato `SUPER_USER`

3. **Token JWT valido** dal server OIDC Schema31 (https://is.schema31.it)

## Avvio dell'Applicazione

### Backend

```bash
cd cto-manager-be
./mvnw quarkus:dev
```

L'applicazione sarà disponibile su: http://localhost:8080

### Frontend

```bash
cd cto-manager-fe
pnpm dev
```

Il frontend sarà disponibile su: http://localhost:5173

## Preparazione del Database

### 1. Creare un Profilo SUPER_USER

```sql
-- Connettiti al database cto, schema crm
\c cto

SET search_path TO crm;

-- Crea il profilo SUPER_USER
INSERT INTO profilo (nome, descrizione, commonentity_updatedat)
VALUES ('SUPER_USER', 'Super amministratore con accesso completo', NOW())
ON CONFLICT DO NOTHING;
```

### 2. Creare un Utente di Test

```sql
-- Crea un utente (sostituisci 'username_dal_jwt' con il valore della claim 'sub' del tuo JWT)
INSERT INTO utente (
    nome,
    cognome,
    username,
    codicefiscale,
    attivo,
    profilo_id,
    commonentity_updatedat
)
VALUES (
    'Test',
    'User',
    'username_dal_jwt',  -- IMPORTANTE: deve corrispondere alla claim 'sub' del JWT
    'TSTUSER80A01H501T',
    true,
    (SELECT id FROM profilo WHERE nome = 'SUPER_USER'),
    NOW()
)
ON CONFLICT DO NOTHING;
```

### 3. Verificare la Configurazione

```sql
-- Verifica che l'utente sia stato creato correttamente
SELECT
    u.id,
    u.nome,
    u.cognome,
    u.username,
    p.nome as profilo
FROM utente u
LEFT JOIN profilo p ON u.profilo_id = p.id
WHERE u.username = 'username_dal_jwt';
```

## Flusso di Test

### 1. Ottenere un Token JWT

Il frontend gestisce automaticamente l'autenticazione tramite OIDC:

1. Apri http://localhost:5173
2. Clicca su "Login"
3. Verrai reindirizzato al server OIDC Schema31
4. Dopo il login, verrai reindirizzato al callback
5. Il token JWT verrà salvato automaticamente

### 2. Verificare il Token

Il frontend include automaticamente il token nell'header `Authorization`:

```
Authorization: Bearer <jwt_token>
```

### 3. Testare l'Endpoint Current User

Il frontend chiama automaticamente `/api/current-user` dopo il login:

**Risposta Attesa (200 OK):**
```json
{
  "id": 1,
  "username": "username_dal_jwt",
  "nome": "Test",
  "cognome": "User",
  "profilo": "SUPER_USER",
  "ruoli": ["SUPER_USER"],
  "isSuperUser": true
}
```

**Errore 401 (Token non valido):**
```json
{
  "error": "unauthorized",
  "message": "Token JWT non valido o mancante"
}
```

**Errore 404 (Utente non trovato):**
```json
{
  "error": "not_found",
  "message": "Utente non trovato nel database"
}
```

### 4. Testare Altri Endpoint

Dopo il login, puoi testare altri endpoint protetti:

```bash
# GET utenti (richiede autenticazione)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/utenti

# GET current user
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/current-user

# GET progetti (richiede autenticazione)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/progetti
```

## Troubleshooting

### Problema: Errore 401 anche con token valido

**Possibili Cause:**
1. Token scaduto
2. Token non firmato correttamente
3. Issuer non corrisponde alla configurazione
4. JWKS non raggiungibile

**Verifica:**
```bash
# Controlla i log del backend
# Cerca messaggi come:
# "Token validation failed"
# "Invalid issuer"
# "Unable to fetch JWKS"
```

**Soluzione:**
- Verifica che il server OIDC sia raggiungibile: https://is.schema31.it/oauth2/jwks
- Controlla che la configurazione in `application.yml` sia corretta
- Verifica che il token non sia scaduto

### Problema: Errore 404 "Utente non trovato"

**Causa:** L'username nel database non corrisponde alla claim `sub` del JWT

**Soluzione:**
1. Decodifica il JWT per vedere la claim `sub`
2. Verifica che esista un utente con quel username nel database
3. Se necessario, aggiorna l'username nel database

```sql
-- Aggiorna l'username dell'utente
UPDATE utente
SET username = 'username_dalla_claim_sub'
WHERE id = 1;
```

### Problema: Errore CORS

**Sintomo:** Browser mostra errore "CORS policy" o "Access-Control-Allow-Origin"

**Causa:** Gli header CORS non sono configurati correttamente

**Verifica:**
1. Controlla che `CorsFilter.java` sia presente
2. Verifica la configurazione CORS in `application.yml`
3. Assicurati che l'origine del frontend sia permessa

**Configurazione corrente:**
```yaml
quarkus:
  http:
    cors:
      origins: http://localhost:5173
      methods: GET,POST,PUT,DELETE,OPTIONS,HEAD
      headers: accept,authorization,content-type,x-requested-with
```

### Problema: Ruoli non assegnati

**Sintomo:** L'utente è autenticato ma riceve 403 Forbidden

**Causa:** Il profilo non ha ruoli assegnati o il RoleAugmentor non funziona

**Verifica:**
```sql
-- Verifica i ruoli del profilo
SELECT
    p.nome as profilo,
    r.nome as ruolo
FROM profilo p
LEFT JOIN profilo_ruolo pr ON p.id = pr.profilo_id
LEFT JOIN ruolo r ON pr.ruoli_id = r.id
WHERE p.nome = 'SUPER_USER';
```

**Soluzione:**
- Se il profilo è `SUPER_USER`, non servono ruoli aggiuntivi
- Il `RoleAugmentor` aggiungerà automaticamente il ruolo `SUPER_USER`
- Controlla i log del backend (livello DEBUG) per vedere se il RoleAugmentor viene eseguito

### Problema: Token nel Frontend non viene inviato

**Sintomo:** Il backend riceve richieste senza header Authorization

**Verifica:**
1. Apri DevTools del browser
2. Vai alla tab Network
3. Seleziona una richiesta API
4. Verifica che l'header `Authorization` sia presente

**Soluzione:**
- Verifica che `userManager.getUser()` restituisca un utente valido
- Controlla che l'interceptor in `api.ts` funzioni correttamente
- Pulisci il localStorage e rieffettua il login

```javascript
// Console del browser
localStorage.clear();
location.reload();
```

## Endpoint Pubblici

Alcuni endpoint non richiedono autenticazione:

- `/api/auth/token` - Scambio codice OAuth2 per token
- `/api/auth/refresh` - Rinnovo token
- `/q/health` - Health check
- `/q/openapi` - OpenAPI specification
- `/q/swagger-ui` - Swagger UI

## Log Utili

Per abilitare logging dettagliato, modifica `application.yml`:

```yaml
quarkus:
  log:
    level: DEBUG
    category:
      "io.quarkus.oidc":
        level: DEBUG
      "io.quarkus.security":
        level: DEBUG
      "it.schema31.crm.cto.security":
        level: DEBUG
```

Cerca nei log messaggi come:

```
DEBUG [it.schema31.crm.cto.security.RoleAugmentor] Augmenting security identity for user: username
DEBUG [it.schema31.crm.cto.security.RoleAugmentor] User username has profile: SUPER_USER
DEBUG [it.schema31.crm.cto.security.RoleAugmentor] User username is a SUPER_USER
DEBUG [it.schema31.crm.cto.security.CorsFilter] CORS headers added for origin: http://localhost:5173, status: 401
```

## Test Manuali con cURL

### Ottenere un Token

Il token deve essere ottenuto tramite il flusso OAuth2. Per test manuali, puoi:

1. Fare login tramite il frontend
2. Aprire DevTools > Application > Local Storage
3. Cercare il token in `oidc.user:https://is.schema31.it/oauth2/oidcdiscovery:l0P5iJK5YGr9AhUlya_Xf0vzXOYa`
4. Copiare il valore di `access_token`

### Testare con cURL

```bash
# Salva il token in una variabile
export TOKEN="eyJhbGc..."

# Test current user
curl -v -H "Authorization: Bearer $TOKEN" \
     http://localhost:8080/api/current-user

# Test lista utenti
curl -v -H "Authorization: Bearer $TOKEN" \
     http://localhost:8080/api/utenti

# Test senza token (deve restituire 401)
curl -v http://localhost:8080/api/utenti
```

## Checklist Pre-Test

Prima di iniziare i test, verifica:

- [ ] Database PostgreSQL in esecuzione
- [ ] Schema `crm` creato
- [ ] Profilo `SUPER_USER` creato
- [ ] Utente creato con username = claim `sub` del JWT
- [ ] Utente ha profilo `SUPER_USER` assegnato
- [ ] Backend avviato su porta 8080
- [ ] Frontend avviato su porta 5173
- [ ] Configurazione CORS corretta
- [ ] Server OIDC raggiungibile (https://is.schema31.it)

## Test Automatici

Per eseguire i test automatici (quando saranno implementati):

```bash
cd cto-manager-be
./mvnw test
```

**Note:** I test attualmente sono disabilitati con `-DskipTests` perché richiedono mock del sistema OIDC.
