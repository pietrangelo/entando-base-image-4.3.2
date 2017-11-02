<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<div class="jpforum_block jpforum_search">
	<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_SEARCH" /></h<s:property value="jpforumTitle2"/>>
	
	<s:set var="searchActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Search/search.action" /></s:set>
	<form action="<s:property value="searchActionPath" escape="false" />" method="post">
		<p>
			<%-- normal search --%>
			<label for="jpforum_search_text"><wp:i18n key="jpforum_LABEL_SEARCH_POST" /></label>:&#32;
			<s:textfield id="jpforum_search_text" name="textToFind" cssClass="text" /><input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_SEARCH" />" />
		</p>
	</form>
	
	<s:set var="searchUsersActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/Search/search.action" />#jpforum_userlist</s:set>
	<form action="<s:property value="searchUsersActionPath" escape="false" />" method="post">
		<p>
			<label for="jpforum_searchuser_text"><wp:i18n key="jpforum_LABEL_SEARCH_USERS" /></label>:&#32;
			<s:textfield cssClass="text" id="jpforum_searchuser_text" name="text" /><input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_SEARCH" />" />
		</p>
	</form>
</div>