<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<div class="jpforum_block jpforum_previous_sections"> 
	<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 
	<s:set var="section" value="currentSection" />
	<s:set var="breadcrumbs_separator">&#32;&raquo;&#32;</s:set>
	
	<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_SECTION_PREVIOUS_SECTIONS" /></h<s:property value="jpforumTitle2"/>>
	<p>
		<s:iterator var="pathItem" value="breadCrumbs" status="status">
		<s:if test="!#status.last">
		<s:set var="viewSectionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Browse/viewSection.action" ><wp:parameter name="section"><s:property value="#pathItem" /></wp:parameter></wp:action></s:set>
			<a href="<s:property value="%{#viewSectionActionPath}" escape="false"/>"><s:property value="%{getSection(#pathItem).titles[#currentLang]}"/></a><s:property value="#breadcrumbs_separator" escape="false" />
		</s:if>
		<s:else>
			<s:if test="#request.jpforum_show_last_breadcrumb == true">
				<s:set var="viewSectionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Browse/viewSection.action" ><wp:parameter name="section"><s:property value="#pathItem" /></wp:parameter></wp:action></s:set>
				<a href="<s:property value="%{#viewSectionActionPath}" escape="false"/>"><s:property value="%{getSection(#pathItem).titles[#currentLang]}"/></a>
			</s:if>
			<s:else>
				<s:property value="%{getSection(#pathItem).titles[#currentLang]}"/>
			</s:else>
		</s:else>
		</s:iterator>
	</p>

</div>