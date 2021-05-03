package com.mishchenkov.repository;

import com.mishchenkov.storage.Storages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCTemplate {

    public long update(Connection con, String sql, Object ...values) throws SQLException {
        Long result = null;

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = con.prepareStatement(sql, RETURN_GENERATED_KEYS);

            putValuesToStatement(statement, values);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }

        } finally {
            Storages.quiteClose(resultSet);
            Storages.quiteClose(statement);
        }

        return Optional.ofNullable(result).orElseThrow(SQLException::new);
    }

    public <T> List<T> executeQuery(Connection con, String sql, RowMapper<T> rowMapper) throws SQLException {
        List<T> list = new ArrayList<>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = con.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(rowMapper.row(resultSet));
            }
        } finally {
            Storages.quiteClose(resultSet);
            Storages.quiteClose(statement);
        }
        return list;
    }

    public <T> List<T> executeQuery(Connection con, String sql, RowMapper<T> rowMapper, Object ...values) throws SQLException {
        List<T> list = new ArrayList<>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = con.prepareStatement(sql);
            putValuesToStatement(statement, values);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(rowMapper.row(resultSet));
            }
        } finally {
            Storages.quiteClose(resultSet);
            Storages.quiteClose(statement);
        }
        return list;
    }

    private void putValuesToStatement(PreparedStatement statement, Object ...values) throws SQLException {
        for (int i = 1; i <= values.length ; i++) {
            statement.setObject(i, values[i - 1]);
        }
    }
}
