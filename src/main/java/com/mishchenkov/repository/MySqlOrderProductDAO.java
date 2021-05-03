package com.mishchenkov.repository;

import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.entity.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySqlOrderProductDAO implements OrderProductsDAO {

    private static final String SQL_INSERT_PRODUCTS = "INSERT INTO order_products (count, price, orders_id, products_id) VALUES (?,?,?,?)";

    private final JDBCTemplate template;

    public MySqlOrderProductDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public void insertOrderProducts(Connection con, List<DataContainer<ProductDTO, Integer>> products, long orderId) throws SQLException {
        for (DataContainer<ProductDTO, Integer> element: products) {
            template.update(con, SQL_INSERT_PRODUCTS,
                    element.getValue(),
                    element.getName().getProduct().getPrice().getMoneyInCents(),
                    orderId,
                    element.getName().getKey()
            );
        }
    }
}
