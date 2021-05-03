package com.mishchenkov.listener;

import com.mishchenkov.repository.DeliveryDAO;
import com.mishchenkov.repository.JDBCTemplate;
import com.mishchenkov.repository.ManufactureDAO;
import com.mishchenkov.repository.MySqlDeliveryDAO;
import com.mishchenkov.repository.MySqlManufactureDAO;
import com.mishchenkov.repository.MySqlOrderDAO;
import com.mishchenkov.repository.MySqlOrderProductDAO;
import com.mishchenkov.repository.MySqlOrderStatusDAO;
import com.mishchenkov.repository.MySqlPaymentDAO;
import com.mishchenkov.repository.MySqlProductDAO;
import com.mishchenkov.repository.MySqlProductTypeDAO;
import com.mishchenkov.repository.MySqlStatusDAO;
import com.mishchenkov.repository.MySqlUserDAO;
import com.mishchenkov.repository.OrderDAO;
import com.mishchenkov.repository.OrderProductsDAO;
import com.mishchenkov.repository.OrderStatusDAO;
import com.mishchenkov.repository.PaymentDAO;
import com.mishchenkov.repository.ProductDAO;
import com.mishchenkov.repository.ProductTypeDAO;
import com.mishchenkov.repository.StatusDAO;
import com.mishchenkov.repository.UserDAO;
import com.mishchenkov.service.CaptchaService;
import com.mishchenkov.service.CookieCaptchaService;
import com.mishchenkov.service.HiddenFieldCaptchaService;
import com.mishchenkov.service.PaginationService;
import com.mishchenkov.service.SessionCaptchaService;
import com.mishchenkov.service.repository.DeliveryService;
import com.mishchenkov.service.repository.MySqlDeliveryService;
import com.mishchenkov.service.repository.MySqlManufactureService;
import com.mishchenkov.service.repository.MySqlOrderService;
import com.mishchenkov.service.repository.MySqlPayDeliveryFacadeService;
import com.mishchenkov.service.repository.MySqlPaymentService;
import com.mishchenkov.service.repository.MySqlProductService;
import com.mishchenkov.service.repository.MySqlProductTypeService;
import com.mishchenkov.service.repository.MySqlUserService;
import com.mishchenkov.service.repository.OrderService;
import com.mishchenkov.service.repository.PaymentService;
import com.mishchenkov.service.repository.TransactionTemplate;
import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

@WebListener
public class StartAppListener implements ServletContextListener {

    private final Logger logger = Logger.getLogger(StartAppListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String preCaptchaTimeout = Optional.ofNullable(
                servletContext.getInitParameter(AppConstant.SERV_CXT_INIT_CAPTCHA_TIMEOUT)
        ).orElse(AppConstant.SERV_CXT_DEFAULT_CAPTCHA_TIMEOUT);

        String preCaptchaMethod = Optional.ofNullable(
                servletContext.getInitParameter(AppConstant.SERV_CXT_INIT_CAPTCHA_KEEP_METHOD)
        ).orElse(AppConstant.SERV_CXT_DEFAULT_CAPTCHA_KEEP_METHOD);

        servletContext.setAttribute(
                AppConstant.SERV_CXT_USER_AVATAR_DIRECTORY,
                Optional.ofNullable(
                        servletContext.getInitParameter(AppConstant.SERV_CXT_INIT_USER_AVATAR_DIRECTORY)
                ).orElse(AppConstant.SERV_CXT_DEFAULT_AVATAR_DIRECTORY)
        );

        servletContext.setAttribute(AppConstant.SERV_CXT_INIT_PRODUCT_ON_PAGE, AppConstant.SERV_CXT_DEFAULT_PRODUCT_ON_PAGE);
        servletContext.setAttribute(AppConstant.SERV_CXT_INIT_SERVER_PATH, servletContext.getRealPath("/"));

        servletContext.setAttribute(AppConstant.SERV_CXT_PAGINATION_SERVICE, new PaginationService());
        servletContext.setAttribute(AppConstant.SERV_CXT_AVAILABLE_PAGINATION_RANGE, new Integer[]{5,10,20});
        servletContext.setAttribute(AppConstant.SERV_CXT_PRODUCT_SORT_VALUES,
                Arrays.asList("default", "name_up", "name_down", "price_up", "price_down"));

        log4jInit(servletContext);
        dataSourceInit(servletContext);
        dataBaseServicesInit(servletContext);
        captchaServiceInit(servletContext, preCaptchaMethod, Integer.parseInt(preCaptchaTimeout));

    }

