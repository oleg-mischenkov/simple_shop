package com.mishchenkov.repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDAO {

    long insertOrder(Connection con, long userId, long deliveryId, long paymentId) throws SQLException;

}
