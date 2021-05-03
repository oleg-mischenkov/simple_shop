package com.mishchenkov.service.repository;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface TransactionWorker<T,DAO> {

    T doWork(DAO dao, Connection con, Object ...values) throws SQLException;

}
