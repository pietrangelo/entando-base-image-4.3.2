<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpgk" uri="/jpgokibi-aps-core" %>
<wp:currentWidget param="config" configParam="baseUrl" var="gokibiBaseUrlVar" />

<c:set var="gokibiScriptVar">
	$(document).ready(function() {
        alto_init("<c:out value="${gokibiBaseUrlVar}" />", "<wp:currentWidget param="config" configParam="pid" />", "<c:choose><c:when test="${sessionScope.currentUser.entandoUser}">token</c:when><c:otherwise><c:out value="${sessionScope.currentUser.token}"/></c:otherwise></c:choose>  ")
	})
</c:set>
<wp:headInfo type="JS_RAW" var="gokibiScriptVar" />
<%--<c:set var="gokibiExtCssVar"><c:out value="${gokibiBaseUrlVar}" />/static/assets/vendor/bootstrap/css/bootstrap.min.css</c:set>--%>
<%--<wp:headInfo type="CSS_EXT" var="gokibiExtCssVar" />--%>
<c:set var="gokibiExtJsVar"><c:out value="${gokibiBaseUrlVar}" />/static/js/alto_api.js</c:set>
<wp:headInfo type="JS_EXT" var="gokibiExtJsVar" />
<jpgk:gokibiWidget escapeXml="false" />