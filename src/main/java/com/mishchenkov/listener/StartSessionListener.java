package com.mishchenkov.listener;

import com.mishchenkov.entity.Cart;
import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class StartSessionListener implements HttpSessionListener {

    private final Logger logger = Logger.getLogger(StartSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("Create new session: " + se.getSession().getId());
        se.getSession().setAttribute(AppConstant.SESSION_ITEM_CURRENT_USER, null);
        initDefaultProductCount(se.getSession());
        initUserCart(se.getSession());
    }

    private void initUserCart(HttpSession session) {
        session.setAttribute(AppConstant.SESSION_ATTR_USER_CART, new Cart());
    }

    private void initDefaultProductCount(HttpSession session) {
        session.setAttribute(AppConstant.SESSION_ATTR_CURRENT_PAGINATION_COUNT, AppConstant.SERV_CXT_DEFAULT_PRODUCT_ON_PAGE);
        session.setAttribute(AppConstant.SESSION_ATTR_CURRENT_SORT_VALUE, AppConstant.SERV_CXT_DEFAULT_SORT_VALUE);
    }
}
