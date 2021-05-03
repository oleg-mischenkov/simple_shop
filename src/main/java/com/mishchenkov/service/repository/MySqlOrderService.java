package com.mishchenkov.service.repository;

import com.mishchenkov.dto.OrderStatusDTO;
import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.entity.Order;
import com.mishchenkov.entity.Product;
import com.mishchenkov.repository.DeliveryDAO;
import com.mishchenkov.repository.OrderDAO;
import com.mishchenkov.repository.OrderProductsDAO;
import com.mishchenkov.repository.OrderStatusDAO;
import com.mishchenkov.repository.PaymentDAO;
import com.mishchenkov.repository.ProductDAO;
import com.mishchenkov.repository.StatusDAO;
import com.mishchenkov.repository.UserDAO;
import com.mishchenkov.service.repository.exception.CantProcessTaskServiceException;
import com.mishchenkov.storage.Storages;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MySqlOrderService implements OrderService {

    private final Logger logger = Logger.getLogger(this.getClass());

    private DataSource dataSource;
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private DeliveryDAO deliveryDAO;
    private PaymentDAO paymentDAO;
    private StatusDAO statusDAO;
    private OrderStatusDAO orderStatusDAO;
    private ProductDAO productDAO;
    private OrderProductsDAO orderProductsDAO;

    private MySqlOrderService() {}

    @Override
    public void insertOrder(Order order) throws CantProcessTaskServiceException {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();

            long userId = userDAO.selectUserByEmail(connection, order.getUser().getEmail())
                    .orElseThrow(CantProcessTaskServiceException::new).getKey();

            long deliveryId = deliveryDAO.selectByName(connection, order.getDelivery().getName())
                    .orElseThrow(CantProcessTaskServiceException::new).getId();

            long paymentId = paymentDAO.selectByName(connection, order.getPayment().getName())
                    .orElseThrow(CantProcessTaskServiceException::new).getId();

            long orderId = orderDAO.insertOrder(connection, userId, deliveryId, paymentId);

            long statusId = statusDAO.selectStatusByName(connection, order.getStatus().getName())
                    .orElseThrow(CantProcessTaskServiceException::new).getId();

            orderStatusDAO.insertOrderStatus(connection,
                    new OrderStatusDTO()
                            .setOrderId(orderId)
                            .setStatusId(statusId)
            );

            List<DataContainer<ProductDTO,Integer>> productDtoList =
                    obtainProductDTOList(order.getProductList().getAll(), productDAO, connection);

            orderProductsDAO.insertOrderProducts(connection, productDtoList, orderId);

            connection.commit();
        } catch (SQLException e) {
            Storages.quiteRollBack(connection);
            logger.warn(e);
            throw new CantProcessTaskServiceException(e);
        } finally {
            Storages.quiteClose(connection);
        }
    }

    private List<DataContainer<ProductDTO, Integer>> obtainProductDTOList(List<DataContainer<Product, Integer>> all,
                                                                          ProductDAO productDAO,
                                                                          Connection con) {
        return all.stream()
                .map(elem -> productTransformer(productDAO, con, elem))
                .collect(Collectors.toList());
    }

    private DataContainer<ProductDTO, Integer> productTransformer(ProductDAO productDAO, Connection con, DataContainer<Product, Integer> elem) {
        try {
            return new DataContainer<>(
                    productDAO.selectProductBySku(con, elem.getName().getSku())
                            .orElseThrow(IllegalStateException::new),
                    elem.getValue()
            );
        } catch (SQLException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }

    public static Builder getBuilder() {
        return new MySqlOrderService().new Builder();
    }

    public class Builder {

        public Builder setDataSource(DataSource dataSource) {
            MySqlOrderService.this.dataSource = dataSource;
            return this;
        }

        public Builder setUserDAO(UserDAO userDAO) {
            MySqlOrderService.this.userDAO = userDAO;
            return this;
        }

        public Builder setOrderDAO(OrderDAO orderDAO) {
            MySqlOrderService.this.orderDAO = orderDAO;
            return this;
        }

        public Builder setDeliveryDAO(DeliveryDAO deliveryDAO) {
            MySqlOrderService.this.deliveryDAO = deliveryDAO;
            return this;
        }

        public Builder setPaymentDAO(PaymentDAO paymentDAO) {
            MySqlOrderService.this.paymentDAO = paymentDAO;
            return this;
        }

        public Builder setStatusDAO(StatusDAO statusDAO) {
            MySqlOrderService.this.statusDAO = statusDAO;
            return this;
        }

        public Builder setOrderStatusDAO(OrderStatusDAO orderStatusDAO) {
            MySqlOrderService.this.orderStatusDAO = orderStatusDAO;
            return this;
        }

        public Builder setProductDAO(ProductDAO productDAO) {
            MySqlOrderService.this.productDAO = productDAO;
            return this;
        }

        public Builder setOrderProductDAO(OrderProductsDAO orderProductsDAO) {
            MySqlOrderService.this.orderProductsDAO = orderProductsDAO;
            return this;
        }

        public MySqlOrderService build() {
            boolean isNull = Stream.of(
                    MySqlOrderService.this.dataSource,
                    MySqlOrderService.this.userDAO,
                    MySqlOrderService.this.orderDAO,
                    MySqlOrderService.this.deliveryDAO,
                    MySqlOrderService.this.paymentDAO,
                    MySqlOrderService.this.statusDAO,
                    MySqlOrderService.this.orderStatusDAO,
                    MySqlOrderService.this.productDAO,
                    MySqlOrderService.this.orderProductsDAO
            ).anyMatch(Objects::isNull);

            if (!isNull) {
                return MySqlOrderService.this;
            } else {
                throw new IllegalStateException();
            }
        }
    }

}
