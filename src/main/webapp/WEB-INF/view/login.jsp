<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ct" uri="/WEB-INF/tld/custom.tld" %>
<html>
<head>
    <title>User Login</title>
    <jsp:include page="template/head.jsp"/>
</head>
<body>
<jsp:include page="template/header.jsp"/>

<section id="form"><!--form-->
    <div class="container">
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <h2>Login to your account</h2>
                <ct:login user="${sessionScope.get(CURRENT_USER)}"/>
            </div>
        </div>
    </div>
</section><!--/form-->

<jsp:include page="template/footer.jsp"/>
<jsp:include page="template/bottom-script.jsp"/>
<!--Validation-->
<script src="static/js/jq-validation.js"></script>
</body>
</html>
