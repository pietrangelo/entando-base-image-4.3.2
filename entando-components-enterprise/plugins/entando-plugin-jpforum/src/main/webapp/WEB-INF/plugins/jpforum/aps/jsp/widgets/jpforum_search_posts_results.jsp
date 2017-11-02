<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<%//TODO inheredit these settings from /WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp --%>
<c:set var="jpforum_page_code">forum</c:set>
<c:set var="jpforumPostHtmlId">jpforumPost</c:set>
<c:set var="jpforum_time_format">dd MMM yyyy '<wp:i18n key="jpforum_TIME_AT" />' HH:mm:ss</c:set>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/widgets/jpforum_search_posts_results.css"/>

<div class="showlet">
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<div class="jpforum jpforum_search_posts_results">
		
		<%//TODO change the way of getthing the searched key %>
		<%//FIXME change the way of getthing the searched key %>
			<c:set var="searched_key" value="${pageContext.request.parameterMap['textToFind'][0]}" />
			<c:if test="${!empty searched_key}">
				<p><wp:i18n key="SEARCHED_FOR" />: <em><strong><c:out value="${searched_key}" /></strong></em></p>
			</c:if>
		
		<wp:info key="currentLang" var="currentLang" />
		<fmt:setLocale value="${currentLang}"/>
		<jpforum:searcher var="result" />
		<c:choose>
			<c:when test="${empty result}">
				<p><em><wp:i18n key="SEARCH_NOTHING_FOUND" /></em></p>
			</c:when>
			<c:otherwise>
				<wp:pager listName="result" objectName="group" max="10" pagerIdFromFrame="true" >
					<p><em><wp:i18n key="SEARCH_RESULTS_INTRO" />&#32;<c:out value="${group.size}" />&#32;<wp:i18n key="SEARCH_RESULTS_OUTRO" />&#32;[<c:out value="${group.begin + 1}" />&#32;&ndash;&#32;<c:out value="${group.end + 1}" />]:</em></p>
					<c:set var="group" value="${group}" scope="request" />
					<c:import url="/WEB-INF/aps/jsp/widgets/inc/pagerBlock.jsp" />
					<ul>
						<c:forEach var="postId" items="${result}" begin="${group.begin}" end="${group.end}" varStatus="posts">
							<li>
								<wp:url page="${jpforum_page_code}" var="viewPost"><wp:parameter name="post"><c:out value="${postId}" /></wp:parameter></wp:url>
								<jpforum:post postId="${postId}" var="currentPost" />
								<jpforum:thread var="currentThread" threadId="${currentPost.threadId}" />
								<%-- titolo del post--%>
									<%//TODO : inserire nickname corretto al posto dello username %>
									<h3><a title="<wp:i18n key="jpforum_GOTO_SINGLE_POST" />:&#32;<wp:i18n key="jpforum_SEARCH_RESULT" />&#32;<c:out value="${posts.count}" />" href="${viewPost}#<c:out value="${jpforumPostHtmlId}" /><c:out value="${postId}" />">&#32;<wp:i18n key="jpforum_SEARCH_RESULT" />&#32;<c:out value="${posts.count}" />,&#32;<abbr title="<wp:i18n key="jpforum_POST_LONG" />"><wp:i18n key="jpforum_POST_SHORT" /></abbr>&#32;<wp:i18n key="jpforum_WRITTEN_BY" />&#32;<c:out value="${currentPost.username}" />&#32;<wp:i18n key="jpforum_ON_DATE" />&#32;<fmt:formatDate value="${currentPost.postDate}" pattern="${jpforum_time_format}"  timeStyle="both" /></a></h3>
								<%-- testo del post --%>
								<div class="jpforum_message_text">
									<c:out value="${currentPost.text}" escapeXml="false" />
								</div> 
								<%-- vai alla discussione --%>
								<p class="jpforum_goto_thread">
									<wp:url page="${jpforum_page_code}" var="viewThread"><wp:parameter name="post"><c:out value="${currentThread.post.code}" /></wp:parameter></wp:url>
									<a class="jpforum_goto" href="${viewThread}#jpforumPost<c:out value="${currentThread.post.code}" />"><wp:i18n key="jpforum_GOTO_THREAD" />&#32;<em><c:out value="${currentThread.post.title}" /></em></a>
								</p>
							</li>
						</c:forEach>
					</ul>
					<c:import url="/WEB-INF/aps/jsp/widgets/inc/pagerBlock.jsp" />	
				</wp:pager>	
			
		</c:otherwise>
		</c:choose>
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>