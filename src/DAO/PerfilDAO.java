package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DatabaseConnection.DatabaseConnection;
import Entidades.Perfil;

public class PerfilDAO {

    // Método para cadastrar um perfil associado a um membr
		 public boolean cadastrarPerfil(Perfil perfil, int membroId) {
		     String sql = "INSERT INTO perfil (membro_id, sexo, idade, altura, peso) VALUES (?, ?, ?, ?, ?)";

		     try (Connection conn = DatabaseConnection.getConexao();
		          PreparedStatement ps = conn.prepareStatement(sql)) {

		         ps.setInt(1, membroId);  // Associando o membro à tabela perfil
		         ps.setString(2, perfil.getSexo());
		         ps.setInt(3, perfil.getIdade());
		         ps.setDouble(4, perfil.getAltura());
		         ps.setDouble(5, perfil.getPeso());

		         int rowsAffected = ps.executeUpdate();
		         return rowsAffected > 0;  // Se inseriu com sucesso, retorna verdadeiro
		     } catch (SQLException e) {
		         System.out.println("Erro ao cadastrar perfil: " + e.getMessage());
		         return false;
		     }
		 }

	private boolean membroExiste(int membroId) {
	    String sql = "SELECT COUNT(*) FROM membro WHERE id = ?";
	    try (Connection conn = DatabaseConnection.getConexao();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, membroId);
	        try (ResultSet rs = ps.executeQuery()) {
	            return rs.next() && rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Em caso de erro, retorna falso
	    }
	}

    private void validarPerfil(Perfil perfil) throws IllegalArgumentException {
        // Implementar as validações específicas para o perfil
        if (perfil.getIdade() <= 0) {
            throw new IllegalArgumentException("Idade deve ser maior que zero");
        }
            }

    // Método para buscar um perfil associado ao ID do membro
	public Perfil buscarPerfilPorMembroId(int membroId) {
	    String sql = "SELECT * FROM perfil WHERE membro_id = ?";
	    Perfil perfil = null;

	    try (Connection conn = DatabaseConnection.getConexao();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, membroId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            perfil = new Perfil(
	                rs.getString("sexo"),
	                rs.getInt("idade"),
	                rs.getDouble("altura"),
	                rs.getDouble("peso")
	            );
	        }

	    } catch (SQLException e) {
	        System.out.println("Erro ao buscar perfil: " + e.getMessage());
	    }

	    return perfil;
	}

    // Método para atualizar o perfil de um membro
	public boolean atualizarPerfil(Perfil perfil, int membroId) {
	    String sql = "UPDATE perfil SET sexo = ?, idade = ?, altura = ?, peso = ? WHERE membro_id = ?";
	    try (Connection conn = DatabaseConnection.getConexao();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, perfil.getSexo());
	        ps.setInt(2, perfil.getIdade());
	        ps.setDouble(3, perfil.getAltura());
	        ps.setDouble(4, perfil.getPeso());
	        ps.setInt(5, membroId); // Vincula o ID do membro

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        System.out.println("Erro ao atualizar perfil: " + e.getMessage());
	    }
	    return false;
	}
    // Método para excluir o perfil de um membro
	public boolean excluirPerfil(int membroId) {
	    String sql = "DELETE FROM perfil WHERE membro_id = ?";

	    try (Connection conn = DatabaseConnection.getConexao();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, membroId);

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        System.out.println("Erro ao excluir perfil: " + e.getMessage());
	    }
	    return false; // Caso haja falha
	}
}
