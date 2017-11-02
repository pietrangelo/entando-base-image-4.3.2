<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<li class="<s:property value="#liClassName" />"><input type="radio" name="<s:property value="#inputFieldName" />" id="category_<s:property value="#currentRoot.code" />" value="<s:property value="#currentRoot.code" />" <s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_category_<s:property value="#currentRoot.code" />" </s:if> <s:if test="#currentRoot.code == #selectedTreeNode"> checked="checked"</s:if> /><label for="category_<s:property value="#currentRoot.code" />"><s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" /></label>
<s:if test="#currentRoot.children.length > 0">
	<ul class="treeToggler" id="tree_category_<s:property value="#currentRoot.code" />">
		<s:iterator value="#currentRoot.children" var="node">
			<s:set var="currentRoot" value="#node" />
			<s:include value="./categoryTreeBuilder.jsp" />
		</s:iterator>
	</ul>
</s:if>
</li>