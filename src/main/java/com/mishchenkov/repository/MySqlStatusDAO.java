package com.mishchenkov.repository;

import com.mishchenkov.dto.StatusDTO;
import com.mishchenkov.entity.Status;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlStatusDAO implements StatusDAO {

    private static final String SQL_SELECT_ALL = "SELECT statuses.id, statuses.name FROM statuses";

    private static final String SQL_SELECT_STATUS_BY_NAME = "SELECT statuses.id, statuses.name FROM statuses WHERE statuses.name = ?";

    private final JDBCTemplate template;

    public MySqlStatusDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<List<StatusDTO>> selectAll(Connection connection) throws SQLException {
        return Optional.ofNullable(template.executeQuery(connection,SQL_SELECT_ALL, rowMapper));
    }

    @Override
    public Optional<StatusDTO> selectStatusByName(Connection connection, String statusName) throws SQLException {
        return template.executeQuery(connection, SQL_SELECT_STATUS_BY_NAME, rowMapper, statusName)
                .stream()
                .findFirst();
    }

    private final RowMapper<StatusDTO> rowMapper = resultSet ->
            new StatusDTO(
                    resultSet.getInt("id"),
                    new Status(resultSet.getString("name"))
            );

}
