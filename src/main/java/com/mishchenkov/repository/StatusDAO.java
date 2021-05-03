package com.mishchenkov.repository;

import com.mishchenkov.dto.StatusDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StatusDAO {

    Optional<List<StatusDTO>> selectAll(Connection connection) throws SQLException;

    Optional<StatusDTO> selectStatusByName(Connection connection, String statusName) throws SQLException;

}
