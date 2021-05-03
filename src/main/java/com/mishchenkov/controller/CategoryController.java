package com.mishchenkov.controller;

import com.mishchenkov.dto.CategoryPaginationAndSortStateDTO;
import com.mishchenkov.dto.ManufactureDTO;
import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.dto.ProductFilterFormDTO;
import com.mishchenkov.dto.ProductTypeDTO;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.entity.Manufacture;
import com.mishchenkov.entity.Product;
import com.mishchenkov.entity.ProductType;
import com.mishchenkov.service.PaginationService;
import com.mishchenkov.service.repository.ManufactureService;
import com.mishchenkov.service.repository.ProductService;
import com.mishchenkov.service.repository.ProductTypeService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.mishchenkov.constant.AppConstant.PATH_404;
import static com.mishchenkov.constant.AppConstant.REG_POSITIVE_NUMBER;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_MANUFACTURE_SERVICE;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_PAGINATION_SERVICE;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_PRODUCT_SORT_VALUES;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_PRODUCT_TYPE_SERVICE;
import static com.mishchenkov.constant.AppConstant.SESSION_ATTR_CURRENT_PAGINATION_COUNT;
import static com.mishchenkov.constant.AppConstant.SESSION_ATTR_CURRENT_SORT_VALUE;

@WebServlet("/category")
public class CategoryController extends CommonServicesHttpServlet {

    private final Logger logger = Logger.getLogger(CategoryController.class);

    public static final String REQ_PARAM_MIN_PRICE = "price-from";
    public static final String REQ_PARAM_MAX_PRICE = "price-to";
    public static final String REQ_PARAM_PRODUCT_TITLE = "prod-name";
    public static final String REQ_PARAM_MANUFACTURE = "prod-manufacture";
    public static final String REQ_PARAM_PROD_TYPE = "prod-type";
    public static final String REQ_PARAM_PAGINATION_COUNT = "count";
    public static final String REQ_PARAM_PAGINATION_POSITION = "position";
    public static final String REQ_PARAM_SORT_BY = "sort-by";

    public static final String REQ_ATTR_MANUFACTURES = "manufactures";
    public static final String REQ_ATTR_PRODUCT_TYPES = "productTypes";
    public static final String REQ_ATTR_MOST_HEIGHT_PROD_PRICE = "heightPrice";
    public static final String REQ_ATTR_PRODUCT_LIST = "productList";
    public static final String REQ_ATTR_CURRENT_FORM_FILTER = "formFilter";
    public static final String REQ_ATTR_PAGINATION_RANGE = "paginationRange";
    public static final String REQ_ATTR_PAGINATION_POSITION = "paginationPosition";

    public static final String OK_PAGE = "/WEB-INF/view/category.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        ServletContext servletContext = req.getServletContext();
        ManufactureService manufactureService = getManufactureService(servletContext);
        ProductTypeService productTypeService = getProductTypeService(servletContext);
        ProductService productService = getProductService(servletContext);
        PaginationService paginationService = getPaginationService(servletContext);

        req.setAttribute(REQ_ATTR_MANUFACTURES, obtainManufactureListFromStorage(manufactureService));
        req.setAttribute(REQ_ATTR_PRODUCT_TYPES, obtainProdTypeListFromStorage(productTypeService));
        req.setAttribute(REQ_ATTR_MOST_HEIGHT_PROD_PRICE, obtainHeightPriceFromStorage(productService));

        List<ProductDTO> productList;
        ProductFilterFormDTO filterFormDTO = new ProductFilterFormDTO();
        CategoryPaginationAndSortStateDTO sortStateDTO = new CategoryPaginationAndSortStateDTO();

        if (req.getQueryString() != null) {
            Map<String, String> errorMap = new TreeMap<>();

            filterFormDTO = obtainFilterFormDataFromRequestParam(req);
            sortStateDTO = obtainPaginationSortDataFromRequestParam(req);
            validateFilterForm(filterFormDTO, errorMap, req);
            validatePaginationSortByData(sortStateDTO, errorMap, getSortValuesAttribute(servletContext));

            setSessionAttr(session, sortStateDTO);

            if (errorMap.size() > 0) {
                resp.sendRedirect(PATH_404);
                return;
            } else {
                req.setAttribute(REQ_ATTR_CURRENT_FORM_FILTER, filterFormDTO);
            }
        }

        initDefaultValuesForPaginationSortDTO(sortStateDTO, session);
        productList = obtainProductListFromStorage(productService, filterFormDTO, sortStateDTO);
        req.setAttribute(REQ_ATTR_PAGINATION_RANGE, obtainPaginationRange(productService, paginationService, filterFormDTO, session));
        req.setAttribute(REQ_ATTR_PRODUCT_LIST, productList);
        req.setAttribute(REQ_ATTR_PAGINATION_POSITION, calculatePosition(sortStateDTO));

