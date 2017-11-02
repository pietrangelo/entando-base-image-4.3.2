<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpjs" uri="/jpjasper-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:headInfo type="CSS" info="../../plugins/jpjasper/static/css/jpjasper.css" />
<div class="row-fluid jpjasper-report-preview">
	<div class="span12">
		<jpjs:reportPreview detailPageCodeVar="destPage" />
	</div>
</div>
<%--
	execReportVar contiene sussess se il render del report è SUCCESS
 --%>
<c:if test="${not empty requestScope[execReportVar] && requestScope[execReportVar] == 'success'}">
	<c:if test="${not empty uriString}">
		<div class="row-fluid">
			<div class="span12">
			<p>
				<a class="btn btn-default" href="<wp:url page="${destPage}">
					<wp:urlPar name="uriString"><c:out value="${uriString}"/></wp:urlPar>
					<wp:urlPar name="inputControlValues"><c:out value="${showletInputControlValues}"/></wp:urlPar>
					<wp:urlPar name="downloadFormats"><c:out value="${showletDownloadFormats}"/></wp:urlPar>
					<wp:urlPar name="titleParam"><c:out value="${showletTitle}"/></wp:urlPar>
				</wp:url>"><wp:i18n key="jpjasper_GO_REPORT_DETAIL" /></a>
			</p>
			</div>
		</div>
	</c:if>
</c:if>