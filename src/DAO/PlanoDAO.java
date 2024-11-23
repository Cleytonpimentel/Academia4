package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;  // Importando List e ArrayList
import java.util.List;

import DatabaseConnection.DatabaseConnection;
import Entidades.Plano;

public class PlanoDAO {

    // Método para adicionar um plano no banco
    public void adicionarPlano(Plano plano) {
        String sql = "INSERT INTO plano (nome, valor) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plano.getNome());
            ps.setBigDecimal(2, plano.getValor());  // Usando BigDecimal para o valor
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para listar planos do banco
    public List<Plano> listarPlanos() {
        List<Plano> planos = new ArrayList<>();
        String sql = "SELECT id, nome, valor FROM plano";

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rset = ps.executeQuery()) {

            while (rset.next()) {
                // Usando BigDecimal para o valor do plano
                Plano plano = new Plano(rset.getString("nome"), rset.getBigDecimal("valor"));
                plano.setId(rset.getInt("id"));
                planos.add(plano);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return planos;
    }

    // Método para buscar um plano pelo ID
    public Plano buscarPlanoPorId(int id) {
        String sql = "SELECT id, nome, valor FROM plano WHERE id = ?";
        Plano plano = null;

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id); // Definir o ID do plano no PreparedStatement
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Usando BigDecimal para o valor do plano
                plano = new Plano(rs.getString("nome"), rs.getBigDecimal("valor"));
                plano.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plano;
    }
}
