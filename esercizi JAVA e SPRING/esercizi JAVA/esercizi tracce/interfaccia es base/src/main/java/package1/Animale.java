package package1;

public class Animale implements Saluto {
    @Override
    public void saluta() {
        System.out.println("Ciao, sono animale!");
    }

    public void mangia() {
        System.out.println("Ciao, sono una animale che mangia!");
    }
}
