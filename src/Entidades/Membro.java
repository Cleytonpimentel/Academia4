package Entidades;

public class Membro extends Pessoa {
    private String endereco;
    private String telefone;
    private Plano plano;
    private Perfil perfil; // Novo atributo para associar o perfil

    // Construtor com todos os parâmetros necessários (incluindo o perfil)
    public Membro(int id, String nome, String cpf, String endereco, String telefone, Plano plano, Perfil perfil) {
        super(id, nome, cpf); // Chama o construtor da classe Pessoa
        this.endereco = endereco;
        this.telefone = telefone;
        this.plano = plano;
        this.perfil = perfil;
    }

    // Construtor sem ID (útil para criação de novos membros, onde o ID será gerado automaticamente)
    public Membro(String nome, String cpf, String endereco, String telefone, Plano plano, Perfil perfil) {
        super(0, nome, cpf); // Define 0 como valor padrão para o ID
        this.endereco = endereco;
        this.telefone = telefone;
        this.plano = plano;
        this.perfil = perfil;
    }

    // Construtor básico (sem perfil)
    public Membro(String nome, String cpf, String endereco, String telefone, Plano plano) {
        super(0, nome, cpf); // Define 0 como valor padrão para o ID
        this.endereco = endereco;
        this.telefone = telefone;
        this.plano = plano;
        this.perfil = new Perfil(); // Inicializa um perfil vazio por padrão
    }

    // Getters e setters para os atributos

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return "Membro{" +
               "ID=" + getId() +
               ", Nome='" + getNome() + '\'' +
               ", CPF='" + getCpf() + '\'' +
               ", Endereço='" + endereco + '\'' +
               ", Telefone='" + telefone + '\'' +
               ", Plano=" + (plano != null ? plano.getNomePlano() : "Sem plano") +
               ", Perfil=" + (perfil != null ? perfil.toString() : "Sem perfil") +
               '}';
    }
}
