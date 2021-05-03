package com.mishchenkov.service;

import com.mishchenkov.entity.DataContainer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

public interface CaptchaService {

    void putCaptcha(HttpServletRequest request, DataContainer<String, LocalTime> captchaContainer);

    DataContainer<String, LocalTime> deriveCaptcha(HttpServletRequest request);

}
