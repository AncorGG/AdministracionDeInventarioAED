/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.administraciondeinventario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ancor
 */
public class ProductDAO {

    //READ
    public List<String> getDeposits() {
        List<String> entidades = new ArrayList<>();
        String sql = "SELECT * FROM Deposit";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                entidades.add(rs.getInt("id_deposit") + " - "
                        + rs.getString("description") + " - " + rs.getString("localization"));
            }
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entidades;
    }

    public List<String> getProducts() {
        List<String> entidades = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                entidades.add(rs.getInt("id_product") + " - "
                        + rs.getString("cod_product") + " - " + rs.getString("name")
                        + " - " + rs.getString("description") + " - " + rs.getFloat("price") + " - " + rs.getString("shelf"));
            }
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entidades;
    }

    public String[] getProductByTable(int position) {
        String[] productDetails = new String[4];
        String sql = "SELECT id_product, name, price, description FROM Products LIMIT 1 OFFSET ?";

        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, position);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    productDetails[0] = rs.getString("id_product");
                    productDetails[1] = rs.getString("name");
                    productDetails[2] = rs.getString("price");
                    productDetails[3] = rs.getString("description");
                }
            }

            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDetails;
    }

    public String[] getDepositByTable(int position) {
        String[] depositDetails = new String[3];
        String sql = "SELECT id_deposit, description, localization FROM Deposit LIMIT 1 OFFSET ?";

        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, position);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    depositDetails[0] = rs.getString("id_deposit");
                    depositDetails[1] = rs.getString("description");
                    depositDetails[2] = rs.getString("localization");
                }
            }

            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return depositDetails;
    }

    public List<String> getStored() {
        List<String> entidades = new ArrayList<>();
        String sql = "SELECT * FROM Stored";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                entidades.add(rs.getInt("id_deposit") + " - "
                        + rs.getInt("id_product") + " - " + rs.getInt("quantity"));
            }
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entidades;
    }

    public List<String> getComponents() {
        List<String> entidades = new ArrayList<>();
        String sql = "SELECT * FROM Components";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                entidades.add(rs.getInt("id_prod_parent") + " - "
                        + rs.getInt("id_prod_child"));
            }
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entidades;
    }

    public List<String> getProductsInDeposit(int id_deposit) {
        List<String> products = new ArrayList<>();
        String sql = "SELECT p.id_product, p.cod_product, p.name, s.quantity "
                + "FROM Products p "
                + "INNER JOIN Stored s ON p.id_product = s.id_product "
                + "WHERE s.id_deposit = ?";

        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id_deposit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(rs.getString("cod_product") + " - "
                        + rs.getString("name") + " - "
                        + rs.getInt("quantity"));
            }

            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<String> getDepositsOfProduct(int id_product) {
        List<String> deposits = new ArrayList<>();
        String sql = "SELECT d.id_deposit, d.description, d.localization "
                + "FROM Deposit d "
                + "INNER JOIN Stored s ON d.id_deposit = s.id_deposit "
                + "WHERE s.id_product = ?";

        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id_product);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                deposits.add(rs.getInt("id_deposit") + " - "
                        + rs.getString("description") + " - "
                        + rs.getString("localization"));
            }

            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deposits;
    }

    public List<String> getChildComponents(int parentId) {
        List<String> childComponents = new ArrayList<>();
        String sql = "SELECT p.id_product, p.cod_product, p.name, p.description, p.shelf, p.price "
                + "FROM Products p "
                + "JOIN Components c ON p.id_product = c.id_prod_child "
                + "WHERE c.id_prod_parent = ?";

        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, parentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                
                String productDetails = 
                        rs.getString("cod_product") + " - "
                        + rs.getString("name");
                childComponents.add(productDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return childComponents;
    }

    public List<String> getParentComponents(int childId) {
        List<String> parentComponents = new ArrayList<>();
        String sql = "SELECT p.id_product, p.cod_product, p.name, p.description, p.shelf, p.price "
                + "FROM Products p "
                + "JOIN Components c ON p.id_product = c.id_prod_parent "
                + "WHERE c.id_prod_child = ?";

        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, childId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String productDetails = rs.getString("cod_product")  
                        + " - " + rs.getString("name");
                parentComponents.add(productDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parentComponents;
    }

    // UPDATE
    public void updateProducto(int idProd, String codProd, String name, String description, double price, String shelf) {
        String sql = "UPDATE Products SET cod_product = ?, name = ?, description = ?, price = ?, shelf = ? WHERE id_product = ?";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codProd);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setDouble(4, price);
            stmt.setString(5, shelf);
            stmt.setInt(6, idProd);
            stmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDeposit(int idDep, String name, String description, String loc) {
        String sql = "UPDATE Deposit SET name = ?, description = ?, localization = ? WHERE id_product = ?";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, loc);
            stmt.setInt(4, idDep);
            stmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStored(int idDep, int idProd, int quantity) {
        String sql = "UPDATE Stored SET quantity = ? WHERE id_deposit = ? AND id_product = ?";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, idDep);
            stmt.setInt(3, idProd);
            stmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCompone(int parentProdId, int childProdId, int newParentProdId, int newChildProdId) {
        String deleteSql = "DELETE FROM Components WHERE id_prod_parent = ? AND id_prod_child = ?";
        String insertSql = "INSERT INTO Components (id_prod_parent, id_prod_child) VALUES (?, ?)";

        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement deleteStmt = connection.prepareStatement(deleteSql); PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

            deleteStmt.setInt(1, parentProdId);
            deleteStmt.setInt(2, childProdId);
            deleteStmt.executeUpdate();

            insertStmt.setInt(1, newParentProdId);
            insertStmt.setInt(2, newChildProdId);
            insertStmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //CREATE
    public void createProduct(String codProd, String name, String description, double price, String shelf) {
        String sql = "INSERT INTO Products (cod_prod, name, description, price, shelf) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codProd);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setDouble(4, price);
            stmt.setString(5, shelf);
            stmt.executeUpdate();
            System.out.println("Producto insertado correctamente.");
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDeposit(String name, String description, String loc) {
        String sql = "INSERT INTO Deposit (name, description, localization) VALUES (?, ?)";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, loc);
            stmt.executeUpdate();
            System.out.println("Depósito insertado correctamente.");
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createStored(int idDep, int idProd, int quantity) {
        String sql = "INSERT INTO Stored (id_deposit, id_product, quantity) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDep);
            stmt.setInt(2, idProd);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
            System.out.println("Registro de almacenaje insertado correctamente.");
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createComponent(int parentProdId, int childProdId) {
        String sql = "INSERT INTO Components (id_prod_parent, id_prod_child) VALUES (?, ?)";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, parentProdId);
            stmt.setInt(2, childProdId);
            stmt.executeUpdate();
            System.out.println("Relación de composición insertada correctamente.");
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DDELETE
    public void deleteProduct(int idProd) {
        String sql = "DELETE FROM Products WHERE id_product = ?";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProd);
            stmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDeposit(int idDep) {
        String sql = "DELETE FROM Deposit WHERE id_deposit = ?";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDep);
            stmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStored(int idDep, int idProd) {
        String sql = "DELETE FROM Stored WHERE id_deposit = ? AND id_product = ?";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDep);
            stmt.setInt(2, idProd);
            stmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCompone(int parentProdId, int childProdId) {
        String sql = "DELETE FROM Components WHERE id_prod_parent = ? AND id_prod_child = ?";
        try (Connection connection = ConnectionDB.OpenConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, parentProdId);
            stmt.setInt(2, childProdId);
            stmt.executeUpdate();
            ConnectionDB.CloseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
