<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<wp:currentPage param="code" var="currentPageCodeVar" />
<div class="jpsalesforce-login">
	<h2 class="title">
		<wp:i18n key="jpsalesforce_TITLE_LOGIN" />
		<s:if test="%{logged}">
			&#32;<span class="icon-ok"></span>
		</s:if>
	</h2>

	<s:if test="%{!logged}">
		<s:form action="access" namespace="/do/salesforce">
			<wpsf:hidden name="redirectPage" value="%{#attr.currentPageCodeVar}" />
			<wpsf:submit type="button" cssClass="btn btn-small">
				<wp:i18n key="jpsalesforce_LOGIN_NOW" />
			</wpsf:submit>
		</s:form>
	</s:if>
	<s:else>
		&#32;
		<%/* //FIX ME: add the salesfoce logout link */ %>
		<s:set var="salesforce_logout_url" value="%{'###logout'}" />
		<span class="noscreen sr-only"><wp:i18n key="jpsalesforce_LOGIN_OK" /></span>
		<a
			class="btn btn-default btn-small"
			href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/logout">
				<wp:i18n key="jpsalesforce_LOGOUT" />
				&#32;
				<span class="icon-hand-right"></span>&#32;
		</a>
	</s:else>
</div>
