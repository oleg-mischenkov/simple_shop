package com.mishchenkov.repository;

import com.mishchenkov.dto.ManufactureDTO;
import com.mishchenkov.entity.Manufacture;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlManufactureDAO implements ManufactureDAO {

    private static final String SQL_SELECT_ALL_MANUFACTURES = "SELECT id, `name` FROM manufactures";

    private final JDBCTemplate template;

    public MySqlManufactureDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<List<ManufactureDTO>> selectAll(Connection connection) throws SQLException {
        return Optional.ofNullable(
                template.executeQuery(connection, SQL_SELECT_ALL_MANUFACTURES, dtoRowMapper)
        );
    }

    private RowMapper<ManufactureDTO> dtoRowMapper = resultSet ->
            new ManufactureDTO()
                    .setKey(resultSet.getLong("id"))
                    .setManufacture(
                            new Manufacture(resultSet.getString("name"))
                    );

}
