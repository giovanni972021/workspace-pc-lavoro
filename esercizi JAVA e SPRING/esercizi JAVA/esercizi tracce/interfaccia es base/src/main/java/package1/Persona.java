package package1;

public class Persona implements Saluto {
    @Override
    public void saluta() {
        System.out.println("Ciao, sono una persona!");
    }

    public void mangia() {
        System.out.println("Ciao, sono una persona che mangia!");
    }
}
