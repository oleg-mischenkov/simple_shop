package com.mishchenkov.service.repository;

import com.mishchenkov.dto.ManufactureDTO;
import com.mishchenkov.repository.ManufactureDAO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public class MySqlManufactureService implements ManufactureService {

    private final ManufactureDAO manufactureDAO;
    private final TransactionTemplate template;

    public MySqlManufactureService(ManufactureDAO manufactureDAO, TransactionTemplate template) {
        this.manufactureDAO = manufactureDAO;
        this.template = template;
    }

    @Override
    public Optional<List<ManufactureDTO>> selectAll() throws CantProcessTaskServiceException, TechnicalProblemServiceException {
        return template.executeTransaction(manufactureDAO, worker, new Object());
    }

    private final TransactionWorker<Optional<List<ManufactureDTO>>, ManufactureDAO> worker = (dao, con, objects) ->  dao.selectAll(con);

}
