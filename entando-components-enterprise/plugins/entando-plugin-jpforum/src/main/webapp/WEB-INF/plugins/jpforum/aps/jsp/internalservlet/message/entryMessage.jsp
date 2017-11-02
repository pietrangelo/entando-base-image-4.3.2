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
	
	<div class="jpforum_send_message">

		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />
		 
		<div class="jpforum_block jpforum_message_edit">
			<s:if test="null != refsMessage">
				<s:if test='%{"MESSAGE".equalsIgnoreCase(refsMessage.messageType)}'>
					<s:set var="message_title"><wp:i18n key="jpforum_TITLE_PM_REPLAY" /></s:set>
				</s:if>
				<s:elseif test='%{"WARN".equalsIgnoreCase(refsMessage.messageType)}'>
					<s:set var="message_title"><wp:i18n key="jpforum_TITLE_WARN_REPLAY" /></s:set>
				</s:elseif>
			</s:if>
			<s:else>
			<s:if test='%{"MESSAGE".equalsIgnoreCase(messageType)}'>
				<s:set var="message_title"><wp:i18n key="jpforum_TITLE_PM_NEW" /></s:set>
			</s:if> 
			<s:elseif test='%{"WARN".equalsIgnoreCase(messageType)}'>
				<s:set var="message_title"><wp:i18n key="jpforum_TITLE_WARN_NEW" /></s:set>
			</s:elseif>
			</s:else>
			<h<s:property value="jpforumTitle2"/> class="title_level2"><s:property value="#message_title" /></h<s:property value="jpforumTitle2" />> 
			
			<%-- <s:property value="messageType"/> --%>
			<s:set var="saveMessageActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/saveMessage.action" /></s:set>
			<form action="<s:property value="saveMessageActionPath" escape="false" />" method="post">
			<s:token />
				<s:if test="hasActionErrors()"> 
					<div class="message message_error">
						<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="message.title.ActionErrors" /></h<s:property value="jpforumTitle3" />>	
						<ul>
						<s:iterator value="actionErrors">
							<li><s:property escape="false" /></li>
						</s:iterator>
						</ul>
					</div>
				</s:if>
					
				<s:if test="hasFieldErrors()">
					<div class="message message_error">
						<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="message.title.FieldErrors" /></h<s:property value="jpforumTitle3" />>	
						<ul>
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
							<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
						</ul>
					</div>
				</s:if>
			 		 
				<s:if test="hasActionMessages()">
					<div class="message message_confirm">
						<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="messages.confirm" /></h<s:property value="jpforumTitle3" />>	
						<ul>
							<s:iterator value="actionMessages">
								<li><s:property/></li>
							</s:iterator>
						</ul>
					</div>
				</s:if>
				<p class="noscreen">
					<wpsf:hidden name="code" />
					<wpsf:hidden name="sender" />
					<wpsf:hidden name="recipient" />
					<wpsf:hidden name="messageType" />
					<%-- PER IL REDIRECT --%>
					<wpsf:hidden name="username" value="%{getRecipient()}" />
				</p>
				<p>
					<wp:i18n key="jpforum_PM_RECIPIENT" />: <s:property value="%{getUserNick(recipient)}"/>
				</p>
				<p>
					<label for="jpforum_pim_title"><wp:i18n key="jpforum_LABEL_PM_SUBJECT" /></label>:<br />
					<s:textfield cssClass="text" id="jpforum_pim_title" name="title" />
				</p>
				<p>
					<label for="jpforum_pim_body"><wp:i18n key="jpforum_LABEL_PM_BODY" /></label>:<br />
					<s:textarea id="jpforum_pim_body" name="body" cols="50" rows="15" />
				</p>
				<p>
					<s:set var="SEND_TEXT"><wp:i18n key="jpforum_BUTTON_PM_SEND" /></s:set>
					<s:submit cssClass="button" value="%{#SEND_TEXT}" />
				</p>

				<hr />
				
				<div class="jpforum_tools_container">
					<p>
						<s:set var="viewMessagesActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewMessages.action" ><wp:parameter name="type"><s:property value="messageType" /></wp:parameter></wp:action></s:set>
						<s:if test='%{"MESSAGE".equalsIgnoreCase(messageType)}'>
							<a class="jpforum_user_messages" href="<s:property value="%{#viewMessagesActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_MESSAGES_SHOW_ALL" /></a>
							&#32;|&#32;
						</s:if>
						<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="recipient" /></wp:parameter></wp:action></s:set>
						<a class="jpforum_user_profile" href="<s:property value="%{#viewUserActionPath}" escape="false" />"><wp:i18n key="jpforum_GOTO_PROFILE_OF" />&#32;<s:property value="%{getUserNick(recipient)}"/></a>
					</p>
				</div>

				<hr />
				
			</form>

		</div>
		
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
			
</div>