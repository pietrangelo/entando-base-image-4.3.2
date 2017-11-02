<%@ taglib prefix="jpsocial" uri="/jpsocial" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:url var="currentUrl" paramRepeat="true" />
<c:set var="socialLogoutURL"><wp:info key="systemParam" paramName="applicationBaseURL" />do/jpsocial/Front/Post/socialLogout.action</c:set>
<c:url value="${socialLogoutURL}" var="facebookLogoutUrlVar">
	<c:param name="provider" value="facebook" />
	<c:param name="redirectUrl" value="${currentUrl}" />
</c:url>
<c:url value="${socialLogoutURL}" var="twitterLogoutUrlVar">
	<c:param name="provider" value="twitter" />
	<c:param name="redirectUrl" value="${currentUrl}" />
</c:url>
<c:url value="${socialLogoutURL}" var="allLogoutUrlVar">
	<c:param name="redirectUrl" value="${currentUrl}" />
</c:url>

<jpsocial:isLogged provider="facebook" var="authorizedFB" />
<jpsocial:isLogged provider="twitter" var="authorizedTW" />

<div class="jpsocial jpsocial_connect">
	<h1><wp:i18n key="jpsocial_TITLE_CONNECT_WIDGET"/></h1>

	<c:choose>
		<%-- user quest can't use this functionality, showing login message --%>
		<c:when test="${sessionScope.currentUser == 'guest' }">
			<p><wp:i18n key="jpsocial_CONNECT_ENTANDO_FIRST" /></p>
		</c:when>
		<c:otherwise>
			<%-- if entando logged user --%>
				<h2>Facebook</h2>
					<p>
						<c:choose>
							<c:when test="${authorizedFB}">
								<a href="<c:out value="${facebookLogoutUrlVar}" />"><wp:i18n key="jpsocial_CONNECT_LOGOUT_FB" /></a>
							</c:when>
							<c:otherwise>
								<jpsocial:loginUrl currentPage="${currentUrl}" provider="facebook" logInSystem="false"/>
								<a href="<c:out value="${loginUrl}" />">
									<img src="<wp:resourceURL />plugins/jpsocial/static/img/login-facebook.png" alt="<wp:i18n key="jpsocial_CONNECT_SIGNIN_FB" />" />
								</a>
							</c:otherwise>
						</c:choose>
					</p>
				<h2>Twitter</h2>
					<p>
						<c:choose>
							<c:when test="${authorizedTW}">
								<a href="<c:out value="${twitterLogoutUrlVar}" />"><wp:i18n key="jpsocial_CONNECT_LOGOUT_TW" /></a>
							</c:when>
							<c:otherwise>
								<jpsocial:loginUrl currentPage="${currentUrl}" provider="twitter" logInSystem="false"/>
								<a href="<c:out value="${loginUrl}" />">
									<img src="<wp:resourceURL />plugins/jpsocial/static/img/login-twitter.png" alt="<wp:i18n key="jpsocial_CONNECT_SIGNIN_TW" />" />
								</a>
							</c:otherwise>
						</c:choose>
					</p>
				<c:if test="${authorizedTW && authorizedFB}">
					<wp:i18n key="jpsocial_LOGOUT" />
					<p><a class="btn" href="<c:out value="${allLogoutUrlVar}" />"><wp:i18n key="jpsocial_LOGOUT_ALL" /></a></p>
				</c:if>
		</c:otherwise>
	</c:choose>
</div>
