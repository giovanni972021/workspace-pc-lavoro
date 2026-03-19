// T sta per "Type" (puoi usare qualsiasi lettera, ma T è lo standard)
public class Scatola<T> {
  private T contenuto;

  public void inserisci(T oggetto) {
    this.contenuto = oggetto;
  }

  public T estrai() {
    return contenuto;
  }
}