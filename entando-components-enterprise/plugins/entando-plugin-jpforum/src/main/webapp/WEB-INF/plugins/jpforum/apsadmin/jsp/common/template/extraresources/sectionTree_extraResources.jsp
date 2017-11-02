<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />

<script type="text/javascript">
<!--//--><![CDATA[//><!--
                  
window.addEvent('domready', function(){

	var jpforum_sectionTree = new Wood({
		rootId: "jpforum_sectionTree",
		menuToggler: "subTreeToggler",
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: "true",
		expandAllLabel: "<s:text name="label.expandAll"/>",
		collapseAllLabel: "<s:text name="label.collapseAll"/>",
		type: "tree",
		<s:if test="%{selectedNode != null && !(selectedNode.equalsIgnoreCase(''))}">
		"startIndex": "fagianonode_<s:property value="selectedNode" />",	
		</s:if>
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"			
	});

	<s:if test="#myClient == 'advanced'">
		<s:include value="/WEB-INF/apsadmin/jsp/common/template/extraresources/inc/js_trees_context_menu.jsp" />
	</s:if>
	
});



//--><!]]></script>