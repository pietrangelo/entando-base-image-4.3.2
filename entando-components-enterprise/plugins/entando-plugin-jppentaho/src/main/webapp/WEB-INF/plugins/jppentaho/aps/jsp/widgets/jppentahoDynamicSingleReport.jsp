<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="charset=UTF-8" %>
<c:choose>
	<c:when test="${null != sessionScope.currentUser && (!empty sessionScope.currentUser) && sessionScope.currentUser != 'guest'}">
		<wp:internalServlet actionPath="/ExtStr2/do/jppentaho/Front/pentaho/singlereport"/>
	</c:when>
	<c:otherwise>
		<div class="jppentaho">
			<p><wp:i18n key="jppentaho_MSG_PLEASE_LOGIN" /></p>
		</div>
	</c:otherwise>
</c:choose>