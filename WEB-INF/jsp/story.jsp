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
                <c:when test="${!empty story.thumbnail && !empty story.description}">
                    <div class="col1">&nbsp;</div>
                    <div class="col5">
                        <h2>${story.title}</h2>
                        <p>${story.description}</p>
                        <p>${story.type}</p>
                    </div>
                    <div class="col5 thumbnail">
                        <img src="${story.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:when>
                <c:otherwise>
                    <div class="col1">&nbsp;</div>
                    <div class="col10">
                        <h2>${story.title}</h2>
                        <c:if test="${!empty story.description}">
                            <p>${story.description}</p>
                        </c:if>
                        <c:if test="${!empty story.thumbnail}">
                            <div class="full-thumbnail">
                                <img src="${story.thumbnail}" height="auto" width="auto" alt="thumbnail" />
                            </div>
                        </c:if>
                    </div>
                    <div class="col1">&nbsp;</div>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${!empty story.characters}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>Characters that participate in this story:</p>
                    <table class="table" id="characters">
                        <thead>
                        <th>Character Name</th>
                        <th>Character Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="character" items="${story.characters}">
                            <tr>
                                <td><a href="/superheroes/character?id=${character.id}">${character.name}</a></td>
                                <td><div>${character.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(story.characters) gt 10}">
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

        <c:if test="${!empty story.series}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This story is featured in the following series:</p>
                    <table class="table" id="series">
                        <thead>
                        <th>Series Title</th>
                        <th>Series Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="series" items="${story.series}">
                            <tr>
                                <td><a href="/superheroes/series?id=${series.id}">${series.title}</a></td>
                                <td><div>${series.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(story.series) gt 10}">
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

        <c:if test="${!empty story.comics}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This story is featured in the following comics:</p>
                    <table class="table" id="comic">
                        <thead>
                        <th>Comic Title</th>
                        <th>Comic Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="comic" items="${story.comics}">
                            <tr>
                                <td><a href="/superheroes/comic?id=${comic.id}">${comic.title}</a></td>
                                <td><div>${comic.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(story.comics) gt 10}">
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

        <c:if test="${!empty story.events}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>The following events take place in this story:</p>
                    <table class="table" id="events">
                        <thead>
                        <th>Event Title</th>
                        <th>Event Description</th>
                        </thead>
                        <tbody>
                        <c:forEach var="event" items="${story.events}">
                            <tr>
                                <td><a href="/superheroes/event?id=${event.id}">${event.title}</a></td>
                                <td><div>${event.description}</div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${fn:length(story.events) gt 10}">
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

        <c:if test="${!empty story.creators}">
            <div class="row hide js-hide">
                <div class="col1">&nbsp;</div>
                <div class="col10">
                    <p>This story was formed by:</p>
                    <ul>
                        <c:forEach var="creator" items="${story.creators}" varStatus="status">
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

        <jsp:include page="jspf/footer.jspf"/>

    </div>
    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="web/resources/scripts/gridlayout.js"></script>
    <script src="web/resources/scripts/jquery.tablesorter.js"></script>
    <script src="web/resources/scripts/jquery.tablesorter.pager.js"></script>
    <script src="web/resources/scripts/superheroes.js"></script>
</body>
</html>