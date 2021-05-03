package com.mishchenkov.repository;

import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.entity.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderProductsDAO {

    void insertOrderProducts(Connection con, List<DataContainer<ProductDTO,Integer>> products, long orderId) throws SQLException;

}
