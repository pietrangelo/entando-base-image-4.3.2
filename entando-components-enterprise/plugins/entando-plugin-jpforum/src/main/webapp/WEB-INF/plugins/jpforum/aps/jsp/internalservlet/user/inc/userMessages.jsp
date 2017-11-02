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
	 	
	 	
	 	<s:set var="messageType" value="#parameters.type" />
		<s:if test='%{"MESSAGE".equalsIgnoreCase(#messageType)}'>
			<s:set var="message_title"><wp:i18n key="jpforum_TITLE_PM" /></s:set>
		</s:if> 
		<s:elseif test='%{"WARN".equalsIgnoreCase(#messageType)}'>
			<s:set var="message_title"><wp:i18n key="jpforum_TITLE_WARNs" /></s:set>
		</s:elseif>
		<h<s:property value="jpforumTitle2"/> class="title_level2"><s:property value="#message_title" /></h<s:property value="jpforumTitle2" />> 
		
		<s:set var="msgList" value="messages[#parameters.type]" />
		<s:if test="#msgList.size > 0">
		
			<form action="<wp:action path="/ExtStr2/do/jpforum/Front/User/viewMessages.action" ><wp:parameter name="type"><s:property value="#messageType" /></wp:parameter></wp:action>" method="post">
				<table class="jpforum_table_generic" summary="<wp:i18n key="jpforum_PIMs_LIST_SUMMARY" />">
					<caption><wp:i18n key="jpforum_LABEL_PM_LIST" /></caption>
					<tr>
						<th><wp:i18n key="jpforum_PM_STATUS" /></th>
						<th><wp:i18n key="jpforum_PM_SENDER" /></th>
						<th><wp:i18n key="jpforum_PM_SUBJECT" /></th>
						<th><wp:i18n key="jpforum_PM_DATE" /></th>
						<th><wp:i18n key="jpforum_PM_REMOVE" /></th>
					</tr>
					<s:iterator value="#msgList" var="messageId">
						<s:set var="message" value="%{getMessage(#messageId)}" />
						
						<s:set var="readMessageActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/readMessage.action" ><wp:parameter name="code"><s:property value="#message.code" /></wp:parameter></wp:action></s:set>
						<tr>
							<td>
								<s:if test="#message.toRead">
									<img src="<wp:resourceURL/>/plugins/jpforum/static/img/icons/user_message_unread.png" alt="<wp:i18n key="jpforum_PM_STATUS_TOREAD" />" title="<wp:i18n key="jpforum_PM_STATUS_TOREAD" />" />
								</s:if>
								<s:else>
									<img src="<wp:resourceURL/>/plugins/jpforum/static/img/icons/user_message_already_read.png" alt="<wp:i18n key="jpforum_PM_STATUS_READ" />" title="<wp:i18n key="jpforum_PM_STATUS_READ" />" />
								</s:else>
							</td>
							<td><s:property value="getUserNick(#message.sender)" /></td> 
							<td>
								<a title="<wp:i18n key="jpforum_PM_DOREAD" />: <s:property value="#message.title" />" href="<s:property value="%{#readMessageActionPath}" escape="false" />">
									<s:property value="#message.title"/>
								</a>
							</td> 
							<td><span title="<s:date name="#message.messageDate" nice="true"/>"><s:date name="#message.messageDate" /></span></td>
							<td>
								<s:set var="removeMessageActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/removeMessage.action" ><wp:parameter name="code"><s:property value="#message.code" /></wp:parameter></wp:action></s:set>
								<a href="<s:property value="%{#removeMessageActionPath}" escape="false" />" title="<wp:i18n key="jpforum_MSG_DELETE" />: <s:property value="#message.title" />" >
									<img src="<wp:resourceURL />plugins/jpforum/static/img/icons/attach_trash.png" alt="<wp:i18n key="jpforum_PM_DELETE" />" />
								</a>
							</td>
						</tr>
					</s:iterator>
				</table>
			</form>
						
		</s:if> 
		<s:else>
			<p><wp:i18n key="jpforum_PM_NO_PM_FOR_YOU" /></p>
		</s:else>

	</div> 

	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>