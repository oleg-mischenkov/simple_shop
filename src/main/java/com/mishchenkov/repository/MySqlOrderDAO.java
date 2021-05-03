package com.mishchenkov.repository;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlOrderDAO implements OrderDAO {

    private static final String SQL_INSERT_USER = "INSERT INTO orders (users_id, delivery_id, payment_id) VALUES (?,?,?)";

    private final JDBCTemplate template;

    public MySqlOrderDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public long insertOrder(Connection con, long userId, long deliveryId, long paymentId) throws SQLException {
        return template.update(con, SQL_INSERT_USER, userId, deliveryId, paymentId);
    }
}
