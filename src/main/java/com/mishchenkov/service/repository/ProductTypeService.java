package com.mishchenkov.service.repository;

import com.mishchenkov.dto.ProductTypeDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public interface ProductTypeService {

    Optional<List<ProductTypeDTO>> selectAll() throws TechnicalProblemServiceException, CantProcessTaskServiceException;

}
