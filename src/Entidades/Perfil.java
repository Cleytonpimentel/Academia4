package Entidades;

public class Perfil {
    private String sexo;
    private int idade;
    private double altura;
    private double peso;

    // Construtor completo
    public Perfil(String sexo, int idade, double altura, double peso) {
        this.sexo = sexo;
        this.idade = idade;
        this.altura = altura;
        this.peso = peso;
    }

    // Construtor vazio
    public Perfil() {}

    // Getters e Setters
    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Perfil{" +
               "sexo='" + sexo + '\'' +
               ", idade=" + idade +
               ", altura=" + altura +
               ", peso=" + peso +
               '}';
    }
}
