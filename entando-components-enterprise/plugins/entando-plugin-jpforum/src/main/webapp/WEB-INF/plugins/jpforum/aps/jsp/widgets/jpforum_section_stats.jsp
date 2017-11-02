<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<wp:info key="currentLang" var="currentLang" />
<%-- Remove sectionCode to make this showlet dynamic. It will check for a parameter section --%>
<jpforum:sectionStats var="rootStats" />
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/widgets/jpforum_section_stats.css"/>
<c:set var="jpforum_page_code">forum</c:set>

<div class="showlet">
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<div class="jpforum jpforum_section_stats">
		<wp:url page="${jpforum_page_code}" var="viewThread"><wp:parameter name="post"><c:out value="${rootStats_section.code}" /></wp:parameter></wp:url>
		<h3><a href="<c:out value="${viewThread}" />" title="<wp:i18n key="jpforum_GOTO_SECTION" />:&#32;<c:out value="${rootStats_section.titles[currentLang]}" />"><c:out value="${rootStats_section.titles[currentLang]}" /></a>&#32;<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpforum/Rss/showSection.action?sectionId=<c:out value="${rootStats_section.code}" />&amp;lang=<c:out value="${currentLang}" />" title="<wp:i18n key="jpforum_RSS_LONG" />&#32;<c:out value="${rootStats_section.titles[currentLang]}" />" class="rss_icon"><img src="<wp:resourceURL />plugins/jpforum/static/img/icons/rss.png" alt="RSS"/></a></h3>
		<%-- 
		<p>
			<c:out value="${rootStats_section.descriptions[currentLang]}" />
		</p>
		--%>
		<dl>
			<dt><wp:i18n key="jpforum_LABEL_SECTION_THREADS" /></dt>
				<dd><c:out value="${rootStats[0]}" /></dd>
			<dt><wp:i18n key="jpforum_LABEL_SECTION_MESSAGES" /></dt>
				<dd><c:out value="${rootStats[1]}" /></dd>
		</dl>
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>