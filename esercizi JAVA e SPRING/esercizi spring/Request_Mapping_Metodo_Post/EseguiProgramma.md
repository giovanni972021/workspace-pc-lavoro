/\*

- per TESTARLO
- nel TERMINALE
-
- mvn clean SE NECESSARIO
- mvn spring-boot:run

apri chrome-extension://aejoelaoggembcahagimdiliamlcdmfm/index.html
method POST

url http://localhost:8080/prodotti

nel body scrivo
{
"id": 100,
"nome": "Monitor Gaming",
"prezzo": 44299.99
}

in alto a destra clic su SEND

SE TUTTO OK vado nel wweb , scrivo

http://localhost:8080/prodotti

e dovrei vedere i prodotti inseriti
