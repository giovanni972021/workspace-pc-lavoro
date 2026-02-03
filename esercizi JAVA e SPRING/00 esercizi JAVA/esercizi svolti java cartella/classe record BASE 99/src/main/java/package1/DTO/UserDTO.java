package DTO;

public record UserDTO(
    Long id,
    String username,
    String email,
    boolean attivo) {

  public UserDTO {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Username non valido");
    }

    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email non valida");
    }
  }
}
