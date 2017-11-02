<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum showlet">
	
	<%-- jAPSIntra Showlet Decoration 
	<jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" />	
	--%>
	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />

	<div class="jpforum_block">
	
		<s:if test='%{"MESSAGE".equalsIgnoreCase(messageType)}'>
			<s:set var="message_title"><wp:i18n key="jpforum_TITLE_PM_REMOVE" /></s:set>
		</s:if> 
		<s:elseif test='%{"WARN".equalsIgnoreCase(messageType)}'>
			<s:set var="message_title"><wp:i18n key="jpforum_TITLE_WARN_REMOVE" /></s:set> 
		</s:elseif>
		<h<s:property value="jpforumTitle2"/> class="title_level2"><s:property value="#message_title" /></h<s:property value="jpforumTitle2" />> 
		
		<%-- title --%>
		
		<%-- remove form --%>
		<div class="jpforum_block">
			<s:set var="removeMessageActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/doRemoveMessage.action" /></s:set>
			<s:set var="sender_nick" value="getUserNick(sender)" />
			<form action="<s:property value="removeMessageActionPath" escape="false" />" method="post">
				<p>
					<wp:i18n key="jpforum_MSG_TRASH_CONFIRM" />&#32; 
					<em><s:property value="#sender_nick" /></em>&#32;/&#32;<em><s:property value="title"/></em>&#32;/&#32;<em><s:date name="messageDate" /></em>
				</p>
				<p>
					<wpsf:hidden name="strutsAction" value="%{getStrutsAction()}" />
					<wpsf:hidden name="code" value="%{getCode()}" />
					<wpsf:hidden name="type" value="%{getMessageType()}" />
					<input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_REMOVE" />" /> 
				</p>
			</form>		
		</div>
		
		<%-- remove form --%>
		
		<div class="jpforum_block">
			<div class="jpforum_tools_container">
				<p>	
					<s:set var="viewMessagesActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewMessages.action" ><wp:parameter name="type"><s:property value="messageType" /></wp:parameter></wp:action></s:set>
					<a class="jpforum_user_messages" href="<s:property value="%{#viewMessagesActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_MESSAGES_SHOW_ALL" /></a>
					&#32;|&#32;
					<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="sender" /></wp:parameter></wp:action></s:set>
					<a class="jpforum_user_profile" href="<s:property value="%{#viewUserActionPath}" escape="false" />"><wp:i18n key="jpforum_GOTO_PROFILE_OF" />&#32;<em><s:property value="#sender_nick" /></em></a>
				</p>
			</div>  
		</div>
	</div>
	<%-- jAPSIntra Showlet Decoration 
	<jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" />
	--%>	
</div>

<%-- 
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--------------
--%>