        req.getRequestDispatcher(OK_PAGE).forward(req, resp);
    }

    private Integer calculatePosition(CategoryPaginationAndSortStateDTO sortStateDTO) {
        return Integer.parseInt(sortStateDTO.getPosition()) / Integer.parseInt(sortStateDTO.getCount());
    }

    private List<String> getSortValuesAttribute(ServletContext servletContext) {
        return (List<String>) servletContext.getAttribute(SERV_CXT_PRODUCT_SORT_VALUES);
    }

    private void initDefaultValuesForPaginationSortDTO(CategoryPaginationAndSortStateDTO sortStateDTO, HttpSession session) {
        if (sortStateDTO.getCount() == null) sortStateDTO.setCount((String) session.getAttribute(SESSION_ATTR_CURRENT_PAGINATION_COUNT));
        if (sortStateDTO.getPosition() == null) sortStateDTO.setPosition("0");
        if (sortStateDTO.getSortBy() == null) sortStateDTO.setSortBy((String) session.getAttribute(SESSION_ATTR_CURRENT_SORT_VALUE));
    }

    private void setSessionAttr(HttpSession session, CategoryPaginationAndSortStateDTO sortStateDTO) {
        if (sortStateDTO.getCount() != null) session.setAttribute(SESSION_ATTR_CURRENT_PAGINATION_COUNT, sortStateDTO.getCount());
        if (sortStateDTO.getSortBy() != null) session.setAttribute(SESSION_ATTR_CURRENT_SORT_VALUE, sortStateDTO.getSortBy());
    }

    private String[] obtainPaginationRange(ProductService productService, PaginationService paginationService,
                                           ProductFilterFormDTO filterFormDTO, HttpSession session) {
        int itemCount = Integer.parseInt((String)session.getAttribute(SESSION_ATTR_CURRENT_PAGINATION_COUNT));
        return paginationService.getPages(itemCount, obtainProductCountFromStorage(productService, filterFormDTO));
    }

    private int obtainProductCountFromStorage(ProductService productService, ProductFilterFormDTO filterFormDTO) {
        int result;
        try {
            result = productService.selectProductCountByFilter(filterFormDTO).orElse(0);
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
        return result;
    }

    private PaginationService getPaginationService(ServletContext servletContext) {
        return (PaginationService) servletContext.getAttribute(SERV_CXT_PAGINATION_SERVICE);
    }

    private List<ProductDTO> obtainProductListFromStorage(ProductService productService,
                                                       ProductFilterFormDTO filterFormDTO,
                                                       CategoryPaginationAndSortStateDTO sortStateDTO) {
        List<ProductDTO> products;
        try {
            products = productService.selectProductsByFilter(filterFormDTO, sortStateDTO).orElse(new ArrayList<>());

        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }

        return products;
    }

    private void validateFilterForm(ProductFilterFormDTO filterFormDTO, Map<String, String> errorMap, HttpServletRequest request) {
        List<String> formParamList = Arrays.asList(
                REQ_PARAM_MIN_PRICE, REQ_PARAM_MAX_PRICE,
                REQ_PARAM_PRODUCT_TITLE, REQ_PARAM_MANUFACTURE,
                REQ_PARAM_PROD_TYPE);

        if (!matchAny(formParamList, getAllPassedRequestParamNames(request))) return;

        DataContainer<String,String> dataContainer = new DataContainer<>(REQ_PARAM_MIN_PRICE, filterFormDTO.getMinPrice());
        Validator[] validators = {
                new NullValidator(dataContainer, REQ_PARAM_MIN_PRICE),
                new RegexpValidator(dataContainer, REQ_PARAM_MIN_PRICE, REG_POSITIVE_NUMBER)
        };
        validateItem(validators, errorMap);

        dataContainer = new DataContainer<>(REQ_PARAM_MAX_PRICE, filterFormDTO.getMaxPrice());
        validators = new Validator[] {
                new NullValidator(dataContainer, REQ_PARAM_MAX_PRICE),
                new RegexpValidator(dataContainer, REQ_PARAM_MAX_PRICE, REG_POSITIVE_NUMBER)
        };
        validateItem(validators, errorMap);

        if (errorMap.size() == 0 && !isMaxPriceMoreThenMinPrice(filterFormDTO)) {
            errorMap.put(REQ_PARAM_MAX_PRICE, REQ_PARAM_MAX_PRICE);
        }

        Manufacture manufacture = new Manufacture(filterFormDTO.getManufacture());
        List<Manufacture> manufactureList = (List<Manufacture>) request.getAttribute(REQ_ATTR_MANUFACTURES);
        if (manufacture.getName() != null && !manufactureList.contains(manufacture)) {
            errorMap.put(REQ_PARAM_MANUFACTURE, REQ_PARAM_MANUFACTURE);
        }

        ProductType productType = new ProductType(filterFormDTO.getProductType());
        List<ProductType> productTypeList = (List<ProductType>) request.getAttribute(REQ_ATTR_PRODUCT_TYPES);
        if (productType.getName() != null && !productTypeList.contains(productType)) {
            errorMap.put(REQ_PARAM_PROD_TYPE, REQ_PARAM_PROD_TYPE);
        }
    }

    private void validatePaginationSortByData(CategoryPaginationAndSortStateDTO sortStateDTO,
                                              Map<String, String> errorMap, List<String> sortValueList) {
        String count = sortStateDTO.getCount();
        String position = sortStateDTO.getPosition();

        if (count != null && position != null) {
            DataContainer<String,String> countContainer = new DataContainer<>(REQ_PARAM_PAGINATION_COUNT, count);
            DataContainer<String,String> positionContainer = new DataContainer<>(REQ_PARAM_PAGINATION_POSITION, position);

            Validator[] validators = {
                    new RegexpValidator(countContainer, REQ_PARAM_PAGINATION_COUNT, REG_POSITIVE_NUMBER),
                    new RegexpValidator(positionContainer, REQ_PARAM_PAGINATION_POSITION, REG_POSITIVE_NUMBER)
            };
            validateItem(validators, errorMap);

        }

        if (sortStateDTO.getSortBy() != null && !sortValueList.contains(sortStateDTO.getSortBy())) {
            errorMap.put(REQ_PARAM_SORT_BY, REQ_PARAM_SORT_BY);
        }
    }

    private boolean matchAny(List<String> formParamList, List<String> allPassedRequestParamNames) {
        return allPassedRequestParamNames.stream().anyMatch(formParamList::contains);
    }

    private List<String> getAllPassedRequestParamNames(HttpServletRequest request) {
        return Arrays.stream(request.getQueryString().split("&"))
                .map(s -> s.substring(0, s.indexOf('=')))
                .collect(Collectors.toList());
    }

    private boolean isMaxPriceMoreThenMinPrice(ProductFilterFormDTO filterFormDTO) {
        return Integer.parseInt(filterFormDTO.getMinPrice()) <= Integer.parseInt(filterFormDTO.getMaxPrice());
    }

    private ProductFilterFormDTO obtainFilterFormDataFromRequestParam(HttpServletRequest req) {
        return new ProductFilterFormDTO()
                .setMinPrice(req.getParameter(REQ_PARAM_MIN_PRICE))
                .setMaxPrice(req.getParameter(REQ_PARAM_MAX_PRICE))
                .setProductTitle("".equals(req.getParameter(REQ_PARAM_PRODUCT_TITLE)) ? null : req.getParameter(REQ_PARAM_PRODUCT_TITLE))
                .setManufacture(req.getParameter(REQ_PARAM_MANUFACTURE))
                .setProductType(req.getParameter(REQ_PARAM_PROD_TYPE));
    }

    private CategoryPaginationAndSortStateDTO obtainPaginationSortDataFromRequestParam(HttpServletRequest req) {
        return new CategoryPaginationAndSortStateDTO()
                .setCount(req.getParameter(REQ_PARAM_PAGINATION_COUNT))
                .setPosition(req.getParameter(REQ_PARAM_PAGINATION_POSITION))
                .setSortBy(req.getParameter(REQ_PARAM_SORT_BY));
    }

    private int obtainHeightPriceFromStorage(ProductService productService) {
        int price;

        try {
            Optional<ProductDTO> optional = productService.selectMostExpensiveProduct();
            Product product = optional.orElse(new ProductDTO().setProduct(new Product())).getProduct();

            price = product.getPrice().getMoneyValue();

        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }

        return price;
    }

    private List<ProductType> obtainProdTypeListFromStorage(ProductTypeService productTypeService) {
        List<ProductType> productTypeList = null;

        try {
            Optional<List<ProductTypeDTO>> optional = productTypeService.selectAll();

            if (optional.isPresent()) {
                productTypeList = optional.get().stream()
                        .map(ProductTypeDTO::getProductType)
                        .collect(Collectors.toList());
            }

        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }

        return productTypeList;
    }

    private List<Manufacture> obtainManufactureListFromStorage(ManufactureService manufactureService) {
        List<Manufacture> manufactureList = null;

        try {
            Optional<List<ManufactureDTO>> optional = manufactureService.selectAll();

            if (optional.isPresent()) {
                manufactureList = optional.get().stream()
                        .map(ManufactureDTO::getManufacture)
                        .collect(Collectors.toList());
            }

        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }

        return manufactureList;
    }

    private ProductTypeService getProductTypeService(ServletContext servletContext) {
        return (ProductTypeService) servletContext.getAttribute(SERV_CXT_PRODUCT_TYPE_SERVICE);
    }

    private ManufactureService getManufactureService(ServletContext servletContext) {
        return (ManufactureService) servletContext.getAttribute(SERV_CXT_MANUFACTURE_SERVICE);
    }
}