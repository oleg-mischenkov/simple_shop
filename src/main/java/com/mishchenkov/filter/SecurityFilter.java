package com.mishchenkov.filter;

import com.mishchenkov.dto.SecurityDTO;
import com.mishchenkov.entity.User;
import com.mishchenkov.xml.ConstraintParser;
import com.mishchenkov.xml.SecurityConstraint;
import com.mishchenkov.xml.XMLParser;
import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SecurityFilter extends AbstractHttpFilter {

    private final Logger logger = Logger.getLogger(this.getClass());

    private static final String PAGE_LOGIN = "login.jsp";
    private static final String PAGE_DENIED = "no-access";
    private static final String REQ_METHOD_GET = "GET";

    private List<SecurityConstraint> constraints;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        XMLParser<List<SecurityConstraint>> xmlParser = new ConstraintParser();
        constraints = xmlParser.parse(obtainXmlPath(filterConfig));
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean isChain = false;

        if (isDynamic.and(isGet).test(request)) {
            Optional<SecurityConstraint> preRule = getConstraintIfExist(request);

            if (preRule.isPresent()) isChain = isGoodUser(request, response, preRule.get());
        }

        if (!isChain) chain.doFilter(request, response);
    }

    private String obtainXmlPath(FilterConfig filterConfig) throws ServletException {
        return filterConfig.getServletContext().getRealPath(
                Optional.ofNullable(filterConfig.getInitParameter(AppConstant.FILTER_INIT_SECURITY_CONFIG))
                        .orElseThrow(() -> new ServletException("Invalid security config parameter."))
        );
    }

    private Optional<SecurityConstraint> getConstraintIfExist(HttpServletRequest request) {
        return constraints.stream()
                .filter(security -> request.getRequestURL().toString().matches(security.getPath()))
                .findFirst();
    }

    private boolean isGoodUser(HttpServletRequest request, HttpServletResponse response, SecurityConstraint rule) {
        return loginCheck.or(constraintCheck)
                .test(new SecurityDTO()
                        .setRequest(request)
                        .setResponse(response)
                        .setConstraint(rule)
                );
    }

    private final Predicate<HttpServletRequest> isDynamic = req -> !req.getRequestURL().toString().contains("/static/");
    private final Predicate<HttpServletRequest> isGet = req -> REQ_METHOD_GET.equals(req.getMethod());

    private final Predicate<SecurityDTO> loginCheck = container -> {
        User user = obtainUser(container);

        if (isNull(user.getEmail())) {
            return sendRedirect(container, PAGE_LOGIN);
        }
        return false;
    };

    private final Predicate<SecurityDTO> constraintCheck = container -> {
        User user = obtainUser(container);

        if (nonNull(user.getName()) && !isComplimentUserRole(container, user)) {
            return sendRedirect(container, PAGE_DENIED);
        }
        return false;
    };

    private boolean isComplimentUserRole(SecurityDTO container, User user) {
        return container.getConstraint().getRoles().contains(user.getRole());
    }

    private boolean sendRedirect(SecurityDTO container, String page) {
        try {
            container.getHttpResponse().sendRedirect(page);
        } catch (IOException e) {
            logger.error(e);
            throw new IllegalStateException(e);
        }
        return true;
    }

    private User obtainUser(SecurityDTO container) {
        return (User) Optional.ofNullable(
                container.getHttpRequest().getSession().getAttribute(AppConstant.SESSION_ITEM_CURRENT_USER)
        ).orElseGet(User::new);
    }

}