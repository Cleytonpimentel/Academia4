package Entidades;

public class Instrutor extends Pessoa {
    private String especialidade; // Adicionando o campo especialidade

    // Construtor com todos os parâmetros, incluindo os da classe Pessoa
    public Instrutor(int id, String nome, String cpf, String endereco, String telefone, String especialidade) {
        super(id, nome, cpf, endereco, telefone);  // Remover os parâmetros relacionados a rua, cidade, bairro e número
        this.especialidade = especialidade;
    }

    // Construtor sem o ID e com apenas nome, cpf e especialidade
    public Instrutor(String nome, String cpf, String especialidade) {
        super(nome, cpf); // Chama o construtor da classe Pessoa
        this.especialidade = especialidade;
    }

    // Getters e Setters
    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return super.toString() + ", Especialidade: " + especialidade;  // Mostra a especialidade
    }
}
