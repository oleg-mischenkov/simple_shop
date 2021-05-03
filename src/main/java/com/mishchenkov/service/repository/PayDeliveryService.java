package com.mishchenkov.service.repository;

import com.mishchenkov.dto.DeliveryDTO;
import com.mishchenkov.dto.PaymentDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public interface PayDeliveryService {

    Optional<List<PaymentDTO>> selectAllPayment() throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<List<DeliveryDTO>> selectAllDelivery() throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<PaymentDTO> selectPaymentByName(String payName) throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<DeliveryDTO> selectDeliveryByName(String deliveryName) throws TechnicalProblemServiceException, CantProcessTaskServiceException;
}
