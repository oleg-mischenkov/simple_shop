package com.mishchenkov.controller;

import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.service.CaptchaService;
import com.mishchenkov.service.SessionCaptchaService;
import com.mishchenkov.service.repository.MySqlUserService;
import com.mishchenkov.service.repository.UserService;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistrationControllerTest {

    private static final String REQ_PARAM_USER_HIDDEN = "hidden";
    private static final String REQ_PARAM_USER_NAME = "name";
    private static final String REQ_PARAM_USER_SECOND_NAME = "second-name";
    private static final String REQ_PARAM_USER_EMAIL = "email";
    private static final String REQ_PARAM_USER_FIRST_PSW = "psw-1";
    private static final String REQ_PARAM_USER_SECOND_PSW = "psw-2";
    private static final String REQ_PARAM_USER_IS_GET_EMAIL = "is-email";
    private static final String REQ_PARAM_USER_CAPTCHA = "captcha";

    private static final String CAPTCHA_VALUE = "55555";
    private static final String JSESSIONID = "2983671F08637D8B079244C2BEA021FC";

    private static final String PAGE_MAIN_VIEW = "/WEB-INF/view/registration.jsp";
    public static final String PAGE_SUCCESS = "index.html";

    private static final String SERV_CXT_USER_SERVICE = "userService";
    private static final String SERV_CXT_CAPTCHA_SERVICE = "captchaService";

    @Test
    @Ignore
    public void shouldCallMainView_doGetTest() throws ServletException, IOException {
        //given
        RegistrationController controller = new RegistrationController();

        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(session.getId()).thenReturn(JSESSIONID);
        when(request.getRequestDispatcher(PAGE_MAIN_VIEW)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        //when
        controller.doGet(request, response);

        //then
        verify(request, times(1)).getRequestDispatcher(PAGE_MAIN_VIEW);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @Ignore
    public void shouldImitateCaseWhenUserFormHasSomeFaultItems_doPostTest() throws IOException, ServletException {
        //given
        RegistrationController controller = new RegistrationController();

        HttpSession session = mock(HttpSession.class);
        UserService localUserService = mock(MySqlUserService.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ServletContext context = mock(ServletContext.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(session.getId()).thenReturn(JSESSIONID);
        when(context.getAttribute(SERV_CXT_USER_SERVICE)).thenReturn(localUserService);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(context);
        when(request.getRequestDispatcher(PAGE_MAIN_VIEW)).thenReturn(dispatcher);

        //when
        controller.doPost(request, response);

        //then
        verify(request, times(1)).getRequestDispatcher(PAGE_MAIN_VIEW);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @Ignore
    public void shouldImitateCaseWhenUserFormDoesntHaveSomeProblem_doPostTest() throws IOException, ServletException {
        //given
        RegistrationController controller = new RegistrationController();

        HttpSession session = mock(HttpSession.class);
        CaptchaService captchaService = mock(SessionCaptchaService.class);
        UserService localUserService = mock(MySqlUserService.class);
        ServletContext context = mock(ServletContext.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DataContainer<String, LocalTime> dataContainer = new DataContainer<>(CAPTCHA_VALUE, LocalTime.now());

        when(captchaService.deriveCaptcha(request)).thenReturn(dataContainer);
        when(session.getId()).thenReturn(JSESSIONID);
        when(context.getAttribute(SERV_CXT_CAPTCHA_SERVICE)).thenReturn(captchaService);
        when(context.getAttribute(SERV_CXT_USER_SERVICE)).thenReturn(localUserService);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(context);

        when(request.getParameter(REQ_PARAM_USER_HIDDEN)).thenReturn(JSESSIONID);
        when(request.getParameter(REQ_PARAM_USER_NAME)).thenReturn("Bob");
        when(request.getParameter(REQ_PARAM_USER_SECOND_NAME)).thenReturn("Martin");
        when(request.getParameter(REQ_PARAM_USER_EMAIL)).thenReturn("bob@gmail.com");
        when(request.getParameter(REQ_PARAM_USER_FIRST_PSW)).thenReturn("1111111");
        when(request.getParameter(REQ_PARAM_USER_SECOND_PSW)).thenReturn("1111111");
        when(request.getParameter(REQ_PARAM_USER_IS_GET_EMAIL)).thenReturn("on");
        when(request.getParameter(REQ_PARAM_USER_CAPTCHA)).thenReturn(CAPTCHA_VALUE);

        //when
        controller.doPost(request, response);

        //then
        verify(response).sendRedirect(PAGE_SUCCESS);
    }

}