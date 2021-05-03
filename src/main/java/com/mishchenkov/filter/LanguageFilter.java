package com.mishchenkov.filter;

import com.mishchenkov.dto.ServletTransportDTO;
import com.mishchenkov.servlet.LanguageHttpRequestWrapper;
import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanguageFilter extends AbstractHttpFilter {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String SCOPE_SESSION = "SESSION";
    private static final String SCOPE_COOKIES = "COOKIES";
    private static final String REQ_PARAM_LANGUAGE = "lang";

    private Locale defaultLocale;
    private List<Locale> maintainLocale;
    private String localeKeepScope;
    private int cookieLife;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.trace("INIT Language filter");
        defaultLocale = deriveInitLocale(filterConfig);
        maintainLocale = deriveInitAppLocales(filterConfig);
        localeKeepScope = deriveInitLocaleScope(filterConfig);
        cookieLife = deriveInitCookieLife(filterConfig);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Locale currentLocale = new Locale(
                Optional.ofNullable(request.getParameter(REQ_PARAM_LANGUAGE))
                        .orElseGet(() ->  selectLocale(request).getLanguage())
        );

        ServletTransportDTO container = new ServletTransportDTO(request, response);
        putLocaleToScope(container, currentLocale);

        LanguageHttpRequestWrapper requestWrapper = new LanguageHttpRequestWrapper(request, currentLocale);

        requestWrapper.setAttribute(AppConstant.REQ_ATTR_LANGUAGE_LIST, getLanguageNames());
        chain.doFilter(requestWrapper, response);
    }

    private int deriveInitCookieLife(FilterConfig filterConfig) {
        return Integer.parseInt(
                Optional.ofNullable(filterConfig.getInitParameter(AppConstant.FILTER_INIT_COOKIE_LANGUAGE_LIFE)).orElse("600")
        );
    }

    private String deriveInitLocaleScope(FilterConfig filterConfig) throws ServletException {
        return Optional.ofNullable(filterConfig.getInitParameter(AppConstant.FILTER_INIT_LANGUAGE_SCOPE))
                .orElseThrow(() -> new ServletException("Init locale scope is null."));
    }

    private List<Locale> deriveInitAppLocales(FilterConfig filterConfig) throws ServletException {
        return Arrays.stream(
                Optional.ofNullable(filterConfig.getInitParameter(AppConstant.FILTER_INIT_APP_LOCALES))
                        .orElseThrow(() -> new ServletException("Default application locales are empties"))

                        .replace(" ", "")
                        .split(";")

        ).map(Locale::new)
                .collect(Collectors.toList());
    }

    private Locale deriveInitLocale(FilterConfig filterConfig) throws ServletException {
        return new Locale(
                Optional.ofNullable(filterConfig.getInitParameter(AppConstant.FILTER_INIT_DEFAULT_LOCALE))
                        .orElseThrow(() -> new ServletException("Default Locale value is null."))
        );
    }

    private void putLocaleToScope(ServletTransportDTO container, Locale locale) {
        Map<String,Consumer<ServletTransportDTO>> scopeSelectMap = new HashMap<>();
        scopeSelectMap.put(SCOPE_SESSION, cont ->
                cont.getHttpRequest().getSession().setAttribute(AppConstant.SESSION_ATTR_LOCALE, locale.getLanguage()));
        scopeSelectMap.put(SCOPE_COOKIES, cont ->
                cont.getHttpResponse().addCookie(getCookie(locale.getLanguage())));

        scopeSelectMap.get(localeKeepScope).accept(container);
    }

    private Cookie getCookie(String value) {
        Cookie cookie = new Cookie(AppConstant.COOKIES_LOCALE, value);
        cookie.setMaxAge(cookieLife);
        return cookie;
    }

    private String[] getLanguageNames() {
        return maintainLocale.stream().map(Locale::getLanguage).toArray(String[]::new);
    }

    private Locale selectLocale(HttpServletRequest request) {
        return Stream.of(
                    cookieFunction,
                    sessionFunction,
                    defaultAppLocalesFunction,
                    defaultLocaleFunction
                )
                .map(e -> e.apply(request))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseGet(Optional::empty)
                .map(Locale::new)
                .orElseGet(() -> defaultLocale);
    }

    private final Function<HttpServletRequest,Optional<String>> cookieFunction = req ->
            Arrays.stream(req.getCookies())
                    .filter(cookie -> AppConstant.COOKIES_LOCALE.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();

    private final Function<HttpServletRequest,Optional<String>> sessionFunction = req ->
            Optional.ofNullable((String)req.getSession().getAttribute(AppConstant.SESSION_ATTR_LOCALE));

    private final Function<HttpServletRequest,Optional<String>> defaultAppLocalesFunction = req ->
            obtainUserLocales(req.getLocales())
                    .stream()
                    .map(Locale::getLanguage)
                    .filter(s -> maintainLocale.contains(new Locale(s)))
                    .findFirst();

    private final Function<HttpServletRequest,Optional<String>> defaultLocaleFunction = req ->
            Optional.ofNullable(defaultLocale.getLanguage());

    private List<Locale> obtainUserLocales(Enumeration<Locale> localeEnumeration) {
        List<Locale> localeList = new ArrayList<>();

        while (localeEnumeration.hasMoreElements()) {
            localeList.add(localeEnumeration.nextElement());
        }

        return localeList;
    }

}