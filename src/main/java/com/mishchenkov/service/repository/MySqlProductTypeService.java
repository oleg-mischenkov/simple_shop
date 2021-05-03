package com.mishchenkov.service.repository;

import com.mishchenkov.dto.ProductTypeDTO;
import com.mishchenkov.repository.ProductTypeDAO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public class MySqlProductTypeService implements ProductTypeService {

    private final ProductTypeDAO productTypeDAO;
    private final TransactionTemplate template;

    public MySqlProductTypeService(ProductTypeDAO productTypeDAO, TransactionTemplate template) {
        this.productTypeDAO = productTypeDAO;
        this.template = template;
    }


    @Override
    public Optional<List<ProductTypeDTO>> selectAll() throws TechnicalProblemServiceException, CantProcessTaskServiceException {
        return template.executeTransaction(productTypeDAO, worker, new Object());
    }

    private final TransactionWorker<Optional<List<ProductTypeDTO>>,ProductTypeDAO> worker =
            (dao, con, values) -> dao.selectAll(con);
}
