package com.mishchenkov.repository;

import com.mishchenkov.dto.ProductTypeDTO;
import com.mishchenkov.entity.ProductType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlProductTypeDAO implements ProductTypeDAO {

    private static final String SQL_SELECT_ALL_PRODUCT_TYPES = "SELECT id, `name` FROM product_types";

    private final JDBCTemplate template;

    public MySqlProductTypeDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<List<ProductTypeDTO>> selectAll(Connection connection) throws SQLException {
        return Optional.ofNullable(
                template.executeQuery(connection, SQL_SELECT_ALL_PRODUCT_TYPES, rowMapper)
        );
    }

    private final RowMapper<ProductTypeDTO> rowMapper = resultSet ->
            new ProductTypeDTO()
                    .setKey(resultSet.getLong("id"))
                    .setProductType(
                            new ProductType(resultSet.getString("name"))
                    );
}
