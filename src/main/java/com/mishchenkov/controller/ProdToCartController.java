package com.mishchenkov.controller;

import com.mishchenkov.entity.Cart;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.entity.Product;
import com.mishchenkov.service.repository.ProductService;
import com.mishchenkov.service.repository.exception.ServiceException;
import com.mishchenkov.validator.NullValidator;
import com.mishchenkov.validator.RegexpValidator;
import com.mishchenkov.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import static com.mishchenkov.constant.AppConstant.FAIL_MSG_BAD_PRODUCT_ID;
import static com.mishchenkov.constant.AppConstant.MIME_TEXT;
import static com.mishchenkov.constant.AppConstant.REG_POSITIVE_NUMBER;

@WebServlet("/prod-to-cart")
public class ProdToCartController extends CommonServicesHttpServlet  {

    private final Logger logger = Logger.getLogger(ProdToCartController.class);

    public static final String REQ_PARAM_PRODUCT_ID = "prod-id";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext servletContext = req.getServletContext();
        ProductService productService = getProductService(servletContext);
        HttpSession session = req.getSession();
        Cart cart = obtainUserCart(session);

        String preProductId = req.getParameter(REQ_PARAM_PRODUCT_ID);
        validateProductId(preProductId);
        int productId = Integer.parseInt(preProductId);

        Product product = obtainProduct(productService, productId);
        int productInCart = putProductToCart(product, cart);

        logger.trace(cart);

        resp.setContentType(MIME_TEXT);
        PrintWriter writer = resp.getWriter();
        writer.write(productInCart + "");
        writer.flush();
    }

    private int putProductToCart(Product product, Cart cart) {
        cart.add(product);
        return cart.size();
    }

    private Product obtainProduct(ProductService productService, int productId) {
        try {
            return productService.selectProductById(productId).orElseThrow(IllegalStateException::new).getProduct();
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }

    private void validateProductId(String preProductId) {
        Map<String,String> errorMap = new TreeMap<>();
        DataContainer<String,String> container = new DataContainer<>("prod id", preProductId);
        Validator[] validators = {
                new NullValidator(container, FAIL_MSG_BAD_PRODUCT_ID),
                new RegexpValidator(container, FAIL_MSG_BAD_PRODUCT_ID, REG_POSITIVE_NUMBER)
        };
        validateItem(validators, errorMap);
        if (errorMap.size() > 0) {
            logger.warn(FAIL_MSG_BAD_PRODUCT_ID);
            throw new IllegalStateException(FAIL_MSG_BAD_PRODUCT_ID);
        }
    }
}
