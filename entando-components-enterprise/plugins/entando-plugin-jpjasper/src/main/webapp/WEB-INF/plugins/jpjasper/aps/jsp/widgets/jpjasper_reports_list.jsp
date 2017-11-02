<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpjs" uri="/jpjasper-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<jpjs:reportList var="folders"  titleVar="titleList" pageCodeVar="destPage" />
<c:if test="${!(empty titleList)}">
	<h1><c:out value="${titleList}" /></h1>
</c:if>
<div class="jpjasper-report-list">
	<c:choose>
		<c:when test="${!empty folders}">
			<ul>
				<c:forEach items="${folders}" var="item" >
				<li>
					<wp:url page="${destPage}" var="var_viewReportURL">
						<wp:urlPar name="uriString"><c:out value="${item.uriString}"/></wp:urlPar>
					</wp:url>
					<a
						title="<wp:i18n key="jpjasper_GO_REPORT_DETAIL" />&#32;<c:out value="${item.label}" />"
						href="<c:out value="${var_viewReportURL}" />"
						><c:out value="${item.label}" /></a>
				</li>
				</c:forEach>
			</ul>
		</c:when>
		<c:otherwise>
			<div class="alert alert-info">
				<wp:i18n key="jpjasper_NOREPORTS_ON_LIST" />
			</div>
		</c:otherwise>
	</c:choose>
</div>
<c:set var="folders" value="${null}" />
<c:set var="destPage" value="${null}" />