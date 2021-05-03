package com.mishchenkov.repository;

import com.mishchenkov.dto.DeliveryDTO;
import com.mishchenkov.entity.Delivery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlDeliveryDAO implements DeliveryDAO {

    private static final String SQL_SELECT_ALL = "SELECT delivery.id, delivery.name FROM delivery";

    private static final String SQL_SELECT_DELIVERY_BY_NAME = "SELECT delivery.id, delivery.name FROM delivery WHERE delivery.name = ?";

    private final JDBCTemplate template;

    public MySqlDeliveryDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<List<DeliveryDTO>> selectAll(Connection con) throws SQLException {
        return Optional.ofNullable(template.executeQuery(con, SQL_SELECT_ALL, rowMapper));
    }

    @Override
    public Optional<DeliveryDTO> selectByName(Connection con, String deliveryName) throws SQLException {
        return template.executeQuery(con, SQL_SELECT_DELIVERY_BY_NAME, rowMapper, deliveryName)
                .stream()
                .findFirst();
    }

    private final RowMapper<DeliveryDTO> rowMapper = resultSet ->
            new DeliveryDTO(
                    resultSet.getInt("id"),
                    new Delivery(resultSet.getString("name"))
            );
}
