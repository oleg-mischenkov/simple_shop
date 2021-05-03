<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>template</title>
    <jsp:include page="WEB-INF/view/template/head.jsp"/>
</head>
<body>
<jsp:include page="WEB-INF/view/template/header.jsp"/>

<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-3">
                <div class="left-sidebar">
                    <jsp:include page="WEB-INF/view/template/menu-category.jsp"/>
                </div>
            </div>

            <div class="col-sm-9 padding-right">
                <div class="features_items"><!--features_items-->

                    Template

                </div><!--features_items-->

            </div>
        </div>
    </div>
</section>

<jsp:include page="WEB-INF/view/template/footer.jsp"/>
<jsp:include page="WEB-INF/view/template/bottom-script.jsp"/>
<jsp:include page="WEB-INF/view/template/custom-bootom-script.jsp"/>
</body>
</html>