package com.mishchenkov.controller;

import com.mishchenkov.dto.DeliveryDTO;
import com.mishchenkov.dto.PaymentDTO;
import com.mishchenkov.entity.Cart;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.entity.Delivery;
import com.mishchenkov.entity.Payment;
import com.mishchenkov.service.CaptchaService;
import com.mishchenkov.service.repository.PayDeliveryService;
import com.mishchenkov.service.repository.ProductService;
import com.mishchenkov.service.repository.UserService;
import com.mishchenkov.service.repository.exception.ServiceException;
import com.mishchenkov.validator.NullValidator;
import com.mishchenkov.validator.RegexpValidator;
import com.mishchenkov.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mishchenkov.constant.AppConstant.SERV_CXT_CAPTCHA_SERVICE;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_PAY_DELIVERY_SERVICE;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_PRODUCT_SERVICE;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_USER_SERVICE;
import static com.mishchenkov.constant.AppConstant.SESSION_ATTR_USER_CART;

public abstract class CommonServicesHttpServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(this.getClass());

    protected CaptchaService getCaptchaService(HttpServletRequest req) {
        return (CaptchaService) req.getServletContext().getAttribute(SERV_CXT_CAPTCHA_SERVICE);
    }

    protected UserService getUserService(HttpServletRequest req) {
        return (UserService) req.getServletContext().getAttribute(SERV_CXT_USER_SERVICE);
    }

    protected void validateItem(String fieldName, String fieldValue, String failMsg, Map<String, String> errorMap, String regexp) {
        DataContainer<String, String> itemDataPair = new DataContainer<>(fieldName, fieldValue);

        Validator[] validators = {
                new NullValidator(itemDataPair.changeData(), failMsg),
                new NullValidator(itemDataPair, failMsg),
                new RegexpValidator(itemDataPair, failMsg, regexp)
        };

        validateItem(validators, errorMap);
    }

    protected void validateItem(Validator[] validators, Map<String, String> errorMap) {
        Validator head = validators[0];

        if (validators.length > 1) {
            for (int i = 1; i < validators.length; i++) {
                head.setNext(validators[i]);
            }
        }

        head.validate(errorMap);
    }

    protected void transportDataAndRemove(String attrName, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Object sessionValue = session.getAttribute(attrName);

        if (sessionValue != null) {
            req.setAttribute(attrName, sessionValue);
            session.removeAttribute(attrName);
        }
    }

    protected ProductService getProductService(ServletContext servletContext) {
        return (ProductService) servletContext.getAttribute(SERV_CXT_PRODUCT_SERVICE);
    }

    protected Cart obtainUserCart(HttpSession session) {
        return (Cart) session.getAttribute(SESSION_ATTR_USER_CART);
    }

    protected PayDeliveryService obtainPayDeliveryService(ServletContext context) {
        return (PayDeliveryService) context.getAttribute(SERV_CXT_PAY_DELIVERY_SERVICE);
    }

    protected List<Payment> obtainPaymentList(PayDeliveryService payDeliveryService) {
        try {
            return payDeliveryService.selectAllPayment().orElse(new ArrayList<>())
                    .stream()
                    .map(PaymentDTO::getPayment)
                    .collect(Collectors.toList());
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }

    protected List<Delivery> obtainDeliveryList(PayDeliveryService payDeliveryService) {
        try {
            return payDeliveryService.selectAllDelivery().orElse(new ArrayList<>())
                    .stream()
                    .map(DeliveryDTO::getDelivery)
                    .collect(Collectors.toList());
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }
}
