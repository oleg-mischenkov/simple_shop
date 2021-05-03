package com.mishchenkov.repository;

import com.mishchenkov.dto.ManufactureDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ManufactureDAO {

    /**
     * Select all Manufactures from the table
     *
     * @param connection - SQL connection
     * @return  - Optional witch contains List of Manufacture DTO or this Optional can be empty
     * @throws SQLException - exception witch can trow connection
     */
    Optional<List<ManufactureDTO>> selectAll(Connection connection) throws SQLException;

}
