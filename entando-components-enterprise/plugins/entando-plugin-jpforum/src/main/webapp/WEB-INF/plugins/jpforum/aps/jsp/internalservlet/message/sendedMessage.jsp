<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ page contentType="charset=UTF-8" %>
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
		
		<div class="jpforum_block">
			<s:if test='%{"MESSAGE".equalsIgnoreCase(messageType)}'>
				<s:set var="message_title"><wp:i18n key="jpforum_TITLE_PM_NEW" /></s:set>
			</s:if> 
			<s:elseif test='%{"WARN".equalsIgnoreCase(messageType)}'>
				<s:set var="message_title"><wp:i18n key="jpforum_TITLE_WARN_NEW" /></s:set>
			</s:elseif>
			
			<h<s:property value="jpforumTitle2"/> class="title_level2"><s:property value="#message_title" /></h<s:property value="jpforumTitle2" />> 
			
			<p>
				<wp:i18n key="jpforum_PM_SENT_OK" />&#32;<em><s:property value="getUserNick(recipient)" /></em>.
			</p>
			
			<div class="jpforum_tools_container">
				<p>
					<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="recipient" /></wp:parameter></wp:action></s:set>
					<a class="jpforum_user_profile" href="<s:property value="%{#viewUserActionPath}" escape="false" />"><wp:i18n key="jpforum_GOTO_PROFILE_OF" />&#32;<s:property value="getUserNick(recipient)" /></a>
				</p>
			</div>
		</div>
		
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>