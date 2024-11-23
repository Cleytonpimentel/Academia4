package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DatabaseConnection.DatabaseConnection;
import Entidades.Instrutor;

public class InstrutorDAO {

    private Connection connection;

    // Construtor para inicializar a conexão com o banco de dados
    public InstrutorDAO() {
        this.connection = DatabaseConnection.getConexao();
    }

    // Método para garantir que a conexão está aberta antes de qualquer operação
    private void verificarConexao() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DatabaseConnection.getConexao();
        }
    }

    // Método para adicionar um instrutor no banco de dados
    public void adicionarInstrutor(Instrutor instrutor) {
        String sql = "INSERT INTO instrutor (nome, cpf, especialidade, endereco, telefone) " +
                     "VALUES (?, ?, ?, ?, ?)";  // Removido rua, cidade, bairro e numero
        
        try {
            verificarConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, instrutor.getNome());
                stmt.setString(2, instrutor.getCpf());
                stmt.setString(3, instrutor.getEspecialidade());
                stmt.setString(4, instrutor.getEndereco());
                stmt.setString(5, instrutor.getTelefone());
                
                stmt.executeUpdate();
                System.out.println("Instrutor " + instrutor.getNome() + " adicionado com sucesso.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar instrutor: " + instrutor.getNome(), e);
        }
    }

    // Método para buscar um instrutor pelo CPF
    public Instrutor buscarInstrutor(String cpf) {
        String sql = "SELECT * FROM instrutor WHERE cpf = ?";
        Instrutor instrutor = null;

        try {
            verificarConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, cpf);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    instrutor = new Instrutor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("endereco"),
                            rs.getString("telefone"),
                            rs.getString("especialidade")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar instrutor com CPF: " + cpf, e);
        }

        return instrutor;
    }

    // Método para listar todos os instrutores
    public List<Instrutor> listarTodos() {
        String sql = "SELECT * FROM instrutor";
        List<Instrutor> instrutores = new ArrayList<>();
        
        try {
            verificarConexao();
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rset = ps.executeQuery()) {

                while (rset.next()) {
                    Instrutor instrutor = new Instrutor(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("cpf"),
                            rset.getString("endereco"),
                            rset.getString("telefone"),
                            rset.getString("especialidade")
                    );
                    instrutores.add(instrutor);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar instrutores", e);
        }

        return instrutores;
    }

    // Método para atualizar um instrutor
    public void atualizarInstrutor(Instrutor instrutor) {
        String sql = "UPDATE instrutor SET nome = ?, especialidade = ?, endereco = ?, telefone = ? WHERE cpf = ?";  // Removido rua, cidade, bairro e numero

        try {
            verificarConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, instrutor.getNome());
                stmt.setString(2, instrutor.getEspecialidade());
                stmt.setString(3, instrutor.getEndereco());
                stmt.setString(4, instrutor.getTelefone());
                stmt.setString(5, instrutor.getCpf());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Instrutor " + instrutor.getNome() + " atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum instrutor foi atualizado para o CPF: " + instrutor.getCpf());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o instrutor com CPF: " + instrutor.getCpf(), e);
        }
    }

    // Método para remover um instrutor
    public void removerInstrutor(String cpf) {
        String sql = "DELETE FROM instrutor WHERE cpf = ?";

        try {
            verificarConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, cpf);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Instrutor com CPF " + cpf + " removido com sucesso!");
                } else {
                    System.out.println("Nenhum instrutor foi removido para o CPF: " + cpf);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover instrutor com CPF: " + cpf, e);
        }
    }
}
