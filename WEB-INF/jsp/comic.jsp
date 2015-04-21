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

        <div class="row">
            <c:choose>
                <c:when test="${!empty comic.thumbnail && !empty comic.description}">
                    <div class="col1">&nbsp;</div>
                    <div class="col5">
                        <h2>${comic.title}</h2>
                        <p>${comic.description}</p>
                        <table class="info-table">
                            <tbody>
                            <c:if test="${!empty comic.issueNumber}">
                                <tr>
                                    <td>Issue #:</td>
                                    <td>${comic.issueNumber}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty comic.pageCount}">
                                <tr>
                                    <td>Pages:</td>
                                    <td>${comic.pageCount}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty comic.format}">
                                <tr>
                                    <td>Format:</td>
                                    <td>${comic.format}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty comic.onSaleDate}">
                                <tr>
                                    <td>Available since:</td>
                                    <td>${comic.onSaleDate}</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="col5 thumbnail">
                        <img src="${comic.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:when>
                <c:otherwise>
                    <div class="col1">&nbsp;</div>
                    <div class="col10">
                        <h2>${comic.title}</h2>
                        <c:if test="${!empty comic.description}">
                            <p>${comic.description}</p>
                        </c:if>
                        <c:if test="${!empty comic.thumbnail}">
                            <div class="full-thumbnail">
                                <img src="${comic.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                            </div>
                        </c:if>
                        <table class="info-table">
                            <tbody>
                            <c:if test="${!empty comic.issueNumber}">
                                <tr>
                                    <td>Issue #:</td>
                                    <td>${comic.issueNumber}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty comic.pageCount}">
                                <tr>
                                    <td>Pages:</td>
                                    <td>${comic.pageCount}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty comic.format}">
                                <tr>
                                    <td>Format:</td>
                                    <td>${comic.format}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty comic.onSaleDate}">
                                <tr>
                                    <td>Available since:</td>
                                    <td>${comic.onSaleDate}</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${!empty comic.characters}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Characters that participate in this comic:</p>
                    <table class="table" id="characters">
                        <thead>
                            <th>Character Name</th>
                            <th>Character Description</th>
                        </thead>
                        <tbody>
                            <c:forEach var="character" items="${comic.characters}">
                                <tr>
                                    <td><a href="/superheroes/character?id=${character.id}">${character.name}</a></td>
                                    <td><div>${character.description}</div></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(comic.characters) gt 10}">
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

        <c:if test="${!empty comic.stories}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>The following stories are portrayed in this comic:</p>
                    <table class="table" id="story">
                        <thead>
                            <th>Story Title</th>
                            <th>Story Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="story" items="${comic.stories}">
                            <c:if test="${!empty story.title}">
                                <tr>
                                    <td><a href="/superheroes/story?id=${story.id}">${story.title}</a></td>
                                    <td><div>${story.description}</div></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(comic.stories) gt 10}">
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

        <c:if test="${!empty comic.series}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This comic can be found in these series:</p>
                    <table class="table" id="series">
                        <thead>
                        <th>Series Title</th>
                        <th>Series Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="series" items="${comic.series}">
                            <tr>
                                <td><a href="/superheroes/series?id=${series.id}">${series.title}</a></td>
                                <td><div>${series.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(comic.series) gt 10}">
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

        <c:if test="${!empty comic.events}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Events that occur in this comic:</p>
                    <table class="table" id="events">
                        <thead>
                        <th>Event Title</th>
                        <th>Event Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="event" items="${comic.events}">
                            <tr>
                                <td><a href="/superheroes/event?id=${event.id}">${event.title}</a></td>
                                <td><div>${event.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(comic.events) gt 10}">
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
                                    <option  value="50">50</option>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="col1">&nbsp;</div>
            </div>
        </c:if>

        <c:if test="${!empty comic.creators}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This comic was created by:</p>
                    <ul>
                        <c:forEach var="creator" items="${comic.creators}" varStatus="status">
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

        <c:if test="${!empty comic.urls}">
            <div class="row">
                <div class="col12">
                    <p>Follow these official links to read more about this comic:</p>
                    <ul>
                        <c:forEach var="url" items="${comic.urls}">
                            <li><a href="${url.url}" target="_blank">${url.type}</a></li>
                        </c:forEach>
                    </ul>
                </div>
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