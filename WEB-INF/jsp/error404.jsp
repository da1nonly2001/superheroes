<%--
  Created by IntelliJ IDEA.
  User: catop
  Date: 4/13/15
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<jsp:include page="jspf/head.jspf"/>
<body>
    <div id="page_wrapper">
        <jsp:include page="jspf/header.jspf"/>

        <div>&nbsp;</div>

        <div class="row">
            <div class="col1">&nbsp;</div>
            <div class="col10">
                <h2>Oops, that page doesn't exist!</h2>
                <p>Click <a href="/superheroes/">here</a> to navigate to the cover page where you will find more options.  Additional options can be found in the footer of this page.</p>
            </div>
            <div class="col1">&nbsp;</div>
        </div>

        <jsp:include page="jspf/footer.jspf"/>

    </div>

    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="web/resources/scripts/gridlayout.js"></script>
    <script src="web/resources/scripts/superheroes.js"></script>
</body>
</html>