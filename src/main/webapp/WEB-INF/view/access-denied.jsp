<%@ page import="static com.epam.mishchenkov.constant.AppConstant.SESSION_ATTR_REFERER" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="REF_LOCATION" value="<%=SESSION_ATTR_REFERER%>"/>

<html>
<head>
    <title>Access Page</title>
    <jsp:include page="template/head.jsp"/>
</head>
<body>
<jsp:include page="template/header.jsp"/>

<section id="form"><!--form-->
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h1 align="center">Access denied!</h1>
            </div>
            <div class="col-sm-2">
                <h3>Timeout is: </h3>
            </div>
            <div class="col-sm-1">
                <h3 id="time" class="text-danger"></h3>
            </div>
        </div>
    </div>
</section><!--/form-->

<jsp:include page="template/footer.jsp"/>
<jsp:include page="template/bottom-script.jsp"/>
<script>
    let i = 10;
    function time(){
        document.getElementById("time").innerHTML = i;
        i--;
        if (i < 0) location.href = "${sessionScope.get(REF_LOCATION)}";
    }
    time();
    setInterval(time, 1000);
</script>
</body>
</html>
