package com.mishchenkov.service.repository;

import com.mishchenkov.dto.DeliveryDTO;
import com.mishchenkov.repository.DeliveryDAO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public class MySqlDeliveryService implements DeliveryService {

    private final DeliveryDAO deliveryDAO;
    private final TransactionTemplate template;

    public MySqlDeliveryService(DeliveryDAO deliveryDAO, TransactionTemplate template) {
        this.deliveryDAO = deliveryDAO;
        this.template = template;
    }

    @Override
    public Optional<List<DeliveryDTO>> selectAll() throws TechnicalProblemServiceException, CantProcessTaskServiceException {
        return template.executeTransaction(deliveryDAO, selectAllWorker, new Object());
    }

    @Override
    public Optional<DeliveryDTO> selectByName(String deliveryName) throws CantProcessTaskServiceException {
        return template.executeTransaction(deliveryDAO, selectByNameWorker, deliveryName);
    }

    private final TransactionWorker<Optional<List<DeliveryDTO>>,DeliveryDAO> selectAllWorker =
            (dao, con, values) -> dao.selectAll(con);

    private final TransactionWorker<Optional<DeliveryDTO>,DeliveryDAO> selectByNameWorker =
            (dao, con, values) -> dao.selectByName(con, (String) values[0]);
}
