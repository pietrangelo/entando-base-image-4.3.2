<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="jppe" uri="/jppentaho-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>

<div class="jppentaho jppentaho_reportLockedListDetails">
	<h2 class="title"><wp:i18n key="jppentaho_REPORTS" /></h2>
	
			<s:if test="hasActionErrors()">
			<div class="message message_error">	
			<h3><s:text name="message.title.ActionErrors" /></h3>
					<ul>
						<s:iterator value="actionErrors">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</ul>
			</div>
		</s:if>
	
	
	
        <s:set var="params" value="%{getParams()}" />
	<s:if test="%{null != #params && #params.size() > 0}">
		<wp:currentWidget param="code" var="showletCode"/>
		<ol>
			<s:iterator value="%{#params}" var="curr">
				<li>
					<jppe:pentahoConfig key="reportDetailPage" var="page" />
					<wp:url page="${page}" paramRepeat="false" var="reportDetailLink">
						<wp:urlPar name="code"><c:out  value="${showletCode}"/></wp:urlPar>
						<wp:urlPar name="solution"><s:property value="%{#curr.solution}"/></wp:urlPar>
						<wp:urlPar name="path"><s:property value="%{#curr.path}" /></wp:urlPar>
						<wp:urlPar name="name"><s:property value="%{#curr.name}" /></wp:urlPar>
						<wp:urlPar name="action"><s:property value="%{#curr.action}" /></wp:urlPar>
						<wp:urlPar name="type"><s:property value="%{#curr.type}" /></wp:urlPar>
					</wp:url>
				<a href="<c:out value="${reportDetailLink}" escapeXml="false"  />"><s:property value="%{#curr.description}" /></a>
				</li>
			</s:iterator>
		</ol>
	</s:if>
	<s:else>
		<p>
			<wp:i18n key="jppentaho_NOREPORT_FOR_SOLUTION" />
		</p>
	</s:else>
</div>