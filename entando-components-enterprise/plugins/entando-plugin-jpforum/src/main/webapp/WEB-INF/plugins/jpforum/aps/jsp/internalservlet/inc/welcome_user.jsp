<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<s:if test="#session.currentUser.username != 'guest'">
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
	<div class="jpforum_block jpforum_welcom_user">
	
		<s:set var="newMsgs" value="%{getNewMessagesCount('MESSAGE')}" />
		<s:set var="newWarns" value="%{getNewMessagesCount('WARN')}" />
		
		<s:set var="viewMessagesActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewMessages.action" ><wp:parameter name="type" value="MESSAGE" /></wp:action></s:set>
		<s:set var="viewWarnsActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewMessages.action" ><wp:parameter name="type" value="WARN" /></wp:action></s:set>
		<p>
			<wp:i18n key="jpforum_WELCOME" />&#32;<s:property value="getUserNick(#session.currentUser.username)" />,&#32;<br />
			<s:if test="#newMsgs == null">
				<wp:i18n key="jpforum_YOU_NOT_HAVE" />
			</s:if>
			<s:else>
				<wp:i18n key="jpforum_YOUHAVE" />
			</s:else>
			&#32;<a href="<s:property value="%{#viewMessagesActionPath}" escape="false"/>"><s:property value="#newMsgs" />&#32;<wp:i18n key="jpforum_PIMs" /></a>&#32;<wp:i18n key="jpforum_TOREAD" />,<br />
			<s:if test="#newWarns == null">
				<wp:i18n key="jpforum_YOU_NOT_HAVE" />
			</s:if>
			<s:else>
				<wp:i18n key="jpforum_YOUHAVE" />
			</s:else>
			&#32;<a href="<s:property value="%{#viewWarnsActionPath}" escape="false"/>"><s:property value="#newWarns" />&#32;<wp:i18n key="jpforum_WARNINGS" /></a>&#32;<wp:i18n key="jpforum_TOREAD" />.
		</p>

		<div class="jpforum_tools_container">
			<p>
				<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username" ><s:property value="#session.currentUser.username" /></wp:parameter></wp:action></s:set>
				<a class="jpforum_user_panel" href="<s:property value="#viewUserActionPath" escape="false" />"><wp:i18n key="jpforum_GOTO_UR_PANEL" /></a>
				<s:if test="%{!(#section != null && #section.root)}">
					&#32;|&#32;
					<a class="jpforum_goto" href="<wp:url paramRepeat="false" />"><wp:i18n key="jpforum_GOTO_INDEX" /></a>
				</s:if>
			</p>	
		</div>
		
	</div>
	<hr />

</s:if>