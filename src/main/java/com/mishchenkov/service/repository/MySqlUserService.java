package com.mishchenkov.service.repository;

import com.mishchenkov.dto.UserDTO;
import com.mishchenkov.repository.UserDAO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.storage.Storages;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class MySqlUserService implements UserService {

    private final Logger logger = Logger.getLogger(MySqlUserService.class);

    private final UserDAO userDAO;
    private final DataSource dataSource;

    public MySqlUserService(UserDAO userDAO, DataSource dataSource) {
        this.userDAO = userDAO;
        this.dataSource = dataSource;
    }

    @Override
    public void insertUser(UserDTO userDTO) throws CantProcessTaskServiceException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            userDAO.insertUser(connection, userDTO);
            connection.commit();

        } catch (SQLException e) {
            Storages.quiteRollBack(connection);
            logger.warn(e);
            throw new CantProcessTaskServiceException(e);
        } finally {
            Storages.quiteClose(connection);
        }
    }

    @Override
    public Optional<UserDTO> selectUserByEmail(String email) throws CantProcessTaskServiceException {
        Optional<UserDTO> result;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            result = userDAO.selectUserByEmail(connection, email);

        } catch (SQLException e) {
            logger.warn(e);
            throw new CantProcessTaskServiceException(e);
        } finally {
            Storages.quiteClose(connection);
        }

        return result;
    }
}
