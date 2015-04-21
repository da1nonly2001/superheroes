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
<head>
    <title>Hero Library</title>
    <link rel="stylesheet" type="text/css" href="web/resources/styles/gridlayout.css" />
    <link rel="stylesheet" type="text/css" href="web/resources/styles/detect.css" />
</head>
<body>
    <div id="page_wrapper">
        <jsp:include page="jspf/header.jspf"/>

        <jsp:include page="jspf/search.jspf"/>

        <div>&nbsp;</div>

        <div class="row">
            <div class="col1">&nbsp;</div>
            <div class="col10">
                <h2>Oops, something went wrong!</h2>
                <p>The good news is that the error has been logged so it is likely that a dev will investigate the issue.  If you want this issue expedited, then send us an email, please include as much information as possible so a dev can replicate the issue.  Useful information is a step-by-step guide on how you experienced this issue, the URL you accessed, date and time.  Thank you.</p>
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