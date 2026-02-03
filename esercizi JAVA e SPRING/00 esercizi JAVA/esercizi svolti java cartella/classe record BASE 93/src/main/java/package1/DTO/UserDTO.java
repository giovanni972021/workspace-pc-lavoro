// File: src/it/java/tutorial/dto/UserDTO.java
package DTO;

/**
 * Record professionale per il trasporto dati utente.
 * I record sono perfetti per i DTO perché garantiscono che i dati
 * non vengano manomessi durante il passaggio tra i vari livelli dell'app.
 */
public record UserDTO(
    Long id,
    String username,
    String email,
    boolean attivo) {
  // Validazione opzionale: non accettiamo username nulli o vuoti
  public UserDTO {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("username non può essere vuoto");
    }
  }
}