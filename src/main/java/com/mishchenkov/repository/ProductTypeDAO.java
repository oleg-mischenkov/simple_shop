package com.mishchenkov.repository;

import com.mishchenkov.dto.ProductTypeDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductTypeDAO {
    /**
     * Select all Manufactures from the table
     *
     * @param connection - SQL connection
     * @return  - Optional witch contains List of the Product type DTO or this Optional can be empty
     * @throws java.sql.SQLException - exception witch can trow connection
     */
    Optional<List<ProductTypeDTO>> selectAll(Connection connection) throws SQLException;
}
