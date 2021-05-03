package com.mishchenkov.service;

import com.mishchenkov.constant.AppConstant;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HiddenFieldCaptchaService extends AbstractContextStorageCaptchaService {

    private final Logger logger = Logger.getLogger(HiddenFieldCaptchaService.class);

    public HiddenFieldCaptchaService(long timeoutSeconds) {
        super(timeoutSeconds);
    }

    @Override
    protected String getFieldId(HttpServletRequest request) {
        Optional<String> hiddenOptional = Optional.ofNullable(request.getParameter(AppConstant.REQ_PARAM_USER_HIDDEN));

        if (hiddenOptional.isPresent()) {
            return hiddenOptional.get();
        } else {
            logger.warn(AppConstant.EXP_MSG_HIDDEN_FIELD_VALUE);
            throw new IllegalStateException(AppConstant.EXP_MSG_HIDDEN_FIELD_VALUE);
        }
    }
}
