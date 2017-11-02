<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 
<s:set var="section" value="currentSection" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<wp:info key="systemParam"  paramName="loginPageCode" var="loginPage"/>

<div class="jpforum showlet">
	 
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
	
	<p><wp:i18n key="jpforum_PLEASE_LOGIN" /></p>
	
	<p><wp:i18n key="jpforum_USEFUL_LINKS" />:</p>
	
	<%//TODO titoli delle pagine %>
	<ul>
		<li>
			<a href="<wp:url page="${loginPage}" />"><wp:i18n key="jpforum_GOTO_LOGIN" /></a>
		</li>
		<li>
			<a href="<wp:url />"><wp:i18n key="jpforum_GOTO_FORUM_INDEX" /></a>
		</li>
	</ul>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>