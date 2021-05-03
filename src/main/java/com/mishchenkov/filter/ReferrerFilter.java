package com.mishchenkov.filter;

import com.mishchenkov.constant.AppConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class ReferrerFilter extends AbstractHttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String refPage = getRefPageFromRequest(request);

        request.getSession().setAttribute(AppConstant.SESSION_ATTR_REFERER, refPage);
        chain.doFilter(request, response);
    }

    private String getRefPageFromRequest(HttpServletRequest request) {
        return Arrays.stream(
                Optional.ofNullable(request.getHeader(AppConstant.REQ_HEADER_REFERRER)).orElse("/".concat(AppConstant.MAIN_SITE_PAGE)).split("/")
        ).reduce((first, sec) -> sec).get();
    }

}
