package com.mishchenkov.service;

import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CookieCaptchaService extends AbstractContextStorageCaptchaService {

    private final Logger logger = Logger.getLogger(CookieCaptchaService.class);

    public CookieCaptchaService(long timeoutSeconds) {
        super(timeoutSeconds);
    }

    @Override
    protected String getFieldId(HttpServletRequest request) {
        Optional<Cookie> cookieOptional = Arrays.stream(request.getCookies())
                .filter(cke -> AppConstant.COOKIE_ID.equals(cke.getName()))
                .findFirst();

        if (cookieOptional.isPresent()) {
            return cookieOptional.get().getValue();
        } else {
            logger.warn(AppConstant.EXP_MSG_COOKIE_ID_IS_EMPTY);
            throw new IllegalStateException(AppConstant.EXP_MSG_COOKIE_ID_IS_EMPTY);
        }
    }
}
