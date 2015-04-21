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
        <div>&nbsp;</div>

        <div class="row">
            <c:choose>
                <c:when test="${!empty event.thumbnail && !empty event.description}">
                    <div class="col1">&nbsp;</div>
                    <div class="col5">
                        <h2>${event.title}</h2>
                        <p>${event.description}</p>
                        <c:if test="${!empty event.start && !empty event.end}">
                            <p><span>${event.title}</span> last from <fmt:formatDate type="date" value="${event.start}" /> to <fmt:formatDate value="${event.end}" type="date"/>.</p>
                        </c:if>
                    </div>
                    <div class="col5 thumbnail">
                        <img src="${event.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:when>
                <c:otherwise>
                    <div class="col1">&nbsp;</div>
                    <div class="col10">
                        <h2>${event.title}</h2>
                        <c:if test="${!empty event.description}">
                            <p>${event.description}</p>
                        </c:if>
                        <c:if test="${!empty event.start && !empty event.end}">
                            <p><span>${event.title}</span> last from ${event.start} to ${event.end}.</p>
                        </c:if>
                        <c:if test="${!empty event.thumbnail}">
                            <div class="full-thumbnail">
                                <img src="${event.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                            </div>
                        </c:if>
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${!empty event.characters}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Characters that participate in this event:</p>
                    <table class="table" id="characters">
                        <thead>
                        <th>Character Name</th>
                        <th>Character Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="character" items="${event.characters}">
                            <tr>
                                <td><a href="/superheroes/character?id=${character.id}">${character.name}</a></td>
                                <td><div>${character.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(event.characters) gt 10}">
                        <div id="characters-pager" class="pager tablesorter">
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

        <c:if test="${!empty event.stories}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>The following stories are portrayed in this event:</p>
                    <table class="table" id="story">
                        <thead>
                        <th>Story Title</th>
                        <th>Story Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="story" items="${event.stories}">
                            <c:if test="${!empty story.title}">
                                <tr>
                                    <td><a href="/superheroes/story?id=${story.id}">${story.title}</a></td>
                                    <td><div>${story.description}</div></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(event.stories) gt 10}">
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
                                    <option  value="50">50</option>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <c:if test="${!empty event.series}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This event can be found in these series:</p>
                    <table class="table" id="series">
                        <thead>
                        <th>Series Title</th>
                        <th>Series Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="series" items="${event.series}">
                            <tr>
                                <td><a href="/superheroes/series?id=${series.id}">${series.title}</a></td>
                                <td><div>${series.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(event.series) gt 10}">
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

        <c:if test="${!empty event.creators}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This event was created by:</p>
                    <ul>
                        <c:forEach var="creator" items="${event.creators}" varStatus="status">
                            <c:if test="${status.index == 5}">
                                <li><a class="js-creators-more pointer">CLICK TO LOAD MORE CREATORS</a></li>
                            </c:if>
                            <li class="<c:if test="${status.index >= 5}">hide</c:if>"><a href="/superheroes/creator?id=${creator.id}">${creator.fullName}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <c:if test="${!empty event.urls}">
            <div class="row">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Follow these official links to read more about this event:</p>
                    <ul>
                        <c:forEach var="url" items="${event.urls}">
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