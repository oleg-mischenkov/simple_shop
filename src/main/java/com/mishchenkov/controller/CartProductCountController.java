package com.mishchenkov.controller;

import com.mishchenkov.entity.Cart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static com.mishchenkov.constant.AppConstant.MIME_TEXT;

@WebServlet("/cart-product-count")
public class CartProductCountController extends CommonServicesHttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Cart cart = obtainUserCart(session);

        int productInCart = cart.size();

        resp.setContentType(MIME_TEXT);
        PrintWriter writer = resp.getWriter();
        writer.write(productInCart + "");
        writer.flush();
    }
}
