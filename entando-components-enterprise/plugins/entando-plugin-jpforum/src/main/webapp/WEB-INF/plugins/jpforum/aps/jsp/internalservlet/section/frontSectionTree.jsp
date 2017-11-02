<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<c:set var="jpforum_page_code">forum</c:set>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/widgets/frontSectionTree.css"/>

<div class="showlet">
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<div class="jpforum frontSectionTree">
		<s:form>
			<ul>
				<s:set name="inputFieldName" value="'selectedNode'" />
				<s:set name="selectedTreeNode" value="selectedNode" />
				<s:set name="liClassName" value="'page'" />
				<s:set name="currentRoot" value="treeRootNode" />
				<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/frontSectionTreeBuilder.jsp" />
			</ul>
		</s:form>
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>