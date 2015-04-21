<%--
  Created by IntelliJ IDEA.
  User: catop
  Date: 4/13/15
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <div>&nbsp;</div>

        <div class="row">
            <c:choose>
                <c:when test="${!empty character.thumbnail && !empty character.description}">
                    <div class="col1">&nbsp;</div>
                    <div class="col5">
                        <h2>${character.name}</h2>
                        <p>${character.description}</p>
                    </div>
                    <div class="col5 thumbnail">
                        <img src="${character.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:when>
                <c:otherwise>
                    <div class="col1">&nbsp;</div>
                    <div class="col10">
                        <h2>${character.name}</h2>
                        <c:if test="${!empty character.description}">
                            <p>${character.description}</p>
                        </c:if>
                        <c:if test="${!empty character.thumbnail}">
                            <div class="full-thumbnail">
                                <img src="${character.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                            </div>
                        </c:if>
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${!empty character.comics}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p><span class="bold">${character.name}</span> stars in these comics:</p>
                    <table class="table" id="comic">
                        <thead>
                            <th>Comic Title</th>
                            <th>Comic Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="comic" items="${character.comics}">
                            <tr>
                                <td><a href="/superheroes/comic?id=${comic.id}">${comic.title}</a></td>
                                <td><div>${comic.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(character.comics) gt 10}">
                        <div id="comic-pager" class="pager tablesorter">
                            <div>
                                <img src="web/resources/media/first.png" class="first pointer"/>
                                <img src="web/resources/media/prev.png" class="prev pointer"/>
                                <input type="text" class="pagedisplay"/>
                                <img src="web/resources/media/next.png" class="next pointer"/>
                                <img src="web/resources/media/last.png" class="last pointer" />
                                <select class="pagesize" >
                                    <option selected="selected" value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <c:if test="${!empty character.stories}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p><span class="bold">${character.name}</span> plays a role in these stories:</p>
                    <table class="table" id="story">
                        <thead>
                            <th>Story Title</th>
                            <th>Story Description</th>
                        </thead>
                        <tbody>
                            <c:forEach var="story" items="${character.stories}">
                                <c:if test="${!empty story.title}">
                                    <tr>
                                        <td><a href="/superheroes/story?id=${story.id}">${story.title}</a></td>
                                        <td><div>${story.description}</div></td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(character.stories) gt 10}">
                        <div id="story-pager" class="pager tablesorter">
                            <div>
                                <img src="web/resources/media/first.png" class="first pointer"/>
                                <img src="web/resources/media/prev.png" class="prev pointer"/>
                                <input type="text" class="pagedisplay"/>
                                <img src="web/resources/media/next.png" class="next pointer"/>
                                <img src="web/resources/media/last.png" class="last pointer" />
                                <select class="pagesize" >
                                    <option selected="selected" value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <c:if test="${!empty character.series}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p><span class="bold">${character.name}</span> participates in these series:</p>
                    <table class="table" id="series">
                        <thead>
                            <th>Series Title</th>
                            <th>Series Description</th>
                        </thead>
                        <tbody>
                            <c:forEach var="series" items="${character.series}">
                                <tr>
                                    <td><a href="/superheroes/series?id=${series.id}">${series.title}</a></td>
                                    <td><div>${series.description}</div></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(character.series) gt 10}">
                        <div id="series-pager" class="pager tablesorter">
                            <div>
                                <img src="web/resources/media/first.png" class="first pointer"/>
                                <img src="web/resources/media/prev.png" class="prev pointer"/>
                                <input type="text" class="pagedisplay"/>
                                <img src="web/resources/media/next.png" class="next pointer"/>
                                <img src="web/resources/media/last.png" class="last pointer" />
                                <select class="pagesize" >
                                    <option selected="selected" value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <c:if test="${!empty character.events}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p><span class="bold">${character.name}</span> has been involved with the following events:</p>
                    <table class="table" id="events">
                        <thead>
                            <th>Event Title</th>
                            <th>Event Description</th>
                        </thead>
                        <tbody>
                            <c:forEach var="event" items="${character.events}">
                                <tr>
                                    <td><a href="/superheroes/event?id=${event.id}">${event.title}</a></td>
                                    <td><div>${event.description}</div></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(character.events) gt 10}">
                        <div id="events-pager" class="pager tablesorter">
                            <div>
                                <img src="web/resources/media/first.png" class="first pointer"/>
                                <img src="web/resources/media/prev.png" class="prev pointer"/>
                                <input type="text" class="pagedisplay"/>
                                <img src="web/resources/media/next.png" class="next pointer"/>
                                <img src="web/resources/media/last.png" class="last pointer" />
                                <select class="pagesize" >
                                    <option selected="selected" value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <c:if test="${!empty character.urls}">
            <div class="row">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Follow these official links to read more about <span class="bold">${character.name}</span>!</p>
                        <c:forEach var="url" items="${character.urls}">
                            <li><a href="${url.url}" target="_blank">${url.type}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <jsp:include page="jspf/footer.jspf"/>

    </div>
    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="web/resources/scripts/gridlayout.js"></script>
    <script src="web/resources/scripts/jquery.tablesorter.js"></script>
    <script src="web/resources/scripts/jquery.tablesorter.pager.js"></script>
    <script src="web/resources/scripts/superheroes.js"></script>
</body>
</html>