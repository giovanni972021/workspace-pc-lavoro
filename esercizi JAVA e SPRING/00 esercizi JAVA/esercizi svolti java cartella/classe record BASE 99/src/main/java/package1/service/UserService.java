package service;

import java.util.ArrayList;
import java.util.List;

import DTO.UserDTO;

public class UserService {

  private final List<UserDTO> utenti = new ArrayList<>();

  public void addUser(UserDTO user) {
    utenti.add(user);
  }

  public List<UserDTO> getAllUsers() {
    return utenti;
  }

  public List<UserDTO> getActiveUsers() {
    List<UserDTO> attivi = new ArrayList<>();
    for (UserDTO u : utenti) {
      if (u.attivo()) {
        attivi.add(u);
      }
    }
    return attivi;
  }
}
