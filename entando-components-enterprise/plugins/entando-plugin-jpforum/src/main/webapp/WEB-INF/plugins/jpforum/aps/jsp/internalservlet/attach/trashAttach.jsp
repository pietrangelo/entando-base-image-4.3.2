<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<%--	
	l'action del form è impostata in 
	/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/user/inc/userTrashAttach.jsp 
	oppure in
--%>
<div class="jpforum showlet">
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>	
	
	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
 
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />

	<div class="jpforum_block">
		<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_ATTACH_TRASH" /></h<s:property value="jpforumTitle2" />> 
		<form action="<s:property value="#deleteAttachActionPath" escape="false" />" method="post">
			<p class="noscreen">
				<wpsf:hidden name="strutsAction" />
				<wpsf:hidden name="post" />
				<wpsf:hidden name="name" />
				<wpsf:hidden name="username" />
			</p>
		
			<p><wp:i18n key="jpforum_ATTACH_TRASH_CONFIRM" />&#32;<em><s:property value="name"/></em>?</p>
			
			<p>
				<input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_CONFIRM_REMOVE" />" />
			</p>
			
			<div class="jpforum_tools_container">
				<p>
					<%-- back link --%>
					<s:set var="viewUserAttachsActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUserAttachs.action" ><wp:parameter name="username" ><s:property value="username" /></wp:parameter></wp:action></s:set>
					<a class="jpforum_goto" href="<s:property value="%{#viewUserAttachsActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_GOTO_ATTACHMENTS" /></a>
				</p>
			</div>
		</form>	
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>