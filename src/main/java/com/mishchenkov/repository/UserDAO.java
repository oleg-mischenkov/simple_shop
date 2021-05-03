package com.mishchenkov.repository;

import com.mishchenkov.dto.UserDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {

    /**
     * Insert user to the data base and return entity's primary key.
     *
     * @param   connection - SQL connection
     * @param   userDTO - user DTO Object
     * @return  PK
     * @throws SQLException exception witch can trow connection
     */
    long insertUser(Connection connection, UserDTO userDTO) throws SQLException;

    /**
     * Select User from data base by email value.
     *
     * @param   connection - SQL connection
     * @param   email - User email
     * @return  Optional witch contains UserDTO entity or this Optional can be empty
     * @throws SQLException - exception witch can trow connection
     */
    Optional<UserDTO> selectUserByEmail(Connection connection, String email) throws SQLException;

    /**
     * Select all users from data base
     *
     * @param   connection - SQL connection
     * @return  Optional witch contains List of Users DTO or this Optional can be empty
     * @throws  SQLException - exception witch can trow connection
     */
    Optional<List<UserDTO>> selectAll(Connection connection) throws SQLException;

}
