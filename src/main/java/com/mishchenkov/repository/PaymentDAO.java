package com.mishchenkov.repository;

import com.mishchenkov.dto.PaymentDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PaymentDAO {

    Optional<List<PaymentDTO>> selectAll(Connection con) throws SQLException;

    Optional<PaymentDTO> selectByName(Connection con, String paymentName) throws SQLException;

}
