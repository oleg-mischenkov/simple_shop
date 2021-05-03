package com.mishchenkov.service.repository;

import com.mishchenkov.entity.Order;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

public interface OrderService {

    void insertOrder(Order order) throws TechnicalProblemServiceException, CantProcessTaskServiceException;

}
