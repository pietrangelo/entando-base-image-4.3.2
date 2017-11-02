<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpsocial" uri="/jpsocial" %>
<wp:url paramRepeat="true" var="currentUrl"  />
<c:set var="htmlIdPrefix">jpsocial_<wp:currentPage param="code" /><wp:currentWidget param="code"/>_</c:set>
<c:set var="provider" value="facebook" />
<div class="jpsocial jpsocial_fbPost">
	<c:if test="${provider == 'facebook' }" ><wp:i18n key="jpsocial_FB_POST_TITLE" var="title" /></c:if>
	<c:if test="${provider == 'twitter' }" ><wp:i18n key="jpsocial_TW_POST_TITLE" var="title" /></c:if>
	<h1><c:out value="${title}" /></h1>

	<jpsocial:isLogged provider="${provider}" var="fbLogged"/>
	<jpsocial:loginUrl currentPage="${currentUrl}" provider="${provider}" logInSystem="true"/>
	<jpsocial:fbpost>
		<c:choose>
			<c:when test="${fbLogged}">
				<%-- display form --%>
				<form 
					method="post" 
					class="form-horizontal"
					enctype="multipart/form-data" action="<wp:url />"
					>
					<div class="control-group">
						<label class="control-label"  for="<c:out value="${htmlIdPrefix}_fb_posttext" />"><wp:i18n key="jpsocial_POST_TEXT" /></label>
						<div class="controls">
							<textarea name="postText" id="<c:out value="${htmlIdPrefix}_fb_posttext" />"><c:out value="${postText}" /></textarea>
						</div>
					</div>
					<%-- For Facebook users are allowed to upload photos --%>
					<c:if test="${provider == 'facebook' }" >
						<div class="control-group">
							<label class="control-label" for="<c:out value="${htmlIdPrefix}_fb_photo" />"><wp:i18n key="jpsocial_POST_IMAGE" /></label>
							<div class="controls">
								<input type="file" name="postImage" id="<c:out value="${htmlIdPrefix}_fb_photo" />" />
							</div>
						</div>
					</c:if>
					<div class="form-actions">
						<input type="submit" value="<wp:i18n key="jpsocial_SHARE" />" class="btn btn-primary"/>
					</div>
				</form>
			</c:when>
			<c:otherwise>
				<%-- display login link --%>
				<p>
                                    <%--<wp:i18n key="jpsocial_MUST_LOGIN" />&#32;--%>
					<c:choose>
						<c:when test="${provider == 'facebook'}">
							<a href="<c:out value="${loginUrl}" />">
								<img src="<wp:resourceURL />plugins/jpsocial/static/img/login-facebook.png" alt="<wp:i18n key="jpsocial_CONNECT_SIGNIN_FB" />" />
							</a>
						</c:when>
						<c:when test="${provider == 'twitter'}">
							<a href="<c:out value="${loginUrl}" />">
							<img src="<wp:resourceURL />plugins/jpsocial/static/img/login-twitter.png" alt="<wp:i18n key="jpsocial_CONNECT_SIGNIN_TW" />" />
							</a>
						</c:when>
					</c:choose>
				</p>
			</c:otherwise>
		</c:choose>
		<c:set var="bodyTagDisplayed" value="true" />
	</jpsocial:fbpost>

	<%-- Hide the result page if the body of the tag was displayed --%>
	<c:if test="${null == bodyTagDisplayed && bodyTagDisplayed != 'true'}">
		<c:choose>
			<c:when test="${shared == 'true'}" >
				<div class="alert alert-success">
					<strong><wp:i18n key="jpsocial_POST_SUCCESS" /></strong>
						<a href="<c:out value="${currentUrl}" />"><wp:i18n key="jpsocial_SEND_ANOTHER" /></a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="alert alert-warning">
					<strong><wp:i18n key="jpsocial_POST_FAILURE" /></strong>
					<a href="<c:out value="${loginUrl}" />"><wp:i18n key="jpsocial_TRY_AGAIN" /></a>
				</div>
			</c:otherwise>
		</c:choose>
	</c:if>
</div>