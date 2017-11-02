<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum showlet">
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>	
	
	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />
	
	<div class="jpforum_block">
		
		<s:if test='%{"MESSAGE".equalsIgnoreCase(messageType)}'>
			<s:set var="message_title"><wp:i18n key="jpforum_TITLE_PM_DETAILS" /></s:set>
		</s:if> 
		<s:elseif test='%{"WARN".equalsIgnoreCase(messageType)}'>
			<s:set var="message_title"><wp:i18n key="jpforum_TITLE_WARN_DETAILS" /></s:set>
		</s:elseif>
		<h<s:property value="jpforumTitle2"/> class="title_level2"><s:property value="#message_title" /></h<s:property value="jpforumTitle2" />> 
		
		<%-- title --%>
		<div class="jpforum_message_view">
			<div class="jpforum_block">
				<dl>
					<dt><wp:i18n key="jpforum_PM_SENDER" /></dt>
						<s:set var="sender_nick" value="getUserNick(sender)" />
						<dd><s:property value="#sender_nick" /></dd>
					<dt><wp:i18n key="jpforum_PM_RECIPIENT" /></dt>
						<dd><s:property value="recipient" /></dd> 
					<dt><wp:i18n key="jpforum_PM_DATE" /></dt>
						<dd><s:date name="messageDate" /> (<s:date name="messageDate" nice="true" />)</dd>
					<dt class="noscreen"><wp:i18n key="jpforum_PM_SUBJECT" /></dt>
						<dd class="noscreen"><s:property value="title"/></dd>
				</dl>
				
				<h<s:property value="jpforumTitle3"/> class="title_level3 message_title"><s:property value="title"/></h<s:property value="jpforumTitle3" />>		
				<p class="jpforum_message_view_body"><s:property value="body"/></p>
			</div>
			
			<hr />
			
			<div class="jpforum_block">
				<div class="jpforum_tools_container">
					<p>
						<s:set var="replyActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/newMessage.action" ><wp:parameter name="recipient"><s:property value="sender" /></wp:parameter><wp:parameter name="code"><s:property value="code" /></wp:parameter></wp:action></s:set>
						<a class="jpforum_message_reply" href="<s:property value="%{#replyActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_REPLY" /></a>
						&#32;|&#32;
						<s:set var="quoteActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/newMessage.action" ><wp:parameter name="recipient"><s:property value="sender" /></wp:parameter><wp:parameter name="code"><s:property value="code" /></wp:parameter><wp:parameter name="quote" value="true" /></wp:action></s:set>
						<a class="jpforum_message_quote" href="<s:property value="%{#quoteActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_QUOTE" /></a>
						&#32;|&#32;
						<s:set var="removeMessageActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/removeMessage.action" ><wp:parameter name="code"><s:property value="code" /></wp:parameter></wp:action></s:set>
						<a class="jpforum_message_trash" href="<s:property value="%{#removeMessageActionPath}" escape="false" />" title="<wp:i18n key="jpforum_MSG_DELETE" />: <s:property value="#message.title" />" ><wp:i18n key="jpforum_PM_DELETE" /></a>
					</p>	
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
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>