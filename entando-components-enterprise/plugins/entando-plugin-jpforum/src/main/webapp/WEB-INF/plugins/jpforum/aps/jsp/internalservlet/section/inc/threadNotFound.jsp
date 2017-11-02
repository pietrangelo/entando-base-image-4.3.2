<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<div class="jpforum_block">
	<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 
	<s:set var="section" value="currentSection" />
	
	<%-- title: section not found --%>
	<h<s:property value="jpforumTitle2" /> class="title_level2"><wp:i18n key="jpforum_TITLE_SECTION_NOT_FOUND" /></h<s:property value="jpforumTitle2" />>
	
	<%-- warining: section not found --%>
	<p>
		<wp:i18n key="jpforum_SECTION_NOT_FOUND" />&#32;(<em><s:property value="#parameters.section" /></em>)
	</p>
</div>