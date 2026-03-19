package src.main.java.package1;

import java.util.Arrays;
import java.util.List;

public class EsempioMetodoGenerics {

  public static void main(String[] args) {

    List<Integer> numeri = Arrays.asList(1, 2, 3, 4);
    List<String> parole = Arrays.asList("java", "python", "c++");

    Integer primoNumero = Util.primoElemento(numeri);
    String primaParola = Util.primoElemento(parole);

    System.out.println(primoNumero);
    System.out.println(primaParola);

  }

}