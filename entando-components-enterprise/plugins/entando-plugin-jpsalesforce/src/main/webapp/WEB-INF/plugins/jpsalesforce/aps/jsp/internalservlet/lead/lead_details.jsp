<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<s:if test="logged">
	<s:set var="leadDetailVar" value="%{getLead(sobj)}" />
	<s:set var="lastnameVar" value="%{#leadDetailVar.getAttribute('LastName')}" scope="page" />
	<s:set var="firstnameVar" value="%{#leadDetailVar.getAttribute('FirstName')}" scope="page" />
</s:if>
<div class="jpsalesfoce-lead-details">
	<h2>
			<wp:i18n key="jpsalesforce_LEAD_INFO" />
			<s:if test="logged">
				<c:if test="${!(empty lastnameVar) || !(empty firstnameVar)}">:&#32;
					<c:out value="${lastnameVar}" />
					&#32;
					<c:out value="${firstnameVar}" />
				</c:if>
			</s:if>
	</h2>

	<s:if test="logged">
			<%-- details in view only --%>
				<dl class="dl-horizontal">
					<s:iterator var="fieldVar" value="leadFields" status="status">
						<dt title="<s:property value="#fieldVar"/>"><s:property value="#fieldVar"/></dt>
						<dd>
							<s:if test="%{#leadDetailVar.getAttribute(#fieldVar) != null && !#leadDetailVar.getAttribute(#fieldVar).equals(null)}">
								<s:property value="%{#leadDetailVar.getAttribute(#fieldVar)}"/>
							</s:if>
							<s:else>
								<abbr title="<wp:i18n key="jpsalesforce_EMPTYFIELD" />">&ndash;</abbr>
							</s:else>
						</dd>
					</s:iterator>
				</dl>
			<%-- buttons --%>
				<p class="form-actions">
					<%-- go to list --%>
						<wp:pageWithWidget var="listPage" widgetTypeCode="jpsalesforce_lead_search" />
						<a
							class="btn btn-primary"
							href="<wp:url page="${empty param.returnPage ? listPage.code : param.returnPage}"><wp:parameter name="searchText" value="${param.searchText}" /></wp:url>">
							 <wp:i18n key="jpsalesforce_GOTO_LIST" />
						</a>
				</p>
	</s:if>
	<s:else>
		<s:include value="/WEB-INF/plugins/jpsalesforce/aps/jsp/internalservlet/inc/sign-in-message.jsp" />
	</s:else>
</div>
