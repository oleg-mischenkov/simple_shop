package com.mishchenkov.filter;

import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CacheFilter extends AbstractHttpFilter {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String CACHE_ON = "max-age=".concat(AppConstant.TIME_IN_SECOND_TWENTY_FOUR_HOURS);
    private static final String CACHE_OFF = "no-cache";

    private static final Map<String,String> CACHE_MAP = new TreeMap<>();
    static {
        CACHE_MAP.put("on", CACHE_ON);
        CACHE_MAP.put("off", CACHE_OFF);
    }

    private String cacheType;

    @Override
    public void init(FilterConfig filterConfig) {
        cacheType = Optional.ofNullable(filterConfig.getInitParameter(AppConstant.FILTER_INIT_CACHE))
                .orElse(CACHE_OFF)
                .toLowerCase();
        logger.trace(cacheType);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setHeader(AppConstant.REQ_HEADER_CACHE, getCacheValueHeader(cacheType));
        chain.doFilter(request, response);
    }

    private static String getCacheValueHeader(String value) {
        return Optional.ofNullable(CACHE_MAP.get(value)).orElse(CACHE_OFF);
    }
}
