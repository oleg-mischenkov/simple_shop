package com.mishchenkov.repository;

import com.mishchenkov.dto.PaymentDTO;
import com.mishchenkov.entity.Payment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlPaymentDAO implements PaymentDAO {

    private static final String SQL_SELECT_ALL = "SELECT payment.id, payment.name FROM payment";

    private static final String SQL_SELECT_DELIVERY_BY_NAME = "SELECT payment.id, payment.name FROM payment WHERE payment.name = ?";

    private final JDBCTemplate template;

    public MySqlPaymentDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<List<PaymentDTO>> selectAll(Connection con) throws SQLException {
        return Optional.ofNullable(template.executeQuery(con, SQL_SELECT_ALL, rowMapper));
    }

    @Override
    public Optional<PaymentDTO> selectByName(Connection con, String paymentName) throws SQLException {
        return template.executeQuery(con, SQL_SELECT_DELIVERY_BY_NAME, rowMapper, paymentName)
                .stream()
                .findFirst();
    }

    private final RowMapper<PaymentDTO> rowMapper = resultSet ->
            new PaymentDTO(
                    resultSet.getInt("id"),
                    new Payment(resultSet.getString("name"))
            );
}
