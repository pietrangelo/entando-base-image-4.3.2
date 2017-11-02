<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
	<s:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %></s:set>
	<s:set var="htmlId" value="%{'jppentaho_'+#currentNumber+#rand}" />
	
	<s:if test="%{#currentRoot.root}">
		<s:iterator value="#currentRoot.children" var="node" status="status">
			<s:set name="currentRoot" value="#node" />
			<s:set name="liClassName" value="" />
			<s:set name="currentNumber" value="%{#status.count}" />
			<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/directoryTreeBuilderSingleReport.jsp" />
		</s:iterator>
	</s:if>
	<s:elseif test="%{isReportPath(#currentRoot.code) || #currentRoot.directory}">
		<li class="<s:property value="#liClassName" />">
			<s:if test="%{isReportPath(#currentRoot.code)}">
				<input type="radio" 
					name="configPath"
					id="<s:property value="#htmlId" />" 
					value="<s:property value="#currentRoot.code" />" 
					<s:if test="#currentRoot.children.length > 0">class="subTreeToggler tree_<s:property value="#currentRoot.code" />" </s:if> 
					<s:if test="%{#currentRoot.code==#selected}"> checked="checked"</s:if> />
				<label class="tree_toggler_label" for="<s:property value="#htmlId" />"><s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" /></label>
			</s:if>		
			<s:elseif test="%{(!isReportPath(#currentRoot.code)) && (#currentRoot.directory)}">
				<input
					type="radio" 
					id="<s:property value="#htmlId" />"
					class="subTreeToggler noscreen"
					value=""
					/>
				<label class="tree_toggler_label" for="<s:property value="#htmlId" />"><s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" /></label>
			</s:elseif>
			
			<s:if test="#currentRoot.children.length > 0">
				<c:set var="currentTreeRootId">jppentaho_treeroot_<s:property value="#currentNumber" /><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %></c:set>
				<ul class="treeToggler" id="<c:out value="${currentTreeRootId}" />">
					<s:iterator value="#currentRoot.children" var="node" status="status">
						<s:set name="currentRoot" value="#node" />
						<s:set name="liClassName" value="" />
						<s:set name="currentNumber" value="%{#status.count}" />
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/directoryTreeBuilderSingleReport.jsp" />
					</s:iterator>
				</ul>
			</s:if>
		
		</li>
	</s:elseif>
	