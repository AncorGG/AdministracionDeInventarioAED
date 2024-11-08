/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.administraciondeinventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ancor
 */
public class ProductDAO {

    private Connection connection;

    public void updateProducto(int idProd, String codProd, String name, double price, String shelf) {
        String sql = "UPDATE Product SET cod_prod = ?, name = ?, price = ?, shelf = ? WHERE id_product = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codProd);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setString(4, shelf);
            stmt.setInt(5, idProd);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDeposit(int idDep, String name, String loc) {
        String sql = "UPDATE Deposit SET name = ?, localization = ? WHERE id_product = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, loc);
            stmt.setInt(3, idDep);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStored(int idDep, int idProd, int quantity) {
        String sql = "UPDATE Stored SET quantity = ? WHERE id_deposit = ? AND id_product = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, idDep);
            stmt.setInt(3, idProd);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCompone(int parentProdId, int childProdId, int newParentProdId, int newChildProdId) {
        String deleteSql = "DELETE FROM Compone WHERE id_prod_parent = ? AND id_prod_child = ?";
        String insertSql = "INSERT INTO Compone (id_prod_parent, id_prod_child) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql); PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

            deleteStmt.setInt(1, parentProdId);
            deleteStmt.setInt(2, childProdId);
            deleteStmt.executeUpdate();

            insertStmt.setInt(1, newParentProdId);
            insertStmt.setInt(2, newChildProdId);
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
