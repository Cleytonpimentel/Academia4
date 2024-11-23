package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DatabaseConnection.DatabaseConnection;
import Entidades.Membro;
import Entidades.Perfil;
import Entidades.Plano;

public class MembroDAO {

    // Verifica se o plano existe na tabela plano
    private boolean planoExiste(int planoId) {
        String sql = "SELECT id FROM plano WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Retorna true se o plano existir
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a existência do plano: " + e.getMessage());
            return false;
        }
    }

    // Método para cadastrar um membro
    public void cadastrarMembro(Membro membro) {
        if (planoExiste(membro.getPlano().getId())) { // Verifica se o plano existe
            String sql = "INSERT INTO membro (nome, cpf, plano_id, endereco, telefone) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, membro.getNome());
                ps.setString(2, membro.getCpf());
                ps.setInt(3, membro.getPlano().getId());
                ps.setString(4, membro.getEndereco());
                ps.setString(5, membro.getTelefone());

                ps.executeUpdate();  // Executa a inserção

                // Recupera o ID gerado automaticamente para o membro
                ResultSet rs = ps.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    int membroId = rs.getInt(1); // ID do membro recém inserido
                    membro.setId(membroId); // Atribui o ID gerado ao objeto membro

                    // Agora, cadastramos o perfil, associando-o ao membro
                    Perfil perfil = membro.getPerfil(); // Perfil do membro
                    PerfilDAO perfilDAO = new PerfilDAO();
                    
                    // Vamos passar o ID do membro para o perfil
                    boolean perfilCadastrado = perfilDAO.cadastrarPerfil(perfil, membroId);

                    if (perfilCadastrado) {
                        System.out.println("Perfil cadastrado com sucesso!");
                    } else {
                        System.out.println("Falha ao cadastrar perfil.");
                    }
                } else {
                    System.out.println("Erro: ID gerado não recuperado.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao cadastrar membro: " + e.getMessage());
            }
        } else {
            System.out.println("Plano não encontrado. Não é possível cadastrar o membro.");
        }
    }

    // Método para buscar membro por ID
    public Membro buscarMembroPorId(int id) {
        String sql = "SELECT m.id, m.nome, m.cpf, m.plano_id, m.endereco, m.telefone, "
                   + "p.sexo, p.idade, p.altura, p.peso, "
                   + "pl.nome AS plano_nome, pl.valor AS plano_valor "
                   + "FROM membro m "
                   + "JOIN plano pl ON m.plano_id = pl.id "
                   + "LEFT JOIN perfil p ON m.id = p.membro_id "
                   + "WHERE m.id = ?";

        Membro membro = null;

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Cria o objeto Plano
                Plano plano = new Plano(rs.getString("plano_nome"), rs.getDouble("plano_valor"));
                plano.setId(rs.getInt("plano_id"));

                // Cria o objeto Perfil
                Perfil perfil = null;
                if (rs.getString("sexo") != null) {
                    perfil = new Perfil(
                        rs.getString("sexo"),
                        rs.getInt("idade"),
                        rs.getDouble("altura"),
                        rs.getDouble("peso")
                    );
                }

                // Cria o objeto Membro com os dados recuperados
                membro = new Membro(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("endereco"),
                    rs.getString("telefone"),
                    plano,
                    perfil
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar membro por ID: " + e.getMessage());
        }

        return membro;
    }

    // Método para atualizar um membro
    public void atualizarMembro(Membro membro) {
        if (planoExiste(membro.getPlano().getId())) { // Verifica se o plano existe
            String sql = "UPDATE membro SET nome = ?, cpf = ?, plano_id = ?, endereco = ?, telefone = ? WHERE id = ?";

            try (Connection conn = DatabaseConnection.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, membro.getNome());
                ps.setString(2, membro.getCpf());
                ps.setInt(3, membro.getPlano().getId());
                ps.setString(4, membro.getEndereco());
                ps.setString(5, membro.getTelefone());
                ps.setInt(6, membro.getId());

                // Executa a atualização do membro
                ps.executeUpdate();

                // Atualiza o perfil associado ao membro
                PerfilDAO perfilDAO = new PerfilDAO();
                Perfil perfil = membro.getPerfil();  // Assume que o membro tem um perfil associado
                if (perfil != null) {
                    boolean sucessoPerfil = perfilDAO.atualizarPerfil(perfil, membro.getId());
                    if (sucessoPerfil) {
                        System.out.println("Perfil do membro atualizado com sucesso!");
                    } else {
                        System.out.println("Falha ao atualizar o perfil do membro.");
                    }
                }

            } catch (SQLException e) {
                System.out.println("Erro ao atualizar membro: " + e.getMessage());
            }
        } else {
            System.out.println("Plano não encontrado. Não é possível atualizar o membro.");
        }
    }

    // Método para excluir um membro
    public void excluirMembro(int id) {
        String sql = "DELETE FROM membro WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir membro: " + e.getMessage());
        }
    }

    // Método para listar todos os membros
    public List<Membro> listarMembros() {
        String sql = "SELECT m.id, m.nome, m.cpf, m.plano_id, m.endereco, m.telefone, "
                     + "p.sexo, p.idade, p.altura, p.peso, "
                     + "pl.nome AS plano_nome, pl.valor AS plano_valor "
                     + "FROM membro m "
                     + "LEFT JOIN perfil p ON m.id = p.membro_id "
                     + "JOIN plano pl ON m.plano_id = pl.id";

        List<Membro> membros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Cria o objeto Plano
                Plano plano = new Plano(rs.getString("plano_nome"), rs.getDouble("plano_valor"));
                plano.setId(rs.getInt("plano_id"));

                // Cria o objeto Perfil
                Perfil perfil = null;
                if (rs.getString("sexo") != null) {
                    perfil = new Perfil(
                        rs.getString("sexo"),
                        rs.getInt("idade"),
                        rs.getDouble("altura"),
                        rs.getDouble("peso")
                    );
                }

                // Cria o objeto Membro
                Membro membro = new Membro(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("endereco"),
                    rs.getString("telefone"),
                    plano,
                    perfil
                );

                // Adiciona o membro à lista
                membros.add(membro);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar membros: " + e.getMessage());
        }

        return membros;
    }
}
