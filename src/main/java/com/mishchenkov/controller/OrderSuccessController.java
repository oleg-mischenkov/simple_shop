package com.mishchenkov.controller;

import com.mishchenkov.entity.Cart;
import com.mishchenkov.entity.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.mishchenkov.constant.AppConstant.SESSION_ATTR_USER_CART;
import static com.mishchenkov.constant.AppConstant.SESSION_CURRENT_ORDER;

@WebServlet("/order-success")
public class OrderSuccessController extends CommonServicesHttpServlet {

    public static final String REQ_ATTR_ORDER = "currentOrder";
    public static final String REQ_ATTR_ORDER_TOTAL_PRICE = "totalPrice";

    private static final String OK_PAGE = "/WEB-INF/view/order_success.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Order order = obtainCurrentOrder(session);
        Cart cart = obtainUserCart(session);
        String price = String.format("%.2f", cart.getTotalPriceAsFloat());

        session.setAttribute(SESSION_ATTR_USER_CART, new Cart());
        session.removeAttribute(SESSION_CURRENT_ORDER);

        req.setAttribute(REQ_ATTR_ORDER, order);
        req.setAttribute(REQ_ATTR_ORDER_TOTAL_PRICE, price);
        req.getRequestDispatcher(OK_PAGE).forward(req, resp);
    }

    private Order obtainCurrentOrder(HttpSession session) {
        return (Order) session.getAttribute(SESSION_CURRENT_ORDER);
    }
}
