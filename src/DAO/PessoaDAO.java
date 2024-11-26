package DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DatabaseConnection.DatabaseConnection;
import Entidades.Instrutor;
import Entidades.Membro;
import Entidades.Pessoa;
import Entidades.Plano;

public class PessoaDAO {

    private static int contadorId = 1; // Contador para gerar IDs únicos

    // Método para cadastrar uma pessoa
    public boolean cadastrarPessoa(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome, cpf, endereco, telefone, tipo_pessoa, plano, valor, especialidade) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pessoa.getNome()); // Nome
            ps.setString(2, pessoa.getCpf()); // CPF
            ps.setString(3, pessoa.getEndereco());  // Endereço
            ps.setString(4, pessoa.getTelefone());  // Telefone
            ps.setInt(5, pessoa instanceof Membro ? 1 : (pessoa instanceof Instrutor ? 2 : 0)); // Tipo de pessoa

            // Plano e Valor
            if (pessoa.getPlano() != null) {
                ps.setString(6, pessoa.getPlano().getNomePlano());  // Nome do plano
                ps.setDouble(7, pessoa.getPlano().getValor().doubleValue()); // Valor do plano
            } else {
                ps.setString(6, ""); // Sem plano
                ps.setDouble(7, 0.0); // Valor 0
            }

            // Especialidade
            if (pessoa instanceof Instrutor) {
                Instrutor instrutor = (Instrutor) pessoa;
                ps.setString(8, instrutor.getEspecialidade());
            } else {
                ps.setString(8, ""); // Sem especialidade
            }

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Obter ID gerado
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pessoa.setId(rs.getInt(1)); // Definir ID na entidade
                        return true; // Retorna sucesso
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna falha
    }

    // Método para atualizar uma pessoa
    public void update(Pessoa pessoa) {
        String sql = "UPDATE pessoa SET nome = ?, cpf = ?, endereco = ?, telefone = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getCpf());
            ps.setString(3, pessoa.getEndereco());
            ps.setString(4, pessoa.getTelefone());
            ps.setInt(5, pessoa.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para deletar uma pessoa pelo ID
    public boolean excluirPessoa(int id) {
        String sql = "DELETE FROM pessoa WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Retorna true se excluído, false caso contrário
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna falha
    }

    public List<Membro> getMembros() {
        List<Membro> membros = new ArrayList<>();
        List<Pessoa> pessoas = getPessoas();

        for (Pessoa pessoa : pessoas) {
          if (pessoa instanceof Membro) {
            membros.add((Membro) pessoa);
          }
        }

        return membros;
      }
    
    // Método para listar todas as pessoas
    public List<Pessoa> getPessoas() {
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, tipo_pessoa, plano, valor, endereco, telefone, especialidade "
                       + "FROM pessoa";

        try (Connection conn = DatabaseConnection.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rset = ps.executeQuery()) {

            while (rset.next()) {
                Pessoa pessoa = null;
                int tipoPessoa = rset.getInt("tipo_pessoa");

                if (tipoPessoa == 1) { // Tipo 1: Membro
                    Plano plano = new Plano(rset.getString("plano"), BigDecimal.valueOf(rset.getDouble("valor")));
                    plano.setId(rset.getInt("id"));

                    pessoa = new Membro(
                        rset.getString("nome"),
                        rset.getString("cpf"),
                        rset.getString("endereco"),
                        rset.getString("telefone"),
                        plano
                    );
                    pessoa.setId(rset.getInt("id"));
                } else if (tipoPessoa == 2) { // Tipo 2: Instrutor
                    pessoa = new Instrutor(
                        rset.getString("nome"),
                        rset.getString("cpf"),
                        rset.getString("especialidade")
                    );
                    pessoa.setId(rset.getInt("id"));
                    pessoa.setEndereco(rset.getString("endereco"));
                    pessoa.setTelefone(rset.getString("telefone"));
                }

                if (pessoa != null) {
                    pessoas.add(pessoa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoas;
    }

    // Método para buscar um membro pelo ID
    public Membro buscarMembroPorId(int id) {
        String sql = "SELECT id, nome, cpf, endereco, telefone, plano, valor "
                   + "FROM pessoa WHERE id = ? AND tipo_pessoa = 1";

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rset = ps.executeQuery()) {
                if (rset.next()) {
                    Plano plano = new Plano(rset.getString("plano"), BigDecimal.valueOf(rset.getDouble("valor")));
                    plano.setId(rset.getInt("id"));

                    Membro membro = new Membro(
                        rset.getString("nome"),
                        rset.getString("cpf"),
                        rset.getString("endereco"),
                        rset.getString("telefone"),
                        plano
                    );
                    membro.setId(rset.getInt("id"));
                    return membro;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

	public boolean atualizarMembro(Membro membroAtualizar) {
		// TODO Auto-generated method stub
		return false;
	}
}
