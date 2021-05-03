<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="/WEB-INF/tld/custom.tld" %>

<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_ATTR_ERROR_MSG" %>
<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_ATTR_FORM_BEAN" %>
<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_PARAM_USER_NAME" %>
<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_PARAM_USER_SECOND_NAME" %>
<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_PARAM_USER_EMAIL" %>
<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_PARAM_USER_FIRST_PSW" %>
<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_PARAM_USER_SECOND_PSW" %>
<%@ page import="static com.epam.mishchenkov.controller.RegistrationController.REQ_PARAM_USER_CAPTCHA" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.REQ_PARAM_USER_HIDDEN" %>
<c:set var="ERRORS" value="<%=REQ_ATTR_ERROR_MSG%>"/>
<c:set var="FORM_BEAN" value="<%=REQ_ATTR_FORM_BEAN%>"/>
<c:set var="USER_NAME" value="<%=REQ_PARAM_USER_NAME%>"/>
<c:set var="SECOND_NAME" value="<%=REQ_PARAM_USER_SECOND_NAME%>"/>
<c:set var="USER_EMAIL" value="<%=REQ_PARAM_USER_EMAIL%>"/>
<c:set var="FIRST_PSW" value="<%=REQ_PARAM_USER_FIRST_PSW%>"/>
<c:set var="SECOND_PSW" value="<%=REQ_PARAM_USER_SECOND_PSW%>"/>
<c:set var="CAPTCHA" value="<%=REQ_PARAM_USER_CAPTCHA%>"/>
<c:set var="HIDDEN" value="<%=REQ_PARAM_USER_HIDDEN%>"/>

<html>
<head>
    <title>User Registration</title>
    <jsp:include page="template/head.jsp"/>
</head>
<body>
<jsp:include page="template/header.jsp"/>

<section id="form"><!--form-->
    <div class="container">
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <div class="signup-form"><!--sign up form-->
                    <h2>New User Signup!</h2>
                    <form id="form-sing-up" action="registration" enctype="multipart/form-data" method="post">

                        <c:if test="${requestScope.containsKey(ERRORS) && requestScope.get(ERRORS).get(USER_NAME) != null}">
                            <p class="bg-danger-form">${requestScope.get(ERRORS).get(USER_NAME)}</p>
                        </c:if>
                        <input class="f-name" type="text" placeholder="Name" name="name"
                               <c:if test="${requestScope.containsKey(FORM_BEAN)}">value="${requestScope.get(FORM_BEAN).name}"</c:if>/>

                        <c:if test="${requestScope.containsKey(ERRORS) && requestScope.get(ERRORS).get(SECOND_NAME) != null}">
                            <p class="bg-danger-form">${requestScope.get(ERRORS).get(SECOND_NAME)}</p>
                        </c:if>
                        <input class="f-second-name" type="text" placeholder="Second Name" name="second-name"
                               <c:if test="${requestScope.containsKey(FORM_BEAN)}">value="${requestScope.get(FORM_BEAN).secondName}"</c:if>/>

                        <c:if test="${requestScope.containsKey(ERRORS) && requestScope.get(ERRORS).get(USER_EMAIL) != null}">
                            <p class="bg-danger-form">${requestScope.get(ERRORS).get(USER_EMAIL)}</p>
                        </c:if>
                        <input class="f-email" type="email" placeholder="Email Address" name="email"
                               <c:if test="${requestScope.containsKey(FORM_BEAN)}">value="${requestScope.get(FORM_BEAN).mail}"</c:if>/>

                        <c:if test="${requestScope.containsKey(ERRORS) && requestScope.get(ERRORS).get(FIRST_PSW) != null}">
                            <p class="bg-danger-form">${requestScope.get(ERRORS).get(FIRST_PSW)}</p>
                        </c:if>
                        <input class="f-psw" type="password" placeholder="Password" name="psw-1" value=""/>

                        <c:if test="${requestScope.containsKey(ERRORS) && requestScope.get(ERRORS).get(SECOND_PSW) != null}">
                            <p class="bg-danger-form">${requestScope.get(ERRORS).get(SECOND_PSW)}</p>
                        </c:if>
                        <input class="f-psw-2" type="password" placeholder="Password 2" name="psw-2" value=""/>

                        <input type="file" name="file" accept="image/jpeg">

                        <span class="va" style="display: flex">
                            <input type="checkbox" class="checkbox" name="is-email"
                                   <c:if test="${requestScope.containsKey(FORM_BEAN)
                                   && requestScope.get(FORM_BEAN).isSendEmail != null
                                   && requestScope.get(FORM_BEAN).isSendEmail == 'on'}">checked</c:if>>
                            Do you want to get our E-Mails?
                        </span>

                        <c:if test="${requestScope.containsKey(ERRORS) && requestScope.get(ERRORS).get(CAPTCHA) != null}">
                            <p class="bg-danger-form">${requestScope.get(ERRORS).get(CAPTCHA)}</p>
                        </c:if>

                        <ct:captcha-tag value="${requestScope.get(HIDDEN)}"/>

                        <button type="submit" class="btn btn-secondary active" disabled>Signup</button>
                        <%--<button type="submit" class="btn btn-secondary active">Signup</button>--%>
                    </form>
                </div><!--/sign up form-->
            </div>
        </div>
    </div>
</section><!--/form-->

<jsp:include page="template/footer.jsp"/>
<jsp:include page="template/bottom-script.jsp"/>
<jsp:include page="template/custom-bootom-script.jsp"/>

<!--Custom-->
<script>
    $(document).ready(function (){
        let captchaWidth = $("#form-sing-up img").width();
        console.log(captchaWidth);
        $("#form-sing-up .f-captcha").css("width", captchaWidth);
    });
</script>
</body>
</html>
