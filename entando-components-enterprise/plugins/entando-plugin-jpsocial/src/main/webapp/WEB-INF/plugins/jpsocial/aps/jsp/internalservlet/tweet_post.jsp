<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="jpsocial" uri="/jpsocial" %>
<s:set var="htmlIdPrefix">jpsocial_<wp:currentPage param="code" /><wp:currentWidget param="code"/>_</s:set>
<div class="jpsocial tweet_post">
	<h1><wp:i18n key="jpsocial_TITLE_TWITTER" escapeXml="false" /></h1>

	<form 
		class="" 
		action="<wp:action path="/ExtStr2/do/jpsocial/Front/Post/tweet.action" />" 
		method="post" 
		>
		<s:if test="hasFieldErrors()">
			<div class="alert">
				<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
				<ul class="unstyled">
				  <s:iterator value="fieldErrors">
					<s:iterator value="value">
					  <li><s:property/></li>
					</s:iterator>
				  </s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert">
				<p><strong><s:text name="message.title.ActionErrors" /></strong></p>
				<ul class="unstyled">
				  <s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				  </s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
			<div class="alert alert-success">
				<p><strong><s:text name="messages.confirm" /></strong></p>
				<ul class="unstyled">
				  <s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				  </s:iterator>
				</ul>
			</div>
		</s:if>
	<%-- TWITTER --%>
		<jpsocial:isLogged provider="twitter"/>
		<s:set name="isLogged"><c:out value="${isLogged}"/></s:set>
		<s:if test="%{#isLogged == 'true'}" >
			<p>
				<wpsf:hidden name="queue" value="%{'twitter'}" />
				<wpsf:hidden name="provider" value="%{'twitter'}" />
				<label for="<s:property value="htmlIdPrefix" />_tweettext"><wp:i18n key="jpsocial_TWEET_TEXT" escapeXml="false" /></label>
				<wpsf:textarea name="tweet" value="%{tweet}" id="%{#htmlIdPrefix + '_tweettext'}" cols="46" cssClass="text" rows="3" />
			</p>
			<p>
				<wp:i18n key="jpsocial_SEND_TWEET" var="jpsocial_SEND_TWEET" />
				<wpsf:submit cssClass="btn btn-primary" useTabindexAutoIncrement="true" value="%{#attr.jpsocial_SEND_TWEET}" />
			</p>
		</s:if>
		<s:else>
			<p>
				<s:set name="twLink" value="%{twitterLoginPage}"/>
				<a href="<s:property value="%{twitterLoginURL}" />"><img src="<wp:resourceURL />plugins/jpsocial/static/img/login-twitter.png" alt="<wp:i18n key="jpsocial_CONNECT_SIGNIN_TW" />" /></a>
			</p>
		</s:else>
	</form>
</div>

