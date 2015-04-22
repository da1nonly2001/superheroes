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
<jsp:include page="jspf/head.jspf"/>
<body>
    <div id="page_wrapper">
        <jsp:include page="jspf/header.jspf"/>
        <div>&nbsp;</div>

        <div class="row">
            <c:choose>
                <c:when test="${!empty series.thumbnail && !empty series.description}">
                    <div class="col1">&nbsp;</div>
                    <div class="col5">
                        <h2>${series.title}</h2>
                        <p>${series.description}</p>
                        <c:if test="${!empty series.startYear && !empty series.endYear}">
                            <p><span class="bold">${series.title}</span> last from <span class="bold"><fmt:formatDate value="${series.startYear}" pattern="yyyy"/></span> to <span class="bold"><fmt:formatDate value="${series.endYear}" pattern="yyyy"/></span>.</p>
                        </c:if>
                    </div>
                    <div class="col5 thumbnail">
                        <img src="${series.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:when>
                <c:otherwise>
                    <div class="col1">&nbsp;</div>
                    <div class="col10">
                        <h2>${series.title}</h2>
                        <c:if test="${!empty series.thumbnail}">
                            <div class="full-thumbnail">
                                <img src="${series.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                            </div>
                        </c:if>
                        <c:if test="${!empty series.description}">
                            <p>${series.description}</p>
                        </c:if>
                        <c:if test="${!empty series.startYear && !empty series.endYear}">
                            <p><span class="bold">${series.title}</span> last from <span class="bold"><fmt:formatDate value="${series.startYear}" pattern="yyyy"/></span> to <span class="bold"><fmt:formatDate value="${series.endYear}" pattern="yyyy"/></span>.</p>
                        </c:if>
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${!empty series.characters}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Characters that participate in this series:</p>
                    <table class="table" id="characters">
                        <thead>
                        <th>Character Name</th>
                        <th>Character Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="character" items="${series.characters}">
                            <tr>
                                <td><a href="/superheroes/character?id=${character.id}">${character.name}</a></td>
                                <td><div>${character.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(series.characters) gt 10}">
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

        <c:if test="${!empty series.stories}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>The following stories are portrayed in this series:</p>
                    <table class="table" id="story">
                        <thead>
                        <th>Story Title</th>
                        <th>Story Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="story" items="${series.stories}">
                            <c:if test="${!empty story.title}">
                                <tr>
                                    <td><a href="/superheroes/story?id=${story.id}">${story.title}</a></td>
                                    <td><div>${story.description}</div></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(series.stories) gt 10}">
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

        <c:if test="${!empty series.comics}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Comics that are part of this series:</p>
                    <table class="table" id="comic">
                        <thead>
                        <th>Comic Title</th>
                        <th>Comic Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="comic" items="${series.comics}">
                            <tr>
                                <td><a href="/superheroes/comic?id=${comic.id}">${comic.title}</a></td>
                                <td><div>${comic.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(series.comics) gt 10}">
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

        <c:if test="${!empty series.events}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p><span class="bold">Events that are part of this series:</p>
                    <table class="table" id="events">
                        <thead>
                        <th>Event Title</th>
                        <th>Event Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="event" items="${series.events}">
                            <tr>
                                <td><a href="/superheroes/event?id=${event.id}">${event.title}</a></td>
                                <td><div>${event.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(series.events) gt 10}">
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

        <c:if test="${!empty series.creators}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This series was created by:</p>
                    <ul>
                        <c:forEach var="creator" items="${series.creators}" varStatus="status">
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

        <c:if test="${!empty series.urls}">
            <div class="row">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Follow these official links to read more about this series:</p>
                    <ul>
                        <c:forEach var="url" items="${series.urls}">
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