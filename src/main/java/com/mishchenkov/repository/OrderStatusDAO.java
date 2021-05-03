package com.mishchenkov.repository;

import com.mishchenkov.dto.OrderStatusDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderStatusDAO {

    long insertOrderStatus(Connection con, OrderStatusDTO orderStatusDTO) throws SQLException;

}
