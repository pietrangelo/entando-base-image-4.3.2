<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="jppe" uri="/jppentaho-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<%--
	Andrea Dessì <a.dessi@agiletec.it>
	13/apr/2011 10.19.14
--%>


<!--
html :: <c:out value="${html}" /> ::
-->
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>
<div class="jppentaho">

	<s:if test="hasFieldErrors()">
<div class="message message_error">	
<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
</div>
	</s:if>
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

	<div class="exportHtml">
		 ${fn:replace(html," style=\"", " pentaho_style=\"")}
	</div>
	<jppe:pentahoConfig key="reportDetailPage" var="page" />
	<wp:url page="${page}" var="pageUrl" />
	<p class="vai">
		<a class="fakebutton" href="<c:url value="${pageUrl}" >
		<c:param name="code" ><wp:currentWidget param="code" /></c:param>
		<%--
			<c:param name="solution" ><s:property value="solution" escapeXml="false" escapeHtml="false" escapeJavaScript="false" /></c:param>
			<c:param name="path" ><s:property value="path" escapeXml="false" escapeHtml="false" escapeJavaScript="false" /></c:param>
			<c:param name="name" ><s:property value="name" escapeXml="false" escapeHtml="false" escapeJavaScript="false" /></c:param>
			<c:param name="queryString" ><s:property value="queryString" escapeXml="false" escapeHtml="false" escapeJavaScript="false" /></c:param>
			<c:param name="profileParams" ><s:property value="profileParams" escapeXml="false" escapeHtml="false" escapeJavaScript="false" /></c:param>
		--%>
	</c:url><%--&amp;<s:property value="profileParams" />&amp;<s:property value="queryString" />--%>">
		<img src="<wp:imgURL/>configure.png" alt="details" />
	</a>
	</p>
</div>