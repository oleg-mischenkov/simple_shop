package com.mishchenkov.constant;

public final class AppConstant {

    private AppConstant() {}

    /* Fail msg */
    public static final String FAIL_MSG_INVALID_USER_NAME = "User name is incorrect";
    public static final String FAIL_MSG_INVALID_USER_SECOND_NAME = "User second name is incorrect";
    public static final String FAIL_MSG_INVALID_USER_MAIL = "User e-mail is incorrect";
    public static final String FAIL_MSG_USER_IS_EXIST = "System has this e-mail";
    public static final String FAIL_MSG_INVALID_FIRST_PSW = "User password is incorrect";
    public static final String FAIL_MSG_INVALID_SECOND_PSW = "User second password is incorrect";
    public static final String FAIL_MSG_NOT_EQUAL_SECOND_PSW = "A second password is not equal first password";
    public static final String FAIL_MSG_INVALID_CAPTCHA = "Captcha is empty";
    public static final String FAIL_MSG_INCORRECT_CAPTCHA = "Captcha is incorrect";
    public static final String FAIL_MSG_TIMEOUT_CAPTCHA = "Captcha timeout is over";
    public static final String FAIL_MSG_BED_USER_PARAM = "Login or password is incorrect";
    public static final String FAIL_MSG_BAD_PRODUCT_ID = "incorrect product id";

    /* Regexp patterns */
    public static final String REG_USER_NAME = "^([^\\d|^\\s|\\W]{3,})$";
    public static final String REG_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String REG_USER_PSW = "^([\\w\\W]{6,})$";
    public static final String REG_POSITIVE_NUMBER = "^\\d+$";
    public static final String REG_CREDIT_CARD = "[\\d]{4}-[\\d]{4}-[\\d]{4}-[\\d]{4}";

    /* Common request params */
    public static final String REQ_PARAM_USER_HIDDEN = "hidden";

    /* Common request attribute */
    public static final String REQ_ATTR_LANGUAGE_LIST = "languageList";

    /* Servlet context values */
    public static final String SERV_CXT_ORDER_SERVICE = "orderDelService";
    public static final String SERV_CXT_PAY_DELIVERY_SERVICE = "payDelService";
    public static final String SERV_CXT_USER_SERVICE = "userService";
    public static final String SERV_CXT_PRODUCT_SERVICE = "productService";
    public static final String SERV_CXT_MANUFACTURE_SERVICE = "manufactureService";
    public static final String SERV_CXT_PRODUCT_TYPE_SERVICE = "productTypeService";
    public static final String SERV_CXT_CAPTCHA_SERVICE = "captchaService";
    public static final String SERV_CXT_PAGINATION_SERVICE = "PaginationService";
    public static final String SERV_CXT_DATA_SOURCE = "dataSource";
    public static final String SERV_CXT_USER_AVATAR_DIRECTORY = "avatarDirectory";
    public static final String SERV_CXT_AVAILABLE_PAGINATION_RANGE = "paginationRange";
    public static final String SERV_CXT_PRODUCT_SORT_VALUES = "productSortValues";

    /* Servlet context init params */
    public static final String SERV_CXT_INIT_CAPTCHA_TIMEOUT = "captchaTimeoutSec";
    public static final String SERV_CXT_INIT_CAPTCHA_KEEP_METHOD = "captchaKeepingMethod";
    public static final String SERV_CXT_INIT_USER_AVATAR_DIRECTORY = "userAvatarDirectory";
    public static final String SERV_CXT_INIT_SERVER_PATH = "serverRootDir";
    public static final String SERV_CXT_INIT_PRODUCT_ON_PAGE = "productOnPage";

    /* Filters init params */
    public static final String FILTER_INIT_CACHE = "cache";
    public static final String FILTER_INIT_LANGUAGE_SCOPE = "languageScope";
    public static final String FILTER_INIT_COOKIE_LANGUAGE_LIFE = "cookieLife";
    public static final String FILTER_INIT_DEFAULT_LOCALE = "defaultLocale";
    public static final String FILTER_INIT_APP_LOCALES = "appLocales";
    public static final String FILTER_INIT_SECURITY_CONFIG = "configFile";

    /* Servlet context default init values */
    public static final String SERV_CXT_DEFAULT_CAPTCHA_TIMEOUT = "60";
    public static final String SERV_CXT_DEFAULT_CAPTCHA_KEEP_METHOD = "SESSION";
    public static final String SERV_CXT_DEFAULT_AVATAR_DIRECTORY = "avatar";
    public static final String SERV_CXT_DEFAULT_PRODUCT_ON_PAGE = "5";
    public static final String SERV_CXT_DEFAULT_SORT_VALUE = "default";

    /* Captcha keeping methods */
    public static final String CAPTCHA_KEEP_SESSION = "SESSION";
    public static final String CAPTCHA_KEEP_COOKIE = "CONTEXT_COOKIE";
    public static final String CAPTCHA_KEEP_FORM = "CONTEXT_FORM_FIELD";

    /* Session values */
    public static final String SESSION_CURRENT_ORDER = "currentOrder";
    public static final String SESSION_ITEM_CAPTCHA = "sessionCaptcha";
    public static final String SESSION_ITEM_CURRENT_USER = "currentUser";
    public static final String SESSION_ATTR_CURRENT_PAGINATION_COUNT = "paginationCount";
    public static final String SESSION_ATTR_CURRENT_SORT_VALUE = "sortValue";
    public static final String SESSION_ATTR_USER_CART = "userCart";
    public static final String SESSION_ATTR_LOCALE = "sessionLocale";
    public static final String SESSION_ATTR_REFERER = "Referer";

    /* Cookies values */
    public static final String COOKIES_LOCALE = "cookieLocale";

    /* Cookie values */
    public static final String COOKIE_ID = "JSESSIONID";

    /* Http headers */
    public static final String REQ_HEADER_CACHE = "Cache-Control";
    public static final String REQ_HEADER_COMPRESS = "Accept-Encoding";
    public static final String REQ_HEADER_REFERRER = "Referer";

    /* Exception values */
    public static final String EXP_MSG_COOKIE_ID_IS_EMPTY = "The cookie id [JSESSIONID] isn't exist";
    public static final String EXP_MSG_HIDDEN_FIELD_VALUE = "The hidden field value isn't exist";

    /* Site path's */
    public static final String PATH_IMAGES = "static/images";
    public static final String PATH_404 = "404.html";
    public static final String MAIN_SITE_PAGE = "index.jsp";

    /* MIME TYPES */
    public static final String MIME_TEXT = "text/html";

    /* Time */
    public static final String TIME_IN_SECOND_TWENTY_FOUR_HOURS = "86400";
}
