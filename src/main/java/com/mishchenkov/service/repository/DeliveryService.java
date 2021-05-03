package com.mishchenkov.service.repository;

import com.mishchenkov.dto.DeliveryDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {

    Optional<List<DeliveryDTO>> selectAll() throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<DeliveryDTO> selectByName(String deliveryName) throws TechnicalProblemServiceException, CantProcessTaskServiceException;

}
