<%@ taglib prefix="jpsocial" uri="/jpsocial" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:url paramRepeat="true" var="currentUrl" />
<c:set var="provider" value="twitter" />
<jpsocial:loginUrl currentPage="${currentUrl}" provider="${provider}" logInSystem="true" />
<div class="jpsocial jpsocial_twlogin">
	<h1><wp:i18n key="jpsocial_LOGIN_TITLE" />&nbsp;<wp:i18n key="jpsocial_LOGIN_TWITTER" /></h1>
	<c:choose>
		<c:when test="${authorizedUser}">
			<p>
					<wp:i18n key="WELCOME" /> <em title="<c:out value="${sessionScope.currentUser.username}" /> / <c:out value="${sessionScope.currentUser.provider}" />"><c:out value="${sessionScope.currentUser}" /></em>!&#32;
					<a 
						class="btn btn-small" 
						href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/logout" >
						<wp:i18n key="jpsocial_LOGOUT" />
					</a>
			</p>
		</c:when>
		<c:otherwise>
			<p>
				<a href="<c:out value="${loginUrl}" />">
					<img src="<wp:resourceURL />plugins/jpsocial/static/img/login-twitter.png" alt="<wp:i18n key="jpsocial_CONNECT_SIGNIN_TW" />" />
				</a>
			</p>
		</c:otherwise>
	</c:choose>
</div>