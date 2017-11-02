<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpsugarcrm" uri="/jpsugarcrm-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:info key="currentLang" var="currentLang" />
<fmt:setLocale value="${currentLang}" />
<jpsugarcrm:leadList var="list"/>
<wp:pageWithWidget var="dest_page" widgetTypeCode="jpsugarcrm_iframe"/>
<table class="table table-striped table-hover">
	<tr>
		<td><wp:i18n key="jpsugarcrm_LEAD_FULLNAME" /></td>
		<td><wp:i18n key="jpsugarcrm_LEAD_EMAIL" /></td>
		<td><wp:i18n key="jpsugarcrm_LEAD_MODIFIED" /></td>
	</tr>
	<c:forEach var="lead" items="${list}">
		<wp:url page="${dest_page.code}" var="link">
			<wp:urlPar name="redir" ><c:out value="${lead.detailLink}" escapeXml="true" /></wp:urlPar>
		</wp:url>
		<tr>
			<td>
				<a href="<c:out value="${link}" />">
					<c:choose>
						<c:when test="${(empty lead.properties['first_name']) && (empty lead.properties['last_name'])}">
							<span class="text-muted"><span class="icon icon-minus"></span></span>
						</c:when>
						<c:otherwise>
								<c:out value="${lead.properties['first_name']}" />&#32;
								<c:out value="${lead.properties['last_name']}" />
						</c:otherwise>
					</c:choose>
				</a>
			</td>
			<td>
				<c:set var="length" value="${fn:length(lead.properties['email1'])}" />
				<c:choose>
					<c:when test="${empty lead.properties['email1']}">
						<span class="text-muted"><span class="icon icon-minus"></span></span>
					</c:when>
					<c:when test="${length>15}">
						<abbr title="<c:out value="${lead.properties['email1']}" />"><c:out value="${fn:substring(lead.properties['email1'], 0, 15)}" />&nbsp;&hellip;</abbr>
					</c:when>
					<c:otherwise>
						<c:out value="${lead.properties['email1']}" />
					</c:otherwise>
				</c:choose>
				<c:remove var="length" />
			</td>
			<td>
				<c:choose>
					<c:when test="${empty lead.properties['date_modified']}">
						<span class="text-muted"><span class="icon icon-minus"></span></span>
					</c:when>
					<c:otherwise>
						<fmt:parseDate value="${lead.properties['date_modified']}" pattern="yyyy-MM-dd" var="dateModifiedVar" />
						<abbr title="<fmt:formatDate value="${dateModifiedVar}" pattern="EEEE dd MMMM yyyy" />">
							<fmt:formatDate value="${dateModifiedVar}" pattern="dd/MMM/yy" />
						</abbr>
						<c:remove var="dateModifiedVar" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</c:forEach>
</table>