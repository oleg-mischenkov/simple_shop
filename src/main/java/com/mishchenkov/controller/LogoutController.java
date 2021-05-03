package com.mishchenkov.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.mishchenkov.constant.AppConstant.SESSION_ITEM_CURRENT_USER;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.setAttribute(SESSION_ITEM_CURRENT_USER, null);
        resp.sendRedirect("index.jsp");
    }
}
