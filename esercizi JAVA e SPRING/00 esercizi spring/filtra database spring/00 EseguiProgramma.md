per **TESTARE programma**

nel **TERMINALE**
./mvnw spring-boot:run
questo codice qui sopra va eseguito nella cartella che contiene i file
.mvn, .vscode ecc ecc NON da altre parti altrimenti NON funziona

**sul web**
http://localhost:8080/swagger-ui/index.html **per guida alle api**

**api/projects** è cio che ho scritto nella classe situata dentro la cartella controller
@GetMapping("/api") e "projects"

**8080** è il numero di porta di default, se non dovesse funzionare, vedi cosa ce scritto nel file **application.properties** nella cartella "resources"

server.port=8081
in QUESTO caso **8081** è la porta dove verra eseguito il programma in locale

**SE TUTTO OK** vedo ciao mondo
