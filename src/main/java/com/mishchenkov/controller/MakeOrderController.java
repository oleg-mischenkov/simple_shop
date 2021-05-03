package com.mishchenkov.controller;

import com.mishchenkov.dto.OrderFormDTO;
import com.mishchenkov.entity.Cart;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.entity.Delivery;
import com.mishchenkov.entity.Order;
import com.mishchenkov.entity.Payment;
import com.mishchenkov.entity.ProductList;
import com.mishchenkov.entity.Status;
import com.mishchenkov.entity.User;
import com.mishchenkov.service.repository.OrderService;
import com.mishchenkov.service.repository.PayDeliveryService;
import com.mishchenkov.service.repository.exception.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.mishchenkov.constant.AppConstant.REG_CREDIT_CARD;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_ORDER_SERVICE;
import static com.mishchenkov.constant.AppConstant.SESSION_CURRENT_ORDER;
import static com.mishchenkov.constant.AppConstant.SESSION_ITEM_CURRENT_USER;

@WebServlet("/buy")
public class MakeOrderController extends CommonServicesHttpServlet {

    private final Logger logger = Logger.getLogger(this.getClass());

    public static final String REQ_PARAM_DELIVERY = "delivery-type";
    public static final String REQ_PARAM_PAYMENT = "payment-type";
    public static final String REQ_PARAM_CARD = "creditCard";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        ServletContext servletContext = req.getServletContext();
        PayDeliveryService payDeliveryService = obtainPayDeliveryService(servletContext);
        User user = obtainUser(session);
        Cart cart = obtainUserCart(session);
        OrderService orderService = obtainOrderService(servletContext);
        DataContainer<Payment[], Delivery[]> payDeliveryContainer = obtainOayDeliveryContainer(payDeliveryService);
        
        OrderFormDTO orderFormDTO = obtainOrderFormFromRequest(req);
        validateOrderForm(orderFormDTO,payDeliveryContainer);
        Order order = buildOrderEntity(orderFormDTO, user, cart);

        saveOrder(orderService, order);
        session.setAttribute(SESSION_CURRENT_ORDER, order);

        resp.sendRedirect("order-success");
    }

    private void saveOrder(OrderService orderService, Order order) {
        try {
            orderService.insertOrder(order);
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }

    private OrderService obtainOrderService(ServletContext servletContext) {
        return (OrderService) servletContext.getAttribute(SERV_CXT_ORDER_SERVICE);
    }

    private Order buildOrderEntity(OrderFormDTO orderFormDTO, User user, Cart cart) {
        return new Order()
                .setDelivery(new Delivery(orderFormDTO.getDelivery()))
                .setPayment(new Payment(orderFormDTO.getPayment()))
                .setOrderDate(new Date(System.currentTimeMillis()))
                .setStatus(new Status("received"))
                .setStatusDescription("New order")
                .setUser(user)
                .setProductList(new ProductList(cart.getAll()));
    }

    private User obtainUser(HttpSession session) {
        return (User) session.getAttribute(SESSION_ITEM_CURRENT_USER);
    }

    private DataContainer<Payment[], Delivery[]> obtainOayDeliveryContainer(PayDeliveryService payDeliveryService) {
        return new DataContainer<>(
                obtainPaymentList(payDeliveryService).toArray(new Payment[0]),
                obtainDeliveryList(payDeliveryService).toArray(new Delivery[0])
        );
    }

    private void validateOrderForm(OrderFormDTO orderFormDTO, DataContainer<Payment[],Delivery[]> payDeliveryContainer) {
        String delivery = orderFormDTO.getDelivery();
        String payment = orderFormDTO.getPayment();
        String card = orderFormDTO.getCard();
        Payment[] payments = payDeliveryContainer.getName();
        Delivery[] deliveries = payDeliveryContainer.getValue();

        Optional.ofNullable(delivery).orElseThrow(IllegalStateException::new);
        Arrays.stream(deliveries)
                .filter(elem -> elem.getName().equals(delivery))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        Optional.ofNullable(payment).orElseThrow(IllegalStateException::new);
        Arrays.stream(payments)
                .filter(elem -> elem.getName().equals(payment))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        if (payment.equals("card")) {
            Optional.ofNullable(card).orElseThrow(IllegalStateException::new);

            if (!card.matches(REG_CREDIT_CARD)) {
                String msg = "Card value doesn't mach card pattern";
                logger.warn(msg);
                throw new IllegalStateException(msg);
            }
        }
    }

    private OrderFormDTO obtainOrderFormFromRequest(HttpServletRequest req) {
        return new OrderFormDTO()
                .setDelivery(req.getParameter(REQ_PARAM_DELIVERY))
                .setPayment(req.getParameter(REQ_PARAM_PAYMENT))
                .setCard(req.getParameter(REQ_PARAM_CARD));
    }
}