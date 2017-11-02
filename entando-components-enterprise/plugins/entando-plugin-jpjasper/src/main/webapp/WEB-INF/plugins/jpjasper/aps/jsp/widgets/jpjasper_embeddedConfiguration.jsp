<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="scwp" uri="/jpjasper-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<c:catch var="clientException">
    <scwp:iframeEmbedded var="jasperRedirectUrlVar" heightVarName="embeddedConfiguration_height"   />
</c:catch>
<c:if test="${null != clientException}">
    <div class="alert alert-block">
    	<wp:i18n key="jpjasper_CONNECTION_ERROR" /> - url '<wp:info key="systemParam" paramName="jpjasper_baseUrl" />'
    </wp:info>
</c:if>

<c:choose>
    <c:when test="${null != jasperRedirectUrlVar}">
        <iframe src="<c:out value="${jasperRedirectUrlVar}" escapeXml="true"/>" width="100%" height="<c:out value="${embeddedConfiguration_height}" />">
        <p><wp:i18n key="jpjasper_ERROR_NO_IFRAME" /></p>
        </iframe>
    </c:when>
    <c:otherwise>
        <div class="alert alert-block">
        	<wp:i18n key="jpjasper_USER_NOT_ALLOWED" />
        </div>
    </c:otherwise>
</c:choose>




