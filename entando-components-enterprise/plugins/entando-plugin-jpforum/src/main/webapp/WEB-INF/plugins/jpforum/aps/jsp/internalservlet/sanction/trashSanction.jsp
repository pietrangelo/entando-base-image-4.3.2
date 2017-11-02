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

	<s:set var="deleteSanctionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Sanction/deleteSanction.action" /></s:set>
	<form action="<s:property value="deleteSanctionActionPath" escape="false"/>" method="post">
		<p class="noscreen">
			<wpsf:hidden name="strutsAction" />
			<wpsf:hidden name="username" />
			<wpsf:hidden name="id" />
		</p>
		
		<div class="jpforum_block">
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_SANCTION_DELETE" /></h<s:property value="jpforumTitle2" />> 
			
			<p><wp:i18n key="jpforum_SANCTION_DELETE_MESSAGE" />:&#32;<s:property value="id"/>,&#32;<wp:i18n key="jpforum_SANCTION_DELETE_FOR_THE_USER" />:&#32;<s:property value="getUserNick(username)" />?</p>
			
			<p>
				<input class="button" type="submit" value="<wp:i18n key="jpforum_BUTTON_CONFIRM_REMOVE" />"  />
			</p>
			
			<div class="jpforum_tools_container">
				<p>
					<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
					<a class="jpforum_user_profile" href="<s:property value="%{#viewUserActionPath}" escape="false" />"><wp:i18n key="jpforum_GOTO_PROFILE_OF" />&#32;<s:property value="getUserNick(username)" /></a>
				</p>
			</div>
		</div>	
		
	</form>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>