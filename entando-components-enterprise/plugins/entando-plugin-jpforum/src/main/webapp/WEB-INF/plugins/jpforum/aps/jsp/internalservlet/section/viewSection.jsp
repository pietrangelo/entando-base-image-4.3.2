<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 
<s:set var="section" value="currentSection" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum showlet">
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
	 	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />
 
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/search.jsp" />
	<hr />

	<s:if test="#section != null">
		<%-- INCLUSIONE BRICIOLE --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/previous_sections.jsp" />
		<hr />
	
		<%-- INCLUSIONE SEZIONE_CORRENTE --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/inc/currentSectionInfo.jsp" />
		<hr />
		
		<%-- INCLUSIONE TABELLA SOTTO_SEZIONI --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/inc/subSections.jsp" />
		<hr />

		<s:if test="#section.root">
			<%-- 
				This is the "homepage" of our forum.
				
				nothing to do :D 
				
				insert user list?
				insert stats?
				insert what?
			 --%>
		</s:if>
		<s:else>
			<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/inc/threads.jsp" />
		</s:else>
		
	</s:if>
	<s:else>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/inc/threadNotFound.jsp" />
	</s:else>
	
	<hr /> 
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>