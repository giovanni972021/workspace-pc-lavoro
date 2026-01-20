# Quick Start - Risoluzione Problema 401

## Problema Identificato

1. **Issuer non corretto**: Il JWT ha issuer `https://is.schema31.it/oauth2/token` ma la configurazione aveva `https://is.schema31.it/oauth2/oidcdiscovery`
2. **Username errato**: La claim `sub` del JWT è `srossi@schema31.it` (email), non un semplice username

## ✅ Soluzioni Applicate

### 1. Configurazione OIDC Corretta

Aggiornato `application.yml` con l'issuer corretto:

```yaml
quarkus:
  oidc:
    token:
      issuer: https://is.schema31.it/oauth2/token  # ✅ Corretto
```

### 2. Script SQL per Creare l'Utente

Esegui lo script SQL per creare/aggiornare l'utente:

```bash
psql -U srossi -d cto < src/main/resources/create-user-srossi.sql
```

Oppure manualmente:

```sql
-- Connettiti al database
psql -U srossi -d cto

-- Imposta lo schema
SET search_path TO crm;

-- Esegui lo script
\i src/main/resources/create-user-srossi.sql
```

## 🚀 Avvio dell'Applicazione

### 1. Riavvia il Backend

```bash
cd cto-manager-be
./mvnw quarkus:dev
```

### 2. Verifica la Configurazione

Controlla nei log che appaia:

```
INFO  [io.quarkus.oidc] OIDC token issuer: https://is.schema31.it/oauth2/token
INFO  [io.quarkus.oidc] OIDC JWKS path: https://is.schema31.it/oauth2/jwks
```

### 3. Riavvia il Frontend

```bash
cd cto-manager-fe
pnpm dev
```

### 4. Testa il Login

1. Apri http://localhost:5173
2. Fai login con le credenziali di `srossi@schema31.it`
3. Dopo il redirect, dovresti vedere la dashboard

## ✅ Verifica che Funzioni

Dopo il login, controlla:

### Nel Browser (DevTools > Console)

Dovresti vedere:
```
✓ User loaded: srossi@schema31.it
✓ Current user loaded: {...}
```

Invece di:
```
✗ Error loading current user: Network Error
```

### Nel Backend (Logs)

Dovresti vedere (con logging DEBUG attivo):

```
DEBUG [it.schema31.crm.cto.security.RoleAugmentor] Augmenting security identity for user: srossi@schema31.it
DEBUG [it.schema31.crm.cto.security.RoleAugmentor] User srossi@schema31.it has profile: SUPER_USER
DEBUG [it.schema31.crm.cto.security.RoleAugmentor] User srossi@schema31.it is a SUPER_USER
DEBUG [it.schema31.crm.cto.security.RoleAugmentor] Security identity augmented for user srossi@schema31.it with roles: [SUPER_USER]
```

### Endpoint `/api/current-user`

Chiamata:
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/current-user
```

Risposta attesa (200 OK):
```json
{
  "id": 1,
  "username": "srossi@schema31.it",
  "nome": "Salvatore",
  "cognome": "Rossi",
  "profilo": "SUPER_USER",
  "ruoli": ["SUPER_USER"],
  "isSuperUser": true
}
```

## 🐛 Se Ancora Non Funziona

### 1. Verifica l'Utente nel Database

```sql
-- Connettiti al database
psql -U srossi -d cto

SET search_path TO crm;

-- Verifica che l'utente esista
SELECT
    u.id,
    u.username,
    p.nome as profilo,
    u.attivo
FROM utente u
LEFT JOIN profilo p ON u.profilo_id = p.id
WHERE u.username = 'srossi@schema31.it';
```

Dovrebbe restituire:
```
 id |      username       |  profilo   | attivo
----+--------------------+------------+--------
  1 | srossi@schema31.it | SUPER_USER | t
```

### 2. Verifica il Token JWT

Decodifica il token JWT su https://jwt.io e verifica:

- ✅ `"iss": "https://is.schema31.it/oauth2/token"`
- ✅ `"sub": "srossi@schema31.it"`
- ✅ `"aud": "l0P5iJK5YGr9AhUlya_Xf0vzXOYa"`
- ✅ `"exp"`: non scaduto

### 3. Verifica JWKS Raggiungibile

```bash
curl https://is.schema31.it/oauth2/jwks
```

Dovrebbe restituire un JSON con le chiavi pubbliche.

### 4. Abilita Logging Dettagliato

In `application.yml` (profilo dev):

```yaml
quarkus:
  log:
    category:
      "io.quarkus.oidc":
        level: TRACE  # Molto dettagliato
      "io.quarkus.security":
        level: TRACE
      "it.schema31.crm.cto.security":
        level: TRACE
```

Riavvia e cerca nei log:

- `Token validation failed` → problema con il token
- `Invalid issuer` → issuer non corrisponde
- `Unable to fetch JWKS` → problema di rete
- `User not found` → utente non esiste nel DB

### 5. Pulisci Cache Browser

```javascript
// Console del browser
localStorage.clear();
sessionStorage.clear();
location.reload();
```

Poi rifai il login.

## 📋 Checklist Finale

- [x] Issuer corretto in `application.yml`: `https://is.schema31.it/oauth2/token`
- [ ] Script SQL eseguito per creare utente `srossi@schema31.it`
- [ ] Utente verificato nel database con profilo SUPER_USER
- [ ] Backend riavviato
- [ ] Frontend riavviato
- [ ] Cache browser pulita
- [ ] Login effettuato
- [ ] Endpoint `/api/current-user` restituisce 200 OK
- [ ] Nessun errore CORS nel browser
- [ ] Log del backend mostrano "Security identity augmented"

## 🎯 Risultato Atteso

Dopo aver seguito questi passaggi:

1. ✅ Login funziona senza errori
2. ✅ `/api/current-user` restituisce 200 OK
3. ✅ Dashboard carica correttamente
4. ✅ Menu di navigazione è visibile
5. ✅ Liste (utenti, progetti, etc.) sono accessibili

## 📞 Supporto

Se il problema persiste dopo aver seguito tutti i passaggi:

1. Verifica i log del backend per errori specifici
2. Controlla la console del browser per errori JavaScript
3. Verifica che il database sia raggiungibile
4. Testa manualmente con cURL includendo il token JWT

### Test Manuale con cURL

```bash
# Copia il token dal localStorage
TOKEN="eyJ4NXQiOiJN..."

# Testa l'endpoint
curl -v \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/current-user
```

Analizza la risposta:
- **401**: Token non valido (issuer, firma, scadenza)
- **404**: Utente non trovato nel DB
- **200**: Tutto funziona! 🎉
