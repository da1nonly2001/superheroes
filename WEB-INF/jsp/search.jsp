<%--
  Created by IntelliJ IDEA.
  User: catop
  Date: 4/13/15
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

        <c:choose>
            <c:when test="${hits == 0}">
                <div class="row">
                    <div class="col1">&nbsp;</div>
                    <div class="col10">
                        <h2>No Results</h2>
                        <p>Try searching on a different string.  If you do not know the full name of that which you are looking for, but you know a part of it, then you can attempt to search on that part.  For instance, if you are looking for Spiderman but are unsure of how to spell <span class="italic">Spiderman</span>, you can try searching on <span class="italic">Spider</span> or <span class="italic">man</span>.</p>
                    </div>
                    <div class="col1">&nbsp;</div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row">
                    <div class="col12">
                        <h2>Search Results</h2>
                    </div>
                </div>
                <c:if test="${!empty characters}">
                    <div class="row">
                        <div class="col2">&nbsp;</div>
                        <div class="col8">
                            <p>Characters</p>
                            <ul>
                                <c:forEach var="character" items="${characters}">
                                    <li><a href="/superheroes/character?id=${character.id}">${character.name}</a></li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col2">&nbsp;</div>
                    </div>
                </c:if>

                <c:if test="${!empty comics}">
                    <div class="row">
                        <div class="col2">&nbsp;</div>
                        <div class="col8">
                            <p>Comics</p>
                            <ul>
                                <c:forEach var="comic" items="${comics}">
                                    <li><a href="/superheroes/comic?id=${comic.id}">${comic.title}</a></li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col2">&nbsp;</div>
                    </div>
                </c:if>

                <c:if test="${!empty events}">
                    <div class="row">
                        <div class="col2">&nbsp;</div>
                        <div class="col8">
                            <p>Events</p>
                            <ul>
                                <c:forEach var="event" items="${events}">
                                    <li><a href="/superheroes/event?id=${event.id}">${event.title}</a></li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col2">&nbsp;</div>
                    </div>
                </c:if>

                <c:if test="${!empty series}">
                    <div class="row">
                        <div class="col2">&nbsp;</div>
                        <div class="col8">
                            <p>Series</p>
                            <ul>
                                <c:forEach var="seriez" items="${series}">
                                    <li><a href="/superheroes/series?id=${seriez.id}">${seriez.title}</a></li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col2">&nbsp;</div>
                    </div>
                </c:if>

                <c:if test="${!empty stories}">
                    <div class="row">
                        <div class="col2">&nbsp;</div>
                        <div class="col8">
                            <p>Stories</p>
                            <ul>
                                <c:forEach var="story" items="${stories}">
                                    <li><a href="/superheroes/story?id=${story.id}">${story.title}</a></li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col2">&nbsp;</div>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>

        <jsp:include page="jspf/footer.jspf"/>
        <div class="clear">&nbsp;</div>
    </div>

    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="web/resources/scripts/gridlayout.js"></script>
    <script src="web/resources/scripts/superheroes.js"></script>
</body>
</html>