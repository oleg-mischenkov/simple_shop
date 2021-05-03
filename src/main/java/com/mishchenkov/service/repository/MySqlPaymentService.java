package com.mishchenkov.service.repository;

import com.mishchenkov.dto.PaymentDTO;
import com.mishchenkov.repository.PaymentDAO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public class MySqlPaymentService implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final TransactionTemplate template;

    public MySqlPaymentService(PaymentDAO paymentDAO, TransactionTemplate template) {
        this.paymentDAO = paymentDAO;
        this.template = template;
    }

    @Override
    public Optional<List<PaymentDTO>> selectAll() throws TechnicalProblemServiceException, CantProcessTaskServiceException {
        return template.executeTransaction(paymentDAO, selectAllWorker, new Object());
    }

    @Override
    public Optional<PaymentDTO> selectByName(String paymentName) throws CantProcessTaskServiceException {
        return template.executeTransaction(paymentDAO, selectByNameWorker,paymentName);
    }

    private final TransactionWorker<Optional<List<PaymentDTO>>,PaymentDAO> selectAllWorker =
            (dao, con, values) -> dao.selectAll(con);

    private final TransactionWorker<Optional<PaymentDTO>,PaymentDAO> selectByNameWorker =
            (dao, con, values) -> dao.selectByName(con, (String) values[0]);
}
