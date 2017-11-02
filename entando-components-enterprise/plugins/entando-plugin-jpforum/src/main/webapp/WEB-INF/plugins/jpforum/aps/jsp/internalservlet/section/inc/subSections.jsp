<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 
<s:set var="section" value="currentSection" />

<div class="jpforum_block jpforum_subsections">
	<%-- subsection title --%>
	<h<s:property value="jpforumTitle2" /> class="title_level2"><wp:i18n key="jpforum_TITLE_SUB_SECTIONS" /></h<s:property value="jpforumTitle2" />>
	<s:if test="null != #section.children &&  #section.children.length > 0">
		<table class="jpforum_table_generic" summary="<wp:i18n key="jpforum_SECTION_SUBSECTION_SUMMARY" />">
			<caption><wp:i18n key="jpforum_SECTION_SUBSECTION_LIST" /></caption>
			<tr>
				<th><wp:i18n key="jpforum_LABEL_SECTION_TITLE" /></th>
				<th><wp:i18n key="jpforum_LABEL_SECTION_SUBJECT" /></th>
				<th><wp:i18n key="jpforum_LABEL_SECTION_STATUS" /></th>
				<th><wp:i18n key="jpforum_LABEL_SECTION_THREADS" /></th>
				<th><wp:i18n key="jpforum_LABEL_SECTION_MESSAGES" /></th>
			</tr>
		
			<s:iterator var="child" value="#section.children" >
				<s:set var="viewSectionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Browse/viewSection.action" ><wp:parameter name="section"><s:property value="#child.code" /></wp:parameter></wp:action></s:set>
				<s:set var="subSectionStats" value="%{getSectionStatistics(#child.code)}" /> 
				<tr>
					<td>
						<h<s:property value="jpforumTitle3" />>
							<a class="jpforum_section_open_<s:property value="%{#child.open}" />" href="<s:property value ="%{#viewSectionActionPath}" escape="false" />"><s:property value="%{#child.titles[#currentLang]}"/></a>
						</h<s:property value="jpforumTitle3" />>
					</td> 
					<td>
						<s:property value="%{#child.descriptions[#currentLang]}"/>
					</td>
					<td> 
						<s:if test="%{#child.open}">
							<wp:i18n key="jpforum_LABEL_SECTION_STATUS_OPEN" />
						</s:if>
						<s:else>
							<wp:i18n key="jpforum_LABEL_SECTION_STATUS_CLOSED" />
						</s:else>
					</td>
					<td><s:property value="#subSectionStats[0]" /></td>
					<td><s:property value="#subSectionStats[1]" /></td>
				</tr>
			</s:iterator>
		</table>
	</s:if>
	<s:else>
		<p><wp:i18n key="jpforum_NO_SUBSECTIONS" /></p>
	</s:else>
</div>