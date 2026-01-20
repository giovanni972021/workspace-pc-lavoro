/*

1 dichiaro attributi e i loro get

2 dichiaro costruttore     
-    public Persona(String nome, int eta) {
3 TO STRING per concatenare
-    public String toString() {

*/

public class Persona {

    private String nome;
    private int eta;

    public Persona(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    public String getNome() {
        return nome;
    }

    public int getEta() {
        return eta;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + ", Età: " + eta;
    }
}
