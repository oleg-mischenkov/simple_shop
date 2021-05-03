<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="/WEB-INF/tld/custom.tld" %>

<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SESSION_ITEM_CURRENT_USER" %>
<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SERV_CXT_INIT_SERVER_PATH" %>
<%@ page import="static com.epam.mishchenkov.controller.CategoryController.REQ_ATTR_MANUFACTURES" %>
<%@ page import="static com.epam.mishchenkov.controller.CategoryController.REQ_ATTR_PRODUCT_TYPES" %>
<%@ page import="static com.epam.mishchenkov.controller.CategoryController.REQ_ATTR_MOST_HEIGHT_PROD_PRICE" %>
<%@ page import="static com.epam.mishchenkov.controller.CategoryController.REQ_ATTR_CURRENT_FORM_FILTER" %>

<c:set var="CURRENT_USER" value="<%=SESSION_ITEM_CURRENT_USER%>"/>
<c:set var="SERVER_PATH" value="<%=SERV_CXT_INIT_SERVER_PATH%>"/>
<c:set var="MANUFACTURES" value="<%=REQ_ATTR_MANUFACTURES%>"/>
<c:set var="PRODUCT_TYPES" value="<%=REQ_ATTR_PRODUCT_TYPES%>"/>
<c:set var="PROD_PRICE" value="<%=REQ_ATTR_MOST_HEIGHT_PROD_PRICE%>"/>
<c:set var="CURRENT_FORM_FILTER" value="<%=REQ_ATTR_CURRENT_FORM_FILTER%>"/>

<h2>Product filter</h2>
<div class="panel-group">

    <form action="category">
        <div class="row">
            <div class="form-group col-lg-6 col-md-6">
                <label for="pce-from">Price from:</label>
                <input type="number" class="form-control" id="pce-from" name="price-from"
                        <c:choose>
                            <c:when test="${requestScope.get(CURRENT_FORM_FILTER) == null}">
                                value="0"
                            </c:when>
                            <c:otherwise>
                                <c:if test="${requestScope.get(CURRENT_FORM_FILTER).minPrice == null}">
                                    value="0"
                                </c:if>
                                <c:if test="${requestScope.get(CURRENT_FORM_FILTER).minPrice != null}">
                                    value="${requestScope.get(CURRENT_FORM_FILTER).minPrice}"
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                />
            </div>
            <div class="form-group col-lg-6 col-md-6">
                <label for="pce-to">Price to:</label>
                <input type="number" class="form-control" id="pce-to" name="price-to"
                       <c:choose>
                           <c:when test="${requestScope.get(CURRENT_FORM_FILTER) == null}">
                               value="${requestScope.get(PROD_PRICE)}"
                           </c:when>
                           <c:otherwise>
                               <c:if test="${requestScope.get(CURRENT_FORM_FILTER).maxPrice == null}">
                                   value="${requestScope.get(PROD_PRICE)}"
                               </c:if>
                               <c:if test="${requestScope.get(CURRENT_FORM_FILTER).maxPrice != null}">
                                   value="${requestScope.get(CURRENT_FORM_FILTER).maxPrice}"
                               </c:if>
                           </c:otherwise>
                       </c:choose>
                />
            </div>

            <div class="form-group col-lg-12 col-md-12">
                <label for="name">Product name:</label>
                <input type="text" class="form-control" id="name" name="prod-name"
                        <c:choose>
                            <c:when test="${requestScope.get(CURRENT_FORM_FILTER) == null}">
                                value=""
                            </c:when>
                            <c:otherwise>
                                <c:if test="${requestScope.get(CURRENT_FORM_FILTER).productTitle == null}">
                                    value=""
                                </c:if>
                                <c:if test="${requestScope.get(CURRENT_FORM_FILTER).productTitle != null}">
                                    value="${requestScope.get(CURRENT_FORM_FILTER).productTitle}"
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                />
            </div>

            <div class="form-group col-lg-12 col-md-12">
                <label for="manufacture">Manufacturer:</label>
                <select multiple class="form-control" id="manufacture" name="prod-manufacture">
                    <c:forEach var="element" items="${requestScope.get(MANUFACTURES)}">
                        <option <c:if test="${requestScope.get(CURRENT_FORM_FILTER).manufacture == element.name}">selected</c:if>>${element.name}</option>
                    </c:forEach>
                </select>
                <c:if test="${requestScope.get(CURRENT_FORM_FILTER).manufacture != null}">
                    <button type="button" style="margin-top: 5px" class="btn btn-info btn-xs" onclick="$('#manufacture option').prop('selected', false)">remove selection</button>
                </c:if>
            </div>

            <div class="form-group col-lg-12 col-md-12">
                <label for="type">Product type:</label>
                <select multiple class="form-control" id="type" name="prod-type">
                    <c:forEach var="element" items="${requestScope.get(PRODUCT_TYPES)}">
                        <option <c:if test="${requestScope.get(CURRENT_FORM_FILTER).productType == element.name}">selected</c:if>>${element.name}</option>
                    </c:forEach>
                </select>
                <c:if test="${requestScope.get(CURRENT_FORM_FILTER).productType != null}">
                    <button type="button" style="margin-top: 5px" class="btn btn-info btn-xs" onclick="$('#type option').prop('selected', false)">remove selection</button>
                </c:if>
            </div>

            <div class="col-lg-12 col-md-12">
                <button type="submit" class="btn btn-primary">Apply</button>
            </div>
        </div>

    </form>

</div>

<h2>User Box</h2>
<div class="panel-group">

    <ct:login user="${sessionScope.get(CURRENT_USER)}"/>

</div>