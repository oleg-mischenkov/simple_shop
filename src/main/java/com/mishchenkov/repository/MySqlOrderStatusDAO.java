package com.mishchenkov.repository;

import com.mishchenkov.dto.OrderStatusDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlOrderStatusDAO implements OrderStatusDAO {

    private final JDBCTemplate template;

    private static final String SQL_INSERT_ORDER_STATUS = "INSERT INTO order_status (`description`, statuses_id, orders_id) VALUES (?,?,?)";

    public MySqlOrderStatusDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public long insertOrderStatus(Connection con, OrderStatusDTO orderStatusDTO) throws SQLException {
        return template.update(con, SQL_INSERT_ORDER_STATUS,
                orderStatusDTO.getDescription(),
                orderStatusDTO.getStatusId(),
                orderStatusDTO.getOrderId());
    }
}
