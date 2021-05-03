<%@ page import="static com.epam.mishchenkov.controller.CartController.REQ_ATTR_CART_ITEMS" %>
<%@ page import="static com.epam.mishchenkov.controller.CartController.REQ_ATTR_CART_TOTAL_PRICE" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SESSION_ITEM_CURRENT_USER" %>
<%@ page import="static com.epam.mishchenkov.controller.CartController.REQ_ATTR_DELIVERY_LIST" %>
<%@ page import="static com.epam.mishchenkov.controller.CartController.REQ_ATTR_PAYMENT_LIST" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="CART_ITEMS" value="<%=REQ_ATTR_CART_ITEMS%>"/>
<c:set var="TOTAL_PRICE" value="<%=REQ_ATTR_CART_TOTAL_PRICE%>"/>
<c:set var="CURRENT_USER" value="<%=SESSION_ITEM_CURRENT_USER%>"/>
<c:set var="DELIVERY_LIST" value="<%=REQ_ATTR_DELIVERY_LIST%>"/>
<c:set var="PAYMENT_LIST" value="<%=REQ_ATTR_PAYMENT_LIST%>"/>

<html>
<head>
    <title>Cart</title>
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

                <c:if test="${fn:length(requestScope.get(CART_ITEMS)) == 0}">
                    <h2 align="center">The cart is empty :(</h2>
                </c:if>

                <div <c:if test="${fn:length(requestScope.get(CART_ITEMS)) == 0}">style="visibility: hidden" </c:if> class="features_items"><!--features_items-->

                    <table class="table table-striped table-hover">
                        <caption style="text-align: end">
                            <form action="cart" method="POST">
                                <input name="removeAll" type="hidden" value="remove">
                                <button type="submit" class="btn btn-danger" style="margin-top: 0px;"><span class="glyphicon glyphicon-trash"></span></button>
                            </form>
                        </caption>
                        <thead style="background-color: #545453; color: white;">
                            <tr>
                                <th>#</th>      <%--1--%>
                                <th>name</th>   <%--2--%>
                                <th>sku</th>    <%--3--%>
                                <th>count</th>  <%--4--%>
                                <th>price</th>  <%--5--%>
                                <th>total</th>  <%--6--%>
                                <th></th>       <%--7--%>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="element" items="${requestScope.get(CART_ITEMS)}" varStatus="num">
                                <c:set var="prod" value="${element.name}"/>
                                <c:set var="prod_count" value="${element.value}"/>
                                <tr>
                                    <td>${num.index + 1}</td>
                                    <td><b>${prod.title}</b></td>
                                    <td>${prod.sku}</td>
                                    <td>
                                        <form id="count-form" action="cart" method="POST">
                                            <input name="countValue" id="val-${prod.sku}" type="number" class="form-control input-sm" style="max-width:60px;" value="${prod_count}" min="0" />
                                            <input name="countName" type="hidden" value="">
                                        </form>
                                    </td>
                                    <td>$${prod.price}</td>
                                    <td>$<script>document.write( (${prod.price} * ${prod_count}).toFixed(2) )</script></td>
                                    <td>
                                        <form id="remove-form" action="cart" method="POST">
                                            <span id="del-${prod.sku}" style="color:red;" class="glyphicon glyphicon-remove-circle"></span>
                                            <input name="removeElem" type="hidden" value="">
                                        </form>
                                    </td>
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
                                <td></td>
                            </tr>
                        </tfoot>
                    </table>

                    <c:if test="${sessionScope.get(CURRENT_USER) == null}">
                        <div class="alert alert-error" style="background: #ffdcdc;color: #bb4a4a;">
                            <strong>Note!</strong> For order you need login or go to <a href="registration">register page</a>.
                        </div>
                    </c:if>

                    <form id="buy-form" <c:if test="${sessionScope.get(CURRENT_USER) == null}">style="visibility: hidden"</c:if>  action="buy" method="POST">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="deliveryWay">Enter delivery method:</label>
                                    <div class="input-group">
                                        <div class="input-group-addon"><span class="glyphicon glyphicon-plane"></span></div>
                                        <select id="deliveryWay" class="form-control" name="delivery-type">
                                            <c:forEach var="delivery" items="${requestScope.get(DELIVERY_LIST)}">
                                                <option>${delivery.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="paymentWay">Enter payment method:</label>
                                    <div class="input-group">
                                        <div class="input-group-addon"><span class="glyphicon glyphicon-credit-card"></span></div>
                                        <select id="paymentWay" class="form-control" name="payment-type">
                                            <c:forEach var="payment" items="${requestScope.get(PAYMENT_LIST)}">
                                                <option value="${payment.name}"
                                                        <c:if test="${payment.name == 'card'}"
                                                        >selected</c:if>>${payment.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">

                                <div id="cardBlock" class="form-group">
                                    <label for="cardPanel">Enter card number:</label>
                                    <div id="cardPanel" class="panel panel-default">
                                        <div class="panel-heading">Credit card</div>
                                        <div class="panel-body">
                                            <input id="ccn" name="creditCard" class="form-control" type="tel" pattern="[\d]{4}-[\d]{4}-[\d]{4}-[\d]{4}"
                                                   minlength="19" maxlength="19" placeholder="XXXX-XXXX-XXXX-XXXX">

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <c:if test="${fn:length(requestScope.get(CART_ITEMS)) > 0}">
                            <button disabled type="submit" class="btn btn-success">Buy</button>
                        </c:if>

                    </form>

                </div><!--features_items-->

            </div>
        </div>
    </div>
</section>

<jsp:include page="template/footer.jsp"/>
<jsp:include page="template/bottom-script.jsp"/>
<jsp:include page="template/custom-bootom-script.jsp"/>
<script>
    $(document).ready(function (){
        // changing product count
        $('#count-form input').on('change', function () {
            $(this).next().val( $(this).attr('id').replace('val-', '') );
            $(this).parent().submit();
        });

        // remove product
        $('#remove-form span').on('click', function () {
            $(this).next().val( $(this).attr('id').replace('del-', '') );
            $(this).parent().submit();
        });

        // pay selection
        $('#paymentWay').on('change', (function() {
            $(this).children(':selected').trigger('click');
        }));

        $("#paymentWay option").click(function() {
            let payValue = $(this).attr("value");

            let cardBlock = $("#cardBlock");

            if ("card" == payValue) {
                $('#buy-form button').prop('disabled', true);
                cardBlock.show("slow");
            } else {
                cardBlock.hide("slow");
                $('#buy-form button').prop('disabled', false);
            }
        });

        $('#ccn').on('keyup', function(){
            let payStatus = $(this).val();
            if (/[\d]{4}-[\d]{4}-[\d]{4}-[\d]{4}/.test(payStatus)) {
                $('#buy-form button').prop('disabled', false);
            }
        });

    });
</script>
</body>
</html>
