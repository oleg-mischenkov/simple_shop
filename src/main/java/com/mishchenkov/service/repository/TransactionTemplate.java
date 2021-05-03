package com.mishchenkov.service.repository;

import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.storage.Storages;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionTemplate {

    private final Logger logger = Logger.getLogger(TransactionTemplate.class);

    private final DataSource dataSource;

    public TransactionTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T,DAO> T executeTransaction(DAO dao, TransactionWorker<T,DAO> worker, Object ...values)
            throws CantProcessTaskServiceException {
        T result;
        Connection connection = null;

        try {
            connection = dataSource.getConnection();

            result = worker.doWork(dao, connection, values);
            connection.commit();

        } catch (SQLException e) {
            Storages.quiteRollBack(connection);
            logger.warn(e);
            throw new CantProcessTaskServiceException(e);
        } finally {
            Storages.quiteClose(connection);
        }

        return result;
    }

}
