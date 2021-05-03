package com.mishchenkov.service.repository;

import com.mishchenkov.dto.CategoryPaginationAndSortStateDTO;
import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.dto.ProductFilterFormDTO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.service.repository.exception.TechnicalProblemServiceException;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDTO> selectMostExpensiveProduct()
            throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<List<ProductDTO>> selectProductsByFilter(ProductFilterFormDTO formDTO,
                                                      CategoryPaginationAndSortStateDTO sortPaginationDTO)
            throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<Integer> selectProductCountByFilter(ProductFilterFormDTO formDTO)
            throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<ProductDTO> selectProductById(int productId)
            throws TechnicalProblemServiceException, CantProcessTaskServiceException;

    Optional<ProductDTO> selectProductBySku(String sku)
            throws TechnicalProblemServiceException, CantProcessTaskServiceException;
}
