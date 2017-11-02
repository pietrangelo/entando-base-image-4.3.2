<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
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
		
		<s:if test="#session.currentUser.username != username">
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_USER_PROFILE" />: <s:property value="%{getUserNick(username)}"/></h<s:property value="jpforumTitle2" />> 
		</s:if>
		<s:else>
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_YOUR_PROFILE" /></h<s:property value="jpforumTitle2" />> 
		</s:else>
			  
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
			
		<%-- la presenza di actionMessages indica che 
		non esiste l'utente di cui si vuole visualizzare il profilo --%>
		<s:if test="!hasActionMessages()">
			
			<div class="jpforum_block">
				<p>
					<s:set var="currentUsername"><s:property value="username"/></s:set>
					<jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  username="${currentUsername}"/>
					<s:if test="null != #request.currentAvatar">
						<img src="<s:property value="#request.currentAvatar"/>" alt=""/>
					</s:if>
				</p>
				
				<p>
					<s:set var="viewUserpostsActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUserPosts.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set> 
					<wp:i18n key="jpforum_USER_POSTS_NUMBER" />&#32;<s:property value="%{getUserNick(username)}"/>: <strong><s:property value="%{getUserPosts(username).size}"/></strong>. 
				</p>
				
				<p>
					<s:set var="viewUserThreadssActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUserThreads.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
					<wp:i18n key="jpforum_USER_THREADS_NUMBER" />&#32;<s:property value="%{getUserNick(username)}"/>: <strong><s:property value="%{getUserThreads(username).size}"/></strong>.
				</p>
				
				<div class="jpforum_tools_container">
					<p>
						<a class="jpforum_post_list" href="<s:property value="%{#viewUserpostsActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_USER_SHOW_POSTS" /></a>&#32;| 
						<a class="jpforum_thread_list" href="<s:property value="viewUserThreadssActionPath" escape="false" />"><wp:i18n key="jpforum_BUTTON_USER_SHOW_THREADS" /></a>
					</p>
				</div>
			</div>
			
			<s:if test="#session.currentUser.username != username">
				<div class="jpforum_block">
					<h<s:property value="jpforumTitle3"/> class="title_level3"><wp:i18n key="jpforum_TITLE_PM" /></h<s:property value="jpforumTitle3" />>
					
					<div class="jpforum_tools_container">
						<p>
							<s:set var="newMessageActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/newMessage.action" ><wp:parameter name="recipient"><s:property value="username" /></wp:parameter></wp:action></s:set>
							<a class="jpforum_message_new" href="<s:property value="%{#newMessageActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_MESSAGE_SEND_MSG_TO" />&#32;<s:property value="%{getUserNick(username)}"/></a>
						</p>
					</div>
				</div>
			</s:if>
			<s:elseif test="#session.currentUser.username == username">
				
				<hr /> 
	
				<div class="jpforum_block">
					<div class="jpforum_manage_attach">
						<s:set var="attachs" value="%{getUserAttachs(username)}" />
						<h<s:property value="jpforumTitle3"/> class="title_level3"><wp:i18n key="jpforum_TITLE_ATTACH_MANAGEMENT" /></h<s:property value="jpforumTitle3" />> 
						<p><wp:i18n key="jpforum_ATTACHMENTS_INSERTED" />:&#32;<s:property value="#attachs.size"/>.</p>
						<%//TODO inserire spazio libero!!!! %>
						<s:set var="usedSpace" value="%{getUsedSpace(username) / 1024}" />
						<s:set var="totalSpace" value="%{getUserAttachSpace(username) / 1024}" />
						<s:set var="availableSpace" value="(#totalSpace - #usedSpace)" />
						<p><wp:i18n key="jpforum_USED_SPACE" />:&#32;<s:property value="#usedSpace"/>KB (<wp:i18n key="jpforum_LABEL_FREE_SPACE" />&#32;<s:property value="#availableSpace"/>KB, <wp:i18n key="jpforum_LABEL_TOTAL_SPACE" />&#32;<s:property value="#totalSpace"/>KB)</p>
						<s:if test="#attachs.size > 0">
							<div class="jpforum_tools_container">  
								<p>
									<s:set var="viewUserAttachsActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUserAttachs.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
									<a class="jpforum_attach_manage" href="<s:property value="%{#viewUserAttachsActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_MANAGE_ATTACHMENTS" /></a>
								</p>
							</div>
						</s:if>
					</div>
				</div>
					
				<hr />
				
				<div class="jpforum_block">
					<div class="jpforum_manage_messages">
						<h<s:property value="jpforumTitle3"/> class="title_level3"><wp:i18n key="jpforum_TITLE_PM" /></h<s:property value="jpforumTitle3" />> 
			
						<s:set var="msgList" value="messages['MESSAGE']"></s:set>
						<s:set var="warnList" value="messages['WARN']"></s:set>

						<s:set var="newMsgs" value="%{getNewMessagesCount('MESSAGE')}" />
						<s:set var="newWarns" value="%{getNewMessagesCount('WARN')}" />
						<p>
							<s:if test="null != #newMsgs">
								<wp:i18n key="jpforum_YOUHAVE" />&#32;<strong><s:property value="#newMsgs" />&#32;<wp:i18n key="jpforum_NEW_PMs" /></strong>&#32;<wp:i18n key="jpforum_TOREAD" />,&#32;<wp:i18n key="jpforum_TOTAL_COUNT" />&#32;<s:property value="#msgList.size"/>.
							</s:if>
							<s:else>
								<wp:i18n key="jpforum_MESSAGES_NO_MESSAGE" />
							</s:else>
						</p>
						
						<div class="jpforum_tools_container">
							<p>
								<s:set var="viewMessagesActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewMessages.action" ><wp:parameter name="type" value="MESSAGE" /></wp:action></s:set>
								<a class="jpforum_user_messages" href="<s:property value="%{#viewMessagesActionPath}" escape="false"/>"><wp:i18n key="jpforum_BUTTON_MESSAGES_SHOW_ALL" /></a>
							</p>
						</div>
					</div>
				</div>
	
				<hr />					
				
				<div class="jpforum_block">
					<h<s:property value="jpforumTitle3"/> class="title_level3"><wp:i18n key="jpforum_TITLE_SANCTIONS" /></h<s:property value="jpforumTitle3" />> 
					<p>
						<s:if test="null != #newWarns">
							<wp:i18n key="jpforum_YOUHAVE" />&#32;<strong><s:property value="#newWarns" />&#32;<wp:i18n key="jpforum_NEW_WARNs" /></strong>&#32;<wp:i18n key="jpforum_TOREAD" />,&#32;<wp:i18n key="jpforum_TOTAL_COUNT" />&#32;<s:property value="#warnList.size"/>.
						</s:if>
						<s:else>
							<wp:i18n key="jpforum_WARNs_NO_WARN" />
						</s:else>
					</p>
					<div class="jpforum_tools_container">
						<p>
							<s:set var="viewWarnsActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewMessages.action" ><wp:parameter name="type" value="WARN" /></wp:action></s:set>
							<a class="jpforum_warning" href="<s:property value="%{#viewWarnsActionPath}" escape="false"/>"><wp:i18n key="jpforum_BUTTON_WARNs_SHOW_ALL" /></a>
						</p>					
					</div>
				</div>
									
			</s:elseif>
			
			<wp:ifauthorized permission="jpforum_superuser">
				<s:if test="#session.currentUser.username != username">
					<div class="jpforum_block">
						<h<s:property value="jpforumTitle3"/> class="title_level3"><wp:i18n key="jpforum_TITLE_SANCTIONS" /></h<s:property value="jpforumTitle3" />>  
						<s:set value="sanctions" var="sanctionsId" />
						<s:if test="#sanctionsId.size > 0">
							<table class="jpforum_table_generic" summary="<wp:i18n key="jpforum_SANCTION_LIST_SUMMARY" />">
								<caption><wp:i18n key="jpforum_LABEL_SACTION_LIST" /></caption>
								<tr>
									<th><wp:i18n key="jpforum_LABEL_SANCTION_START_DATE" /></th>
									<th><wp:i18n key="jpforum_LABEL_SANCTION_END_DATE" /></th>
									<th><wp:i18n key="jpforum_LABEL_SANCTION_SENDER" /></th>
									<th><wp:i18n key="jpforum_SANCTION_DELETE" /></th>
								</tr>
								<s:iterator var="id" value="#sanctionsId">
									<s:set var="sanction" value="%{getSanction(#id)}" />
									<s:set var="trashSanctionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Sanction/trashSanction.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter><wp:parameter name="id"><s:property value="#id" /></wp:parameter></wp:action></s:set>
									<tr>
										<td><s:date name="#sanction.startDate" format="dd/MM/yyyy" /></td>
										<td><s:date name="#sanction.endDate" format="dd/MM/yyyy" /></td> 
										<td>
											<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#sanction.moderator" /></wp:parameter></wp:action></s:set>
											<a href="<s:property value="#viewUserActionPath" escape="false" />"><s:property value="getUserNick(#sanction.moderator)" /></a>
										</td>
										<td><a href="<s:property value="%{#trashSanctionActionPath}" escape="false" />"><wp:i18n key="jpforum_SANCTION_DELETE" /></a></td>
									</tr>
								</s:iterator>
							</table>
						</s:if>						
						
						<s:else>
							<p><wp:i18n key="jpforum_SACTION_NO_SANTION_FOR_THIS_USER" />&#32;<s:property value="%{getUserNick(username)}"/>.</p>
						</s:else>
						
			
						<div class="jpforum_tools_container">
							<p>
								<s:set var="newWarnActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Message/newWarn.action" ><wp:parameter name="recipient"><s:property value="username" /></wp:parameter></wp:action></s:set>
								<a class="jpforum_warning" href="<s:property value="%{#newWarnActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_SEND_WARN" /></a>&#32;|&#32;
								
								<s:set var="addSanctionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Sanction/newSanction.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
								<a class="jpforum_sanction" href="<s:property value="%{#addSanctionActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_SEND_SANCTION" />&#32;<s:property value="%{getUserNick(username)}"/></a>
							</p>
						</div>
					</div>
				</s:if>
			</wp:ifauthorized>
			 
		</s:if>
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>