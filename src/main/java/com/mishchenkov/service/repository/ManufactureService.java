package com.mishchenkov.service.repository;

import com.mishchenkov.dto.ManufactureDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public interface ManufactureService {

    Optional<List<ManufactureDTO>> selectAll() throws TechnicalProblemServiceException, CantProcessTaskServiceException;

}
