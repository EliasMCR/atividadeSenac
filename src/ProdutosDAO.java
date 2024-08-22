
import java.awt.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;

public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultSet;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto(ProdutosDTO produto) {
        String sql = "INSERT INTO produtos (nome, valor, status)VALUES (?,?,?)";
        conn = new conectaDAO().connectDB();
        try {
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());
            int rowsInserted = prep.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Registro salvo com sucesso!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto!");
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão");
            }
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos() {
        String sql = "SELECT * FROM produtos WHERE status = ?";
        try {
            conn = new conectaDAO().connectDB();
            prep = conn.prepareStatement(sql);
            prep.setString(1, "A Venda");
            resultSet = prep.executeQuery();
            while (resultSet.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setValor(resultSet.getInt("valor"));
                produto.setStatus(resultSet.getString("status"));
                listagem.add(produto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto!");
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão");
            }
            return listagem;
        }
    }
    
    public Boolean venderProduto(int id) {
        String selectSql = "SELECT * FROM produtos WHERE id = ?";
        String updateSql = "UPDATE produtos SET nome = ? WHERE id = ?";
        PreparedStatement prepSelect = null;
        PreparedStatement prepUpdate = null;
        Boolean sucesso = false;
        
        try {
            // Estabelecendo a conexão com o banco de dados
            conn = new conectaDAO().connectDB();

            // Preparando a instrução SQL para seleção
            prepSelect = conn.prepareStatement(selectSql);
            prepSelect.setInt(1, id);

            // Executando a consulta
            resultSet = prepSelect.executeQuery();

            // Verificando se o produto existe
            if (resultSet.next()) {
                // Preparando a instrução SQL para atualização
                prepUpdate = conn.prepareStatement(updateSql);
                prepUpdate.setString(1, "Vendido"); // Modificando o nome para "Vendido"
                prepUpdate.setInt(2, id);

                // Executando a atualização
                int rowsAffected = prepUpdate.executeUpdate();

                // Verificando se a atualização foi bem-sucedida
                if (rowsAffected > 0) {
                    sucesso = true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao vender produto: " + e.getMessage());
        } finally {
            // Fechando recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (prepSelect != null) {
                    prepSelect.close();
                }
                if (prepUpdate != null) {
                    prepUpdate.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão: " + ex.getMessage());
            }
        }
        
        return sucesso;
    }
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        String sql = "SELECT * FROM produtos WHERE status = ?";
        ArrayList<ProdutosDTO> listaVendido = new ArrayList<>();
        
        try {
            // Estabelecendo a conexão com o banco de dados
            conn = new conectaDAO().connectDB();

            // Preparando a instrução SQL
            prep = conn.prepareStatement(sql);
            prep.setString(1, "Vendido");

            // Executando a consulta
            resultSet = prep.executeQuery();

            // Processando o resultado da consulta
            while (resultSet.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setValor(resultSet.getInt("valor"));
                produto.setStatus(resultSet.getString("status"));
                listaVendido.add(produto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos vendidos: " + e.getMessage());
        } finally {
            // Fechando recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (prep != null) {
                    prep.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão: " + ex.getMessage());
            }
        }
        
        return listaVendido;
    }
    
}
