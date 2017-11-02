<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="scwp" uri="/jpsugarcrm-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<c:catch var="clientException">
	<scwp:iframe var="sugarCRMRedirectUrlVar" />
</c:catch>
<c:if test="${!(empty clientException)}">
	<div class="alert alert-error">
		<strong><wp:i18n key="jpsugarcrm_CONNECTION_ERROR" /></strong>
		Url: <wp:info key="systemParam" paramName="jpsugarcrm_sugarCRM_baseUrl" />
	</div>
</c:if>
<c:choose>
	<c:when test="${!(empty sugarCRMRedirectUrlVar)}">
		<div class="row-fluid">
			<iframe
				class="container span12"
				src="<c:out value="${sugarCRMRedirectUrlVar}" escapeXml="false" />"
				height="<wp:currentWidget param="config" configParam="height" />">
		 			<p class="text-warning"><wp:i18n key="jpsugarcrm_ERROR_NO_IFRAME" /></p>
			</iframe>
		</div>
	</c:when>
	<c:otherwise>
		<div class="alert alert-waning">
			<wp:i18n key="jpsugarcrm_USER_NOT_ALLOWED" />
		</div>
	</c:otherwise>
</c:choose>