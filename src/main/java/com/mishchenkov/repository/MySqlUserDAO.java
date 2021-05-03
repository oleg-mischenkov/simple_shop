package com.mishchenkov.repository;

import com.mishchenkov.dto.UserDTO;
import com.mishchenkov.entity.Role;
import com.mishchenkov.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlUserDAO implements UserDAO {

    private static final String SQL_INSERT_USER = "INSERT INTO users (`name`, `s_name`, `mail`, `psw`, `send_mail`, `roles_id`) "
            .concat("VALUES (?, ?, ?, md5(?), ?, (SELECT roles.id FROM roles WHERE roles.role = ?))");
    private static final String SQL_SELECT_USER_BY_EMAIL = "SELECT users.id, `name`, s_name, mail, psw, send_mail, roles.`role` "
            .concat("FROM users LEFT JOIN roles ON roles.id = users.roles_id WHERE `mail` = ?");
    private static final String SQL_SELECT_ALL_USER = "SELECT users.id, `name`, s_name, mail, psw, send_mail, roles.`role` "
            .concat("FROM users LEFT JOIN roles ON roles.id = users.roles_id ORDER BY NULL");

    private final JDBCTemplate template;

    public MySqlUserDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public long insertUser(Connection connection, UserDTO userDTO) throws SQLException {
        return template.update(connection, SQL_INSERT_USER,
                userDTO.getUser().getName(),
                userDTO.getUser().getSecondName(),
                userDTO.getUser().getEmail(),
                userDTO.getUser().getPassword(),
                userDTO.isSendEmail(),
                obtainRoleName(userDTO.getUser())
        );
    }

    @Override
    public Optional<UserDTO> selectUserByEmail(Connection connection, String email) throws SQLException {
        return template.executeQuery(connection, SQL_SELECT_USER_BY_EMAIL, dtoRowMapper, email)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<List<UserDTO>> selectAll(Connection connection) throws SQLException {
        return Optional.ofNullable(
                template.executeQuery(connection, SQL_SELECT_ALL_USER, dtoRowMapper)
        );
    }

    private String obtainRoleName(User user) {
        return user.getRole() == null ? "user" : user.getRole().getRoleName();
    }

    private final RowMapper<UserDTO> dtoRowMapper = resultSet ->
            new UserDTO()
                    .setKey(resultSet.getLong("id"))
                    .setSendEmail(resultSet.getBoolean("send_mail"))
                    .setUser(
                            User.getBuilder()
                                    .setName(resultSet.getString("name"))
                                    .setSecondName(resultSet.getString("s_name"))
                                    .setEmail(resultSet.getString("mail"))
                                    .setPassword(resultSet.getString("psw"))
                                    .setRole(
                                            new Role(resultSet.getString("role"))
                                    )
                                    .build()
                    );

}
