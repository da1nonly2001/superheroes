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

        <jsp:include page="jspf/search.jspf"/>

        <div>&nbsp;</div>

        <div class="row js-height">
            <div class="col2">&nbsp;</div>
            <div class="col4 border">
                <h3>Popular Heroes</h3>
                <ul>
                    <c:forEach items="${characters}" var="character">
                        <li>
                            <a href="/superheroes/character?id=${character.id}">${character.name}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <div class="col4 border">
                <h3>Popular Stories</h3>
                <ul>
                    <c:forEach items="${stories}" var="story">
                        <li>
                            <a href="/superheroes/story?id=${story.id}">${story.title}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="col2">&nbsp;</div>
        </div>

        <div class="row js-height">
            <div class="col2">&nbsp;</div>
            <div class="col4 border">
                <h3>Popular Comics</h3>
                <ul>
                    <c:forEach items="${comics}" var="comic">
                        <li>
                            <a href="/superheroes/comic?id=${comic.id}">${comic.title}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <div class="col4 border">
                <h3>Popular Events</h3>
                <ul>
                    <c:forEach items="${events}" var="event">
                        <li>
                            <a href="/superheroes/event?id=${event.id}">${event.title}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="col2">&nbsp;</div>
        </div>

        <jsp:include page="jspf/footer.jspf"/>

    </div>

    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="web/resources/scripts/gridlayout.js"></script>
    <script src="web/resources/scripts/superheroes.js"></script>
</body>
</html>