    private void dataBaseServicesInit(ServletContext servletContext) {
        DataSource dataSource = (DataSource) servletContext.getAttribute(AppConstant.SERV_CXT_DATA_SOURCE);
        JDBCTemplate jdbcTemplate = new JDBCTemplate();
        TransactionTemplate transactionTemplate = new TransactionTemplate(dataSource);

        UserDAO userDAO = new MySqlUserDAO(jdbcTemplate);
        ManufactureDAO manufactureDAO = new MySqlManufactureDAO(jdbcTemplate);
        ProductTypeDAO productTypeDAO = new MySqlProductTypeDAO(jdbcTemplate);
        ProductDAO productDAO = new MySqlProductDAO(jdbcTemplate);
        PaymentDAO paymentDAO = new MySqlPaymentDAO(jdbcTemplate);
        DeliveryDAO deliveryDAO = new MySqlDeliveryDAO(jdbcTemplate);
        OrderDAO orderDAO = new MySqlOrderDAO(jdbcTemplate);
        StatusDAO statusDAO = new MySqlStatusDAO(jdbcTemplate);
        OrderStatusDAO orderStatusDAO = new MySqlOrderStatusDAO(jdbcTemplate);
        OrderProductsDAO orderProductsDAO = new MySqlOrderProductDAO(jdbcTemplate);

        PaymentService paymentService = new MySqlPaymentService(paymentDAO, transactionTemplate);
        DeliveryService deliveryService = new MySqlDeliveryService(deliveryDAO, transactionTemplate);
        OrderService orderService = MySqlOrderService.getBuilder()
                .setDataSource(dataSource)
                .setUserDAO(userDAO)
                .setOrderDAO(orderDAO)
                .setDeliveryDAO(deliveryDAO)
                .setPaymentDAO(paymentDAO)
                .setStatusDAO(statusDAO)
                .setOrderStatusDAO(orderStatusDAO)
                .setProductDAO(productDAO)
                .setOrderProductDAO(orderProductsDAO)
                .build();

        servletContext.setAttribute(AppConstant.SERV_CXT_USER_SERVICE, new MySqlUserService(userDAO, dataSource));
        servletContext.setAttribute(AppConstant.SERV_CXT_MANUFACTURE_SERVICE, new MySqlManufactureService(manufactureDAO, transactionTemplate));
        servletContext.setAttribute(AppConstant.SERV_CXT_PRODUCT_TYPE_SERVICE, new MySqlProductTypeService(productTypeDAO, transactionTemplate));
        servletContext.setAttribute(AppConstant.SERV_CXT_PRODUCT_SERVICE, new MySqlProductService(productDAO, transactionTemplate));
        servletContext.setAttribute(AppConstant.SERV_CXT_PAY_DELIVERY_SERVICE, new MySqlPayDeliveryFacadeService(deliveryService, paymentService));
        servletContext.setAttribute(AppConstant.SERV_CXT_ORDER_SERVICE, orderService);
    }

    private void dataSourceInit(ServletContext servletContext) {
        try {
            Context context = new InitialContext();
            Context ct = (Context) context.lookup("java:comp/env");
            DataSource dataSource = (DataSource) ct.lookup("jdbc/ST_FOR_CONNECTION");
            servletContext.setAttribute(AppConstant.SERV_CXT_DATA_SOURCE, dataSource);
        } catch (NamingException e) {
            logger.warn(e);
        }

    }

    private void log4jInit(ServletContext servletContext) {
        PropertyConfigurator.configure(
                servletContext.getRealPath("WEB-INF/log4j.properties")
        );
    }

    private void captchaServiceInit(ServletContext servletContext, String keepMethod, long seconds) {
        CaptchaService captchaService = obtainCaptchaService(keepMethod, seconds);
        servletContext.setAttribute(AppConstant.SERV_CXT_CAPTCHA_SERVICE, captchaService);
    }

    private CaptchaService obtainCaptchaService(String keepMethod, long seconds) {
        Map<String, Supplier<CaptchaService>> serviceMap = new TreeMap<>();
        serviceMap.put(AppConstant.CAPTCHA_KEEP_SESSION, () -> new SessionCaptchaService(seconds));
        serviceMap.put(AppConstant.CAPTCHA_KEEP_COOKIE, () -> new CookieCaptchaService(seconds));
        serviceMap.put(AppConstant.CAPTCHA_KEEP_FORM, () -> new HiddenFieldCaptchaService(seconds));

        return serviceMap.get(keepMethod).get();
    }
}
