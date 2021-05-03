package com.mishchenkov.service.repository;

import com.mishchenkov.dto.DeliveryDTO;
import com.mishchenkov.dto.PaymentDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public class MySqlPayDeliveryFacadeService implements PayDeliveryService {

    private final DeliveryService deliveryService;
    private final PaymentService paymentService;

    public MySqlPayDeliveryFacadeService(DeliveryService deliveryService, PaymentService paymentService) {
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
    }

    @Override
    public Optional<List<PaymentDTO>> selectAllPayment() throws TechnicalProblemServiceException, CantProcessTaskServiceException {
        return paymentService.selectAll();
    }

    @Override
    public Optional<List<DeliveryDTO>> selectAllDelivery() throws TechnicalProblemServiceException, CantProcessTaskServiceException {
        return deliveryService.selectAll();
    }

    @Override
    public Optional<PaymentDTO> selectPaymentByName(String payName) throws TechnicalProblemServiceException, CantProcessTaskServiceException {
        return paymentService.selectByName(payName);
    }

    @Override
    public Optional<DeliveryDTO> selectDeliveryByName(String deliveryName) throws TechnicalProblemServiceException, CantProcessTaskServiceException {
        return deliveryService.selectByName(deliveryName);
    }
}
