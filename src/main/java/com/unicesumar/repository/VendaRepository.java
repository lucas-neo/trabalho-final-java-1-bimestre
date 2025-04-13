package com.unicesumar.repository;

import com.unicesumar.entities.Product;
import com.unicesumar.entities.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class VendaRepository {

    private final Connection connection;

    public VendaRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Venda venda) {
        String insertSale = "INSERT INTO sales (id, user_id, payment_method) VALUES (?, ?, ?)";
        String insertSaleProduct = "INSERT INTO sale_products (sale_id, product_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            PreparedStatement saleStmt = connection.prepareStatement(insertSale);
            saleStmt.setString(1, venda.getUuid().toString());
            saleStmt.setString(2, venda.getUser().getUuid().toString());
            saleStmt.setString(3, venda.getPaymentMethod().name());
            saleStmt.executeUpdate();

            PreparedStatement productStmt = connection.prepareStatement(insertSaleProduct);
            for (Product produto : venda.getProdutos()) {
                productStmt.setString(1, venda.getUuid().toString());
                productStmt.setString(2, produto.getUuid().toString());
                productStmt.addBatch();
            }
            productStmt.executeBatch();

            connection.commit();
            System.out.println("Venda registrada com sucesso!");
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Erro ao salvar venda. Transação revertida.");
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Erro ao realizar rollback", rollbackEx);
            }
            throw new RuntimeException("Erro ao salvar venda", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao restaurar auto-commit", e);
            }
        }
    }
}
