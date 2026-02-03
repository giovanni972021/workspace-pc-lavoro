// File: src/it/java/tutorial/main/App.java
package Main;

import DTO.UserDTO;

public class Main {
  public static void main(String[] args) {

    // Creazione di un'istanza del DTO
    UserDTO utente = new UserDTO(1L, "mario_rossi", "mario@example.com", true);

    // Visualizzazione pulita grazie al toString() automatico
    System.out.println("\nUtente creato correttamente:\n" + utente + "\n");

    // Esempio di logica professionale: accesso ai campi
    if (utente.attivo()) {
      System.out.println("L'email di " + utente.username() + " è: " + utente.email() + "\n\n");
    }

    // Nota: non puoi fare utente.attivo = false; (Immutabilità)
  }
}