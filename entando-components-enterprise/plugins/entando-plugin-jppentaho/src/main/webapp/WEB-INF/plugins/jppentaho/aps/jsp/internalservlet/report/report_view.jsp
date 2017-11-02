<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ page contentType="charset=UTF-8" %>
<%--
	Andrea Dessì <a.dessi@agiletec.it>
	13/apr/2011 10.19.14
--%>

<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>

<div class="jppentaho">
	<h2 class="exportHtml_title_text"><s:property value="%{title}"/></h2> 
	<div class="exportHtml">
		<s:property value="html" escape="false"/>
	</div>
</div>