package com.mishchenkov.repository;

import com.mishchenkov.dto.CategoryPaginationAndSortStateDTO;
import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.dto.ProductFilterFormDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {

    /**
     * This method is trying to find the most expensive product
     *
     * @param connection - SQL connection
     * @return - the value as Optional of the ProductDTO
     * @throws SQLException - exception witch can trow connection
     */
    Optional<ProductDTO> selectMostExpensiveProduct(Connection connection) throws SQLException;

    /**
     * The method is finding all product by formDTO model.
     *
     * @param connection - SQL connection
     * @param formDTO - data object
     * @return - the value as Optional of the ProductDTO list
     * @throws SQLException - exception witch can trow connection
     */
    Optional<List<ProductDTO>> selectProductsByFilter(Connection connection,
                                                      ProductFilterFormDTO formDTO,
                                                      CategoryPaginationAndSortStateDTO sortPaginationDTO) throws SQLException;

    /**
     * This method calculates all products by formDTO.
     *
     * @param connection - SQL connection
     * @param formDTO - data object
     * @return - the value as Integer of ProductsDTO
     * @throws SQLException - exception witch can trow connection
     */
    Optional<Integer> selectProductCountByFilter(Connection connection, ProductFilterFormDTO formDTO) throws SQLException;

    /**
     * This method return product by id
     *
     * @param connection - SQL connection
     * @param productId - id in data base which has had some product
     * @return - Optional which contains a ProductDTO or null
     * @throws - SQLException
     */
    Optional<ProductDTO> selectProductById(Connection connection, int productId) throws SQLException;

    /**
     * This method return product by sku
     *
     * @param connection - SQL connection
     * @param sku - sku in data base which has had some product
     * @return - Optional which contains a ProductDTO or null
     * @throws - SQLException
     */
    Optional<ProductDTO> selectProductBySku(Connection connection, String sku) throws SQLException;
}
