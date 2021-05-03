<%@ page import="static com.epam.mishchenkov.constant.AppConstant.REQ_ATTR_LANGUAGE_LIST" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SESSION_ITEM_CURRENT_USER" %>

<%@ page contentType="text/html;charset=UTF-16" language="java"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ct" uri="/WEB-INF/tld/custom.tld" %>

<c:set var="lang" value="<%=request.getLocale()%>"/>
<c:set var="LANGUAGE_LIST" value="<%=REQ_ATTR_LANGUAGE_LIST%>"/>
<c:set var="CURRENT_USER" value="<%=SESSION_ITEM_CURRENT_USER%>"/>

<fmt:setLocale value="${lang}" />

<header id="header"><!--header-->
    <div class="header_top"><!--header_top-->
        <div class="container">
            <div class="row">
                <div class="col-sm-6">

                </div>
                <div class="col-sm-6">
                    <div class="social-icons pull-right">
                        <ul class="nav navbar-nav">
                            <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                            <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                            <li><a href="#"><i class="fa fa-linkedin"></i></a></li>
                            <li><a href="#"><i class="fa fa-dribbble"></i></a></li>
                            <li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div><!--/header_top-->

    <div class="header-middle"><!--header-middle-->
        <div class="container">
            <div class="row">
                <div class="col-sm-4">
                    <div class="logo pull-left">
                        <a href="index.jsp"><img src="static/images/home/logo.png" alt="" /></a>
                    </div>
                    <div class="btn-group pull-left">
                        <ct:lang language="${lang}" langList="${requestScope.get(LANGUAGE_LIST)}"/>
                    </div>
                </div>
                
                <fmt:bundle basename="language-pack" prefix="main.">
                    <div class="col-sm-8">
                        <div class="shop-menu pull-right">
                            <ul class="nav navbar-nav">
                                <li><a href="account"><i class="fa fa-user"></i> <fmt:message key="menu_item_account"/></a></li>
                                <li><a href="#"><i class="fa fa-star"></i> <fmt:message key="menu_item_wish"/></a></li>
                                <li><a href="checkout.html"><i class="fa fa-crosshairs"></i> <fmt:message key="menu_item_checkout"/></a></li>
                                <li id="cart_ico"><a href="cart"><i class="fa fa-shopping-cart"></i> <fmt:message key="menu_item_cart"/></a></li>
                                <c:if test="${sessionScope.get(CURRENT_USER) == null}">
                                    <li><a href="login.jsp"><i class="fa fa-lock"></i> <fmt:message key="menu_item_login"/></a></li>
                                </c:if>
                                <c:if test="${sessionScope.get(CURRENT_USER) != null}">
                                    <li><a href="logout"><i class="fa fa-unlock"></i> <fmt:message key="menu_item_logout"/></a></li>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </fmt:bundle>

            </div>
        </div>
    </div><!--/header-middle-->

    <jsp:include page="header-bottom-menu.jsp" />
</header><!--/header-->
