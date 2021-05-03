package com.mishchenkov.storage;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public final class Storages {

    private static final Logger LOGGER = Logger.getLogger(Storages.class);

    private Storages() {}

    public static void quiteClose(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
    }

    public static void quiteRollBack(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOGGER.warn(e);
            }
        }
    }
}
