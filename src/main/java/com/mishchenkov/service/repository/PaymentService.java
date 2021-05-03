package com.mishchenkov.service.repository;

import com.mishchenkov.dto.PaymentDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    Optional<List<PaymentDTO>> selectAll() throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<PaymentDTO> selectByName(String paymentName) throws TechnicalProblemServiceException, CantProcessTaskServiceException;
}
