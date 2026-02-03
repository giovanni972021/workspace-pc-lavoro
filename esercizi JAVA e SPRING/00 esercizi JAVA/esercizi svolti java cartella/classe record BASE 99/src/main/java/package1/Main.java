import DTO.UserDTO;
import service.UserService;

public class Main {

  public static void main(String[] args) {

    UserService userService = new UserService();

    userService.addUser(new UserDTO(1L, "mario_rossi", "mario@example.com", true));
    userService.addUser(new UserDTO(2L, "luigi_verdi", "luigi@example.com", false));
    userService.addUser(new UserDTO(3L, "anna_bianchi", "anna@example.com", true));

    System.out.println("\nStamppa TUTTI GLI UTENTI:\n");
    for (UserDTO u : userService.getAllUsers()) {
      System.out.println(u);
    }

    System.out.println("\nStampa UTENTI ATTIVI:");
    for (UserDTO u : userService.getActiveUsers()) {
      System.out.println(u.username());
      System.out.println("\n");
    }
  }
}
