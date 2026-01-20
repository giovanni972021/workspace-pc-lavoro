package main.java.package1;

class Persona {
    private String nome;

    // Il parametro del costruttore si chiama come l'attributo, ossia nome
    public Persona(String nome) {
        // Senza 'this', qui si assegnerebbe il parametro a se stesso!
        this.nome = nome;
    }

    public void stampaNome() {
        System.out.println("Il nome è: " + this.nome);
    }
}