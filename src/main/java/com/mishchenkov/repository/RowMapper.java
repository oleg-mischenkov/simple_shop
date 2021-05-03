package com.mishchenkov.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {

    T row(ResultSet resultSet) throws SQLException;

}
