<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<c:set var="jpforum_page_code">forum</c:set>
<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 

<li class="<s:property value="#liClassName" />">
<s:if test="#currentRoot.code==section">
	<s:property value="%{#currentRoot.titles[#currentLang]}"/>
</s:if>
<s:else>
	<c:set var="currentRootCode"><s:property value="#currentRoot.code" /></c:set>
	<jpforum:sectionStats var="rootStats" sectionCode="${currentRootCode}" />
	<s:set var="viewSectionActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Sections/tree.action" />&amp;section=<s:property value="#currentRoot.code" /></s:set>
	<wp:url page="${jpforum_page_code}" var="viewSection">
		<wp:parameter name="post"><c:out value="${rootStats_section.code}" /></wp:parameter>
		<wp:parameter name="internalServletActionPath">/ExtStr2/do/jpforum/Front/Browse/viewSection.action</wp:parameter>
		<wp:parameter name="section"><s:property value="#currentRoot.code" /></wp:parameter>
	</wp:url>
	<a href="<c:out value="${viewSection}" escapeXml="false" />" title="<wp:i18n key="jpforum_GOTO_SECTION" />:&#32;<s:property value="%{#currentRoot.titles[#currentLang]}"/>"><s:property value="%{#currentRoot.titles[#currentLang]}"/></a>
	&#32;
	<abbr title="<c:out value="${rootStats[0]}" />&#32;<wp:i18n key="jpforum_LABEL_SECTION_THREADS" />"><c:out value="${rootStats[0]}" /></abbr>
	&#32;
	/
	&#32;
	<abbr title="<c:out value="${rootStats[1]}" />&#32;<wp:i18n key="jpforum_LABEL_SECTION_MESSAGES" />"><c:out value="${rootStats[1]}" /></abbr>
	&#32;
	<s:if test="#currentRoot.code!='forum'">
		<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpforum/Rss/showSection.action?sectionId=<s:property value="#currentRoot.code" />&amp;lang=<s:property value="#currentLang" />" title="<wp:i18n key="jpforum_RSS_LONG" />&#32;<s:property value="%{#currentRoot.titles[#currentLang]}"/>" class="rss_icon"><img src="<wp:resourceURL />plugins/jpforum/static/img/icons/rss.png" alt="RSS"/></a>
	</s:if>
	
</s:else>
<s:if test="#currentRoot.children.length > 0">
	<ul class="treeToggler" id="tree_<s:property value="#currentRoot.code" />">
		<s:iterator value="#currentRoot.children" id="node">
			<s:set name="currentRoot" value="#node" />
			<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/frontSectionTreeBuilder.jsp" />
		</s:iterator>
	</ul>
</s:if>
</li>