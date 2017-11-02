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
		<s:set var="saveSanctionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Sanction/saveSanction.action" /></s:set>
		<form action="<s:property value="saveSanctionActionPath" escape="false" />" method="post">
			<s:token />
			<div class="jpforum_block">
				<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_SACTION_USER" />:&#32;<em><s:property value="%{getUserNick(username)}"/></em></h<s:property value="jpforumTitle2" />> 
				
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
					<wpsf:hidden name="id" />
					<wpsf:hidden name="username" />
					<wpsf:hidden name="moderator" />
				</p>
				
				<p><wp:i18n key="jpforum_SANCTION_DATE_HELP" escapeXml="false" /></p>
				
				<p>
					<label for="jpforum_sanction_start"><wp:i18n key="jpforum_LABEL_SANCTION_START_DATE" /></label>:<br />
					<s:textfield cssClass="text" id="jpforum_sanction_start" name="startDate" />
				</p>
		 
				<p>
					<label for="jpforum_sanction_end"><wp:i18n key="jpforum_LABEL_SANCTION_END_DATE" /></label>:<br />
					<s:textfield cssClass="text" id="jpforum_sanction_end" name="endDate" />
				</p>
				
				<p><wp:i18n key="jpforum_SANCTION_HELP_OUTRO" /></p>
			</div>
			
			<div class="jpforum_block">
				<p><wp:i18n key="jpforum_SANCTION_ARE_U_SURE" /></p>
				
				<p>
					<input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_SANCTION_SEND" />" />
				</p>
				
				<div class="jpforum_tools_container">
					<p>
						<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
						<a class="jpforum_user_profile" href="<s:property value="#viewUserActionPath" escape="false" />"><wp:i18n key="jpforum_GOTO_CANCEL_TO_USER" />:&#32;<em><s:property value="%{getUserNick(username)}"/></em></a>
					</p>	
				</div>
			</div>
			
		</form>
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>
