package com.mishchenkov.repository;

import com.mishchenkov.dto.DeliveryDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DeliveryDAO {

    Optional<List<DeliveryDTO>> selectAll(Connection con) throws SQLException;

    Optional<DeliveryDTO> selectByName(Connection con, String deliveryName) throws SQLException;

}
