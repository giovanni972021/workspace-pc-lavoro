## OBIETTIVO

estrarre solo alcuni dati da database situato su dbeaver

## 1 modifico APPLICATION PROPERTIES

vedi
Configurazione del database MySQL

**hr_schema31** è nome del database, come lo trovo?
APRO db eaver, in alto a sx apro menu a tendina my sql
apro tendina DATABASES

dentro la tendina databases trovo i **NOMI** di tutti i **DATABASE**

**NOME UTENTE e PASSWORD** dove li trovo?
semopre su DB EAVER
tasto dx su MY SQL
modifica connessione
.
qui vedo nome utente che dovrebbe essere **root**
e password

Configurazione di JPA (Hibernate)
Configurazione del dialect di MySQL per Hibernate
.
.
.

## 2 creo CLASSE USER

creo cartella **ENTITY**
e al suo interno la classe **User.java** per mappare la tabella del database e per permettermi di interagire con database

## 3 Creare il Repository per il database

nella stessa posizione in cui ho creato la cartella controller
creo cartella di nome **repository**
.
.
creo interfaccia di nome **UserRepository**
che mi permette di eseguire OPERAZIONI CRUD sulla tabella users del database
.
.
