package com.mishchenkov.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/no-access")
public class NoAccessController extends HttpServlet {

    private static final String OK_PAGE = "/WEB-INF/view/access-denied.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(OK_PAGE).forward(req, resp);
    }
}
