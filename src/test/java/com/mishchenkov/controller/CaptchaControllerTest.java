package com.mishchenkov.controller;

import com.mishchenkov.service.CaptchaService;
import com.mishchenkov.service.SessionCaptchaService;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CaptchaControllerTest {

    private static final String SERV_CXT_CAPTCHA_SERVICE = "captchaService";
    private static final String JSESSIONID = "2983671F08637D8B079244C2BEA021FC";

    @Test
    public void shouldGetOutputStreamFromMethod_doGetTest() throws IOException {
        //given
        CaptchaServicesController controller = new CaptchaServicesController();

        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext servletContext = mock(ServletContext.class);
        CaptchaService captchaService = mock(SessionCaptchaService.class);
        ServletOutputStream outputStream = mock(ServletOutputStream.class);

        when(session.getId()).thenReturn(JSESSIONID);
        when(response.getOutputStream()).thenReturn(outputStream);
        when(servletContext.getAttribute(SERV_CXT_CAPTCHA_SERVICE)).thenReturn(captchaService);
        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getSession()).thenReturn(session);

        //when
        controller.doGet(request, response);

        //then
        verify(outputStream, times(1)).flush();
    }
}