<%@ page import="static com.epam.mishchenkov.controller.OrderSuccessController.REQ_ATTR_ORDER" %>
<%@ page import="static com.epam.mishchenkov.controller.OrderSuccessController.REQ_ATTR_ORDER_TOTAL_PRICE" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ORDER" value="<%=REQ_ATTR_ORDER%>"/>
<c:set var="TOTAL_PRICE" value="<%=REQ_ATTR_ORDER_TOTAL_PRICE%>"/>

<html>
<head>
    <title>Success</title>
    <jsp:include page="template/head.jsp"/>
</head>
<body>
<jsp:include page="template/header.jsp"/>

<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-3">
                <div class="left-sidebar">
                    <jsp:include page="template/menu-category.jsp"/>
                </div>
            </div>

            <div class="col-sm-9 padding-right">
                <div <c:if test="${requestScope.get(ORDER) == null}">style="visibility: hidden"</c:if>  class="features_items"><!--features_items-->

                    <div class="row">
                        <div class="col-md-6">
                            <h2>Order details: </h2>
                        </div>
                        <div class="col-md-6" style="text-align: end;">
                            <h2><span class="label label-default">
                                <fmt:formatDate type="both" value="${requestScope.get(ORDER).orderDate}" />
                            </span></h2>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            Name:
                        </div>
                        <div class="col-md-10">
                            ${requestScope.get(ORDER).user.name}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            Second name:
                        </div>
                        <div class="col-md-10">
                            ${requestScope.get(ORDER).user.secondName}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            E-mail:
                        </div>
                        <div class="col-md-10">
                            ${requestScope.get(ORDER).user.email}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            Delivery:
                        </div>
                        <div class="col-md-10">
                            ${requestScope.get(ORDER).delivery.name}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            Payment:
                        </div>
                        <div class="col-md-10">
                            ${requestScope.get(ORDER).payment.name}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                            Order status:
                        </div>
                        <div class="col-md-10">
                            ${requestScope.get(ORDER).status.name}
                        </div>
                    </div>
                    <c:if test="${requestScope.get(ORDER).statusDescription != null}">
                        <div class="row">
                            <div class="col-md-2">
                                Status description:
                            </div>
                            <div class="col-md-10">
                                    ${requestScope.get(ORDER).statusDescription}
                            </div>
                        </div>
                    </c:if>

                    <div class="row">
                        <div class="col-md-12">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>#</th>      <%--1--%>
                                        <th>name</th>   <%--2--%>
                                        <th>sku</th>    <%--3--%>
                                        <th>count</th>  <%--4--%>
                                        <th>price</th>  <%--5--%>
                                        <th>total</th>  <%--6--%>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="element" items="${requestScope.get(ORDER).productList.all}" varStatus="num">
                                    <tr>
                                        <td>${num.index + 1}</td>
                                        <td>${element.name.title}</td>
                                        <td>${element.name.sku}</td>
                                        <td>${element.value}</td>
                                        <td>$${element.name.price}</td>
                                        <td>$<script>document.write( (${element.name.price} * ${element.value}).toFixed(2) )</script></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                                <tfoot>
                                <tr class="warning">
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td>Amount:</td>
                                    <td>$${requestScope.get(TOTAL_PRICE)}</td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>

                </div><!--features_items-->

            </div>
        </div>
    </div>
</section>

<jsp:include page="template/footer.jsp"/>
<jsp:include page="template/bottom-script.jsp"/>
<jsp:include page="template/custom-bootom-script.jsp"/>
</body>
</html>
