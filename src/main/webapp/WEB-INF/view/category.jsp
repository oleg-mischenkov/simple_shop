<%@ page import="static com.epam.mishchenkov.controller.CategoryController.REQ_ATTR_PRODUCT_LIST" %>
<%@ page import="static com.epam.mishchenkov.controller.CategoryController.REQ_ATTR_PAGINATION_RANGE" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SERV_CXT_AVAILABLE_PAGINATION_RANGE" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SESSION_ATTR_CURRENT_PAGINATION_COUNT" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SERV_CXT_PRODUCT_SORT_VALUES" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SESSION_ATTR_CURRENT_SORT_VALUE" %>
<%@ page import="static com.epam.mishchenkov.controller.CategoryController.REQ_ATTR_PAGINATION_POSITION" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="/WEB-INF/tld/custom.tld" %>

<c:set var="PRODUCT_LIST" value="<%=REQ_ATTR_PRODUCT_LIST%>"/>
<c:set var="PAGINATION_RANGE" value="<%=REQ_ATTR_PAGINATION_RANGE%>"/>
<c:set var="REQUEST" value="<%=request.getQueryString()%>"/>
<c:set var="AVAILABLE_PAGINATION" value="<%=SERV_CXT_AVAILABLE_PAGINATION_RANGE%>"/>
<c:set var="SORT_VALUES" value="<%=SERV_CXT_PRODUCT_SORT_VALUES%>"/>
<c:set var="SESSION_PAGINATION_COUNT" value="<%=SESSION_ATTR_CURRENT_PAGINATION_COUNT%>"/>
<c:set var="SESSION_SORT_VALUE" value="<%=SESSION_ATTR_CURRENT_SORT_VALUE%>"/>
<c:set var="PAGINATION_POSITION" value="<%=REQ_ATTR_PAGINATION_POSITION%>"/>

<html>
<head>
    <title>Product category</title>
    <jsp:include page="template/head.jsp"/>
</head>
<body>
<jsp:include page="template/header.jsp"/>

<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-3">
                <div class="left-sidebar">
                    <jsp:include page="template/menu-product-filter.jsp"/>
                </div>
            </div>

            <div class="col-sm-9 padding-right">
                <div id="item-count" class="row">
                    <div class="col-md-2 col-lg-2 col-md-offset-8">
                        sort by
                        <select class="form-control input-sm">
                            <c:forEach var="option" items="${applicationScope.get(SORT_VALUES)}">
                                <option value="sort-by=${option}" <c:if test="${sessionScope.get(SESSION_SORT_VALUE) == option }">selected</c:if>>${option}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-2 col-lg-2">
                        page size
                        <select class="form-control input-sm">
                            <c:forEach var="option" items="${applicationScope.get(AVAILABLE_PAGINATION)}">
                                <option value="count=${option}" <c:if test="${sessionScope.get(SESSION_PAGINATION_COUNT) == (option + '')}">selected</c:if>>${option}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="features_items" style="margin-top: 25px;"><!--features_items-->
                    <div style="text-align: center">
                        <ct:prodPagination url="${REQUEST}" page="${requestScope.get(PAGINATION_RANGE)}"
                                           prefix="category" position="${requestScope.get(PAGINATION_POSITION)}"/>
                    </div>

                    <table class="table">
                        <c:forEach var="element" items="${requestScope.get(PRODUCT_LIST)}">
                            <tr>
                                <td><b>${element.product.title}</b></td>
                                <td>sku: ${element.product.sku}</td>
                                <td><span>${element.product.productType.name}<br/>${element.product.manufacture.name}</span></td>
                                <td>$ ${element.product.price}</td>
                                <td><button id="prod-${element.key}" type="button" class="btn btn-success">add to cart</button></td>
                            </tr>
                        </c:forEach>
                    </table>

                    <div style="text-align: center">
                        <ct:prodPagination url="${REQUEST}" page="${requestScope.get(PAGINATION_RANGE)}"
                                           prefix="category" position="${requestScope.get(PAGINATION_POSITION)}"/>
                    </div>

                </div><!--features_items-->
            </div>
        </div>
    </div>
</section>

<jsp:include page="template/footer.jsp"/>
<jsp:include page="template/bottom-script.jsp"/>
<jsp:include page="template/custom-bootom-script.jsp"/>

<script type="text/javascript">
    $(document).ready(function(){

        // Filter block
        $("#item-count select").on("change", (function() {
            $(this).children(":selected").trigger("click");
        }));

        $("#item-count select option").click(function(){
            let currentOptionValue = $(this).attr("value");
            let pathName = $(location).attr('href');

            console.log(currentOptionValue);

            let findCountField = pathName.indexOf("count=") == -1
                ? pathName.length
                : pathName.indexOf("count=") - 1;
            let findSortField = pathName.indexOf("sort-by=") == -1
                ? pathName.length
                : pathName.indexOf("sort-by=") - 1;

            var findMin = findCountField > findSortField ? findSortField : findCountField;

            if ( pathName.length > findMin ) {
                pathName = pathName.slice(0, findMin);
            }

            let finalUrl = pathName;
            if(finalUrl.includes('?')) {
                finalUrl += '&' + currentOptionValue;
            } else {
                finalUrl += '?' + currentOptionValue;
            }

            console.log(finalUrl);

            window.location.href = finalUrl;
        });

        // Ajax block
        $('.table button').click(function(){
            let prodId = ($(this).attr('id')).replace('prod-', '');

            $.ajax({
                url: 'prod-to-cart',
                type: 'POST',
                data: {'prod-id': prodId},
                dataType: 'html'
            }).done(function(value){
                $('#cart_ico a .badge').remove();
                $('#cart_ico a').append('<span class="badge" style="margin-left: 5px; background-color: #ff4040;">'+ value +'</span>');
            });
        });
    });
</script>

</body>
</html>
