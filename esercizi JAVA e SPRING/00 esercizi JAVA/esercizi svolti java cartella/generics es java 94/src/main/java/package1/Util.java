package src.main.java.package1;

import java.util.List;

public class Util {

  public static <T> T primoElemento(List<T> lista) {
    if (lista == null || lista.isEmpty()) {
      return null;
    }
    return lista.get(0);
  }

}