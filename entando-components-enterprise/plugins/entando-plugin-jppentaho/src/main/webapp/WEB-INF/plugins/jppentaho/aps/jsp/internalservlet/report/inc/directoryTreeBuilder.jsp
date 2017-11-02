<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
	<s:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %></s:set>
	<s:set var="htmlId" value="%{'jppentaho_'+#currentNumber+#rand}" />
	<s:if test="%{#currentRoot.root}">
		<s:iterator value="#currentRoot.children" id="node" status="status">
			<s:set name="currentRoot" value="#node" />
			<s:set name="currentNumber" value="#status.count" />
			<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/directoryTreeBuilder.jsp" />
		</s:iterator>
	</s:if>
	<s:elseif test="%{#currentRoot.directory}">
	<%-- 
	<s:elseif test="%{isReportPath(#currentRoot.code) || #currentRoot.directory}">
 	 --%>
		<li class="<s:property value="#liClassName" /><s:if test="%{#currentRoot.directory}"> node_closed</s:if>">
			<s:if test="%{#currentRoot.directory}">
				<input 
					type="radio" 
					name="configPath" 
					id="<s:property value="#htmlId" />" 
					value="<s:property value="#currentRoot.code" />" 
					<s:if test="#currentRoot.children.length > 0">class="subTreeToggler" </s:if> 
					<s:if test="#currentRoot.code == #selected"> checked="checked"</s:if> />
				<label for="<s:property value="#htmlId" />">
					<s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
				</label>
			</s:if>
			<s:elseif test="%{(!#currentRoot.directory) && (isReportPath(#currentRoot.code))}"> 
				<s:property value="getTitle(#currentRoot.code, #currentRoot.titles)" />
			</s:elseif>
		
			<s:if test="#currentRoot.children.length > 0">
				<c:set var="currentTreeRootId">jppentaho_treeroot_<s:property value="#currentNumber" /><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %></c:set>
				<ul class="treeToggler" id="<c:out value="${currentTreeRootId}" />">
					<s:iterator value="#currentRoot.children" id="node" status="status">
						<s:set name="currentRoot" value="#node" />
						<s:set name="currentNumber" value="#status.count" />
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/directoryTreeBuilder.jsp" />
					</s:iterator>
				</ul>
			</s:if>
		</li>
	</s:elseif>