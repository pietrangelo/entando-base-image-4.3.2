<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="jppe" uri="/jppentaho-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ page contentType="charset=UTF-8" %>
<%--
	Andrea Dessì <a.dessi@agiletec.it>
	13/apr/2011 10.19.14
--%>

<s:set var="title" value="getTitle()" />
<s:if test='%{#title!=null&&!#title.equalsIgnoreCase("")}'><c:set var="showletTitle" scope="request"><s:property value="#title" escapeXml="false"/></c:set></s:if>
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>
<c:set var="showletTitle" value="${null}" scope="request" />
<div class="jppentaho">
	<s:set var="isDetail" value="%{detailReport}" />
	<c:if test="${isLockedDetail}"><s:set var="isDetail" value="%{true}" /></c:if>
	<s:if test="#isDetail">
		<h2 class="exportHtml_title_text"><s:property value="%{#title}"/></h2>
		<s:set var="description" value="#description" />
		<s:if test='%{#description!=null&&!#description.equalsIgnoreCase("")}' >
			<div class="exportHtml_title">
				<p>
					<s:property value="%{#description}"/>
				</p>
			</div>
		</s:if>
	</s:if>

	<div class="exportHtml">
		<s:property value="html" escape="false"/>
	</div>
	<s:if test="#isDetail==false">
		<jppe:pentahoConfig key="reportDetailPage" var="page" />
		<wp:url page="${page}" var="pageUrl"><wp:urlPar name="code"><wp:currentWidget param="code"/></wp:urlPar></wp:url>
			<p class="vai">
				<a href="<c:out value="${pageUrl}" />" title="<wp:i18n key="jppentaho_GODETAILS" />">
					<img src="<wp:imgURL/>configure.png" alt="details" />
				</a>
			</p>
	</s:if>
	<s:set var="name" value="%{null}" />
	<s:set var="isDetail" value="%{null}" />
</div>