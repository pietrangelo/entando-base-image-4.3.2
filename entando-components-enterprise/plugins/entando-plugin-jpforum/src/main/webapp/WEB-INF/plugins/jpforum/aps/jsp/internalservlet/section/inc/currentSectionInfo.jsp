<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<div class="jpforum_block jpforum_currentsection_info">
	<%-- current section title --%>
	<h<s:property value="jpforumTitle2" /> class="title_level2"><wp:i18n key="jpforum_TITLE_SECTION" />:&#32;<s:property value="%{#section.titles[#currentLang]}"/></h<s:property value="jpforumTitle2" />>
	
	<%-- current section description e info --%>
	<p><s:property value="%{#section.descriptions[#currentLang]}"/></p>

	<s:set var="stats" value="%{getSectionStatistics(#section.code)}" /> 
	<p>
		<strong><s:property value="#stats[1]"/></strong>&#32;<wp:i18n key="jpforum_POSTS_IN_THIS_SECTION" />&#32;
		<wp:i18n key="jpforum_POSTS_ORGANIZED_IN_start" />&#32;<strong><s:property value="#stats[0]"/></strong>&#32;<wp:i18n key="jpforum_POSTS_ORGANIZED_IN_end" />
	</p>
</div>