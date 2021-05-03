package com.mishchenkov.service.repository;

import com.mishchenkov.dto.UserDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.Optional;

public interface UserService {

    void insertUser(UserDTO userDTO) throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<UserDTO> selectUserByEmail(String email) throws TechnicalProblemServiceException, CantProcessTaskServiceException;
}
