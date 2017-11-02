<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<div class="jpsalesfoce-lead-details">
	<h2>
		<s:if test="%{strutsAction == 2}">
			<wp:i18n key="jpsalesforce_LEAD_EDIT" />
			<%-- load lead --%>
			<s:set var="leadVar" value="%{getLead(sobj)}"/>
		</s:if>
		<s:elseif test="%{strutsAction == 1}">
			<wp:i18n key="jpsalesforce_LEAD_NEW" />
		</s:elseif>
	</h2>

	<form action="<wp:action path="/ExtStr2/do/salesforce/leads/save.action"/>" method="post" >
		<s:if test="hasActionErrors()">
			<div class="alert alert-error">
				<p class="alert-heading"><wp:i18n key="jpsalesforce_ACTION_ERRORS" /></p>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="alert alert-error">
				<p class="alert-heading"><wp:i18n key="jpsalesforce_FIELD_ERRORS" /></p>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
						<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
			<div class="alert alert-info">
				<p class="alert-heading"><wp:i18n key="jpsalesforce_ACTION_MESSAGES" /></p>
				<ul>
					<s:iterator value="actionMessages">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<p class="sr-only noscreen">
			<wpsf:hidden name="strutsAction" />
			<s:if test="%{strutsAction == 1}">
				<%-- NEW --%>
				<wpsf:hidden name="pageCode" />
			</s:if>
			<s:if test="%{strutsAction == 2}" >
				<%-- EDIT --%>
				<wpsf:hidden name="sobj" />
			</s:if>
		</p>

		
			<%-- form fields --%>
			<s:iterator var="fieldVar" value="leadFields" status="status">
				<label for="<s:property value="%{'jpsalesforce-edit-form-field-'+#status.count}" />">
					<s:property value="#fieldVar"/>
				</label>
				
				<s:if test="%{strutsAction == 1}">
					<wpsf:textfield id="%{'jpsalesforce-edit-form-field-'+#status.count}" name="%{#fieldVar}" value="%{fieldMap[#fieldVar]}" />
				</s:if>
				<s:elseif test="%{#leadVar.getAttribute(#fieldVar) != null && !#leadVar.getAttribute(#fieldVar).equals(null)}">
					<wpsf:textfield id="%{'jpsalesforce-edit-form-field-'+#status.count}" name="%{#fieldVar}" value="%{#leadVar.getAttribute(#fieldVar)}" />
				</s:elseif>
				<s:else>
					<wpsf:textfield id="%{'jpsalesforce-edit-form-field-'+#status.count}" name="%{#fieldVar}" />
				</s:else>
			</s:iterator>
		

		<p class="form-actions">
			<s:if test="%{strutsAction == 2 || strutsAction == 1}">
				<%-- save --%>
					<wpsf:submit type="button" cssClass="btn btn-primary">
						<wp:i18n key="jpsalesforce_SAVE" />
					</wpsf:submit>
			</s:if>
			<%-- go to list --%>
				<wp:pageWithWidget var="listPage" widgetTypeCode="jpsalesforce_lead_search" />
				<a
					class="btn btn-link"
					href="<wp:url page="${empty param.returnPage ? listPage.code : param.returnPage}"><wp:parameter name="searchText" value="${param.searchText}" /></wp:url>">
					 <wp:i18n key="jpsalesforce_GOTO_LIST" />
				</a>
		</p>
	</form>
</div>
