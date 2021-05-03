package com.mishchenkov.service.repository;

import com.mishchenkov.dto.CategoryPaginationAndSortStateDTO;
import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.dto.ProductFilterFormDTO;
import com.mishchenkov.repository.ProductDAO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;

import java.util.List;
import java.util.Optional;

public class MySqlProductService implements ProductService {

    private final ProductDAO productDAO;
    private final TransactionTemplate template;

    public MySqlProductService(ProductDAO productDAO, TransactionTemplate template) {
        this.productDAO = productDAO;
        this.template = template;
    }

    @Override
    public Optional<ProductDTO> selectMostExpensiveProduct() throws CantProcessTaskServiceException {
        return template.executeTransaction(productDAO, singleProductWorker, new Object());
    }

    @Override
    public Optional<List<ProductDTO>> selectProductsByFilter(ProductFilterFormDTO formDTO,
                                                             CategoryPaginationAndSortStateDTO sortPaginationDTO)
            throws CantProcessTaskServiceException {
        return template.executeTransaction(productDAO, productListWorker, formDTO, sortPaginationDTO);
    }

    @Override
    public Optional<Integer> selectProductCountByFilter(ProductFilterFormDTO formDTO) throws CantProcessTaskServiceException {
        return template.executeTransaction(productDAO, productCountWorker, formDTO);
    }

    @Override
    public Optional<ProductDTO> selectProductById(int productId) throws CantProcessTaskServiceException {
        return template.executeTransaction(productDAO, productByIdWorker, productId);
    }

    @Override
    public Optional<ProductDTO> selectProductBySku(String sku) throws CantProcessTaskServiceException {
        return template.executeTransaction(productDAO, productBySkuWorker, sku);
    }

    private final TransactionWorker<Optional<ProductDTO>,ProductDAO> productBySkuWorker =
            (dao, con, values) -> dao.selectProductBySku(con, (String) values[0]);

    private final TransactionWorker<Optional<ProductDTO>,ProductDAO> productByIdWorker =
        (dao, con, values) -> dao.selectProductById(con, (Integer) values[0]);

    private final TransactionWorker<Optional<ProductDTO>,ProductDAO> singleProductWorker =
            (dao, con, values) -> dao.selectMostExpensiveProduct(con);

    private final TransactionWorker<Optional<List<ProductDTO>>,ProductDAO> productListWorker =
            (dao, con, values) -> dao.selectProductsByFilter(
                    con, (ProductFilterFormDTO) values[0], (CategoryPaginationAndSortStateDTO) values[1]);

    private final TransactionWorker<Optional<Integer>,ProductDAO> productCountWorker =
            (dao, con, values) -> dao.selectProductCountByFilter(con, (ProductFilterFormDTO) values[0]);
}