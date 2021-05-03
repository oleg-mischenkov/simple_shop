package com.mishchenkov.controller;

import com.mishchenkov.dto.CartItemCountDTO;
import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.entity.Cart;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.entity.Product;
import com.mishchenkov.service.repository.PayDeliveryService;
import com.mishchenkov.service.repository.ProductService;
import com.mishchenkov.service.repository.exception.ServiceException;
import com.mishchenkov.validator.NullValidator;
import com.mishchenkov.validator.RegexpValidator;
import com.mishchenkov.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static com.mishchenkov.constant.AppConstant.REG_POSITIVE_NUMBER;

@WebServlet("/cart")
public class CartController extends CommonServicesHttpServlet {

    private final Logger logger = Logger.getLogger(this.getClass());

    public static final String REQ_PARAM_COUNT_NAME = "countName";
    public static final String REQ_PARAM_COUNT_VALUE = "countValue";
    public static final String REQ_PARAM_REMOVE = "removeElem";
    public static final String REQ_PARAM_REMOVE_ALL = "removeAll";

    public static final String REQ_ATTR_CART_ITEMS = "cart";
    public static final String REQ_ATTR_CART_TOTAL_PRICE = "totalPrice";
    public static final String REQ_ATTR_PAYMENT_LIST = "paymentList";
    public static final String REQ_ATTR_DELIVERY_LIST = "deliveryList";

    public static final String OK_PAGE = "/WEB-INF/view/cart.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext servletContext = req.getServletContext();
        ProductService productService = getProductService(servletContext);
        HttpSession session = req.getSession();
        Cart cart = obtainUserCart(session);

        Map<String, String> errorMap = new TreeMap<>();
        CartItemCountDTO itemCountDTO = obtainCartItem(req);
        validateCountDTO(errorMap, itemCountDTO);

        Product product;
        if (errorMap.isEmpty()) {
            product = obtainProduct(itemCountDTO.getProductSku(), productService);
            changeProductCount(product, parseInt(itemCountDTO),cart);

            resp.sendRedirect("cart");

        } else errorMap.clear();


        String removeSku = req.getParameter(REQ_PARAM_REMOVE);
        validateRemoveSku(errorMap, removeSku);
        if (errorMap.isEmpty()) {
            product = obtainProduct(removeSku, productService);
            cart.remove(product);

            resp.sendRedirect("cart");

        } else errorMap.clear();

        String removeAll = req.getParameter(REQ_PARAM_REMOVE_ALL);
        validateRemoveAll(errorMap, removeAll);
        if (errorMap.isEmpty()) {
            cart.clear();
            logger.trace("Directive: removeAll");
            resp.sendRedirect("cart");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart cart = obtainUserCart(session);
        ServletContext servletContext = req.getServletContext();
        PayDeliveryService payDeliveryService = obtainPayDeliveryService(servletContext);

        req.setAttribute(REQ_ATTR_PAYMENT_LIST, obtainPaymentList(payDeliveryService));
        req.setAttribute(REQ_ATTR_DELIVERY_LIST, obtainDeliveryList(payDeliveryService));
        req.setAttribute(REQ_ATTR_CART_TOTAL_PRICE, cart.getTotalPriceAsFloat());
        req.setAttribute(REQ_ATTR_CART_ITEMS, cart.getAll());
        req.getRequestDispatcher(OK_PAGE).forward(req, resp);
    }

    private int parseInt(CartItemCountDTO itemCountDTO) {
        return Integer.parseInt(itemCountDTO.getValue());
    }

    private void validateRemoveAll(Map<String, String> errorMap, String removeAll) {
        if (removeAll == null || !removeAll.contains("remove")) {
            errorMap.put(REQ_PARAM_REMOVE_ALL, removeAll);
        }
    }

    private void validateRemoveSku(Map<String,String> errorMap, String removeSku) {
        if (removeSku == null || removeSku.length() == 0) {
            errorMap.put(REQ_PARAM_REMOVE, removeSku);
        }
    }

    private void changeProductCount(Product product, int parseInt, Cart cart) {
        if (cart.showValue(product) > parseInt) {
            cart.removeOne(product);
        } else {
            cart.add(product);
        }
    }

    private Product obtainProduct(String productSku, ProductService productService) {
        try {
            return productService.selectProductBySku(productSku).orElse(new ProductDTO()).getProduct();
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }

    private void validateCountDTO(Map<String, String> errorMap, CartItemCountDTO itemCountDTO) {
        String sku = itemCountDTO.getProductSku();
        String value = itemCountDTO.getValue();
        DataContainer<String,String> dataContainer = new DataContainer<>("sku", sku);
        Validator[] validators = {new NullValidator(dataContainer, sku)};
        validateItem(validators, errorMap);

        dataContainer = new DataContainer<>("value", value);
        validators = new Validator[]{
                new NullValidator(dataContainer, value),
                new RegexpValidator(dataContainer, value, REG_POSITIVE_NUMBER)
        };
        validateItem(validators, errorMap);
    }

    private CartItemCountDTO obtainCartItem(HttpServletRequest req) {
        return new CartItemCountDTO()
                .setProductSku(req.getParameter(REQ_PARAM_COUNT_NAME))
                .setValue(req.getParameter(REQ_PARAM_COUNT_VALUE));
    }
}
