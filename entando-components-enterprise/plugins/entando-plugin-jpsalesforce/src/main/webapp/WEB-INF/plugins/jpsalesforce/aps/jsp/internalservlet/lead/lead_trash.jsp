<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<div class="jpsaleforce-lead-trash">
	<h2><wp:i18n key="jpsalesforce_LEAD_TRASH"/></h2>
	<s:if test="logged">
		<form action="<wp:action path="/ExtStr2/do/salesforce/leads/delete.action"/>" method="post" >
			<s:set var="leadDetailVar" value="%{getLead(sobj)}" />
			<p>
				<wpsf:hidden name="sobj"/>
				<wp:i18n key="jpsalesforce_DELETE_CONFIRM"/>
				&#32;
				<em><s:property	value="#leadDetailVar.name" /></em>
				?
			</p>
			<p class="form-actions">
				<%-- delete button --%>
				<wpsf:submit type="button" cssClass="btn btn-danger">
					<wp:i18n key="jpsalesforce_DELETE"/>
				</wpsf:submit>
				<%-- go to list --%>
					<wp:pageWithWidget var="listPage" widgetTypeCode="jpsalesforce_lead_search" />
					<a class="btn btn-link"
						href="<wp:url page="${empty param.returnPage ? listPage.code : param.returnPage}" />">
						<wp:i18n key="jpsalesforce_GOTO_LIST" />
					</a>
			</p>
		</form>
	</s:if>
	<s:else>
		<s:include value="/WEB-INF/plugins/jpsalesforce/aps/jsp/internalservlet/inc/sign-in-message.jsp" />
	</s:else>
</div>
