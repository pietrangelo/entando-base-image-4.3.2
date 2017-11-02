<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />

	<s:if test="getStrutsAction() == 2">
<script type="text/javascript">
<!--//--><![CDATA[//><!--
window.addEvent('domready', function(){
	var pageTree = new Wood({
		rootId: "pageTree",
		menuToggler: "subTreeToggler",
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: "true",
		expandAllLabel: "<s:text name="label.expandAll"/>",
		collapseAllLabel: "<s:text name="label.collapseAll"/>",
		type: "tree",
		startIndex: "<s:property value="subsite.contentViewerPage" />",
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"			
	});
});

//--><!]]>--</script>
	</s:if>