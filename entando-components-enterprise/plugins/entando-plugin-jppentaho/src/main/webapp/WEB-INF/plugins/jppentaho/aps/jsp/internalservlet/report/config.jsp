<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="charset=UTF-8" %>
<%-- 
<wp:headInfo type="JS" info="lib/moo-jAPS-0.2.js" />
--%>
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css" />
<c:set var="treeRootId">jppentaho_<wp:currentPage param="code" /><wp:currentWidget param="code" /><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %></c:set>
<c:set var="config_js_code">
window.addEvent("domready", function(){
	new Wood({
		rootId: "<c:out value="${treeRootId}" />",
		menuToggler: "subTreeToggler",
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: "false",
		type: "tree"
	});
});
</c:set> 
<wp:headInfo type="JS_RAW" info="${config_js_code}"/>
<div class="jppentaho">
	<h2><wp:i18n key="jppentaho_GET_THE_REPORT" /></h2>
	<p><wp:i18n key="jppentaho_TITLE_CONFIGURE_PATH" /></p>
	
	<form action="<wp:action path="/ExtStr2/do/jppentaho/Front/pentahoConfig/saveConfig.action" />" method="post">
		<s:set name="selected" value="%{getConfig().config}"/>
		<s:set name="currentRoot" value="root" />
		<s:set name="liClassName" value="" />
		<s:set name="currentNumber" value="%{'startone'}" />
		<ul id="<c:out value="${treeRootId}" />">
			<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/directoryTreeBuilder.jsp" />
		</ul>
		<p class="vai">
			<input class="fakebutton" type="submit" value="<wp:i18n key="jppentaho_SAVE" />" /> 
		</p>
	</form>
</div>