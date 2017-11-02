<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%-- 
	LIST
--%>
<p>
	<label class="bold" for="<s:property value="%{#htmlId}" />"><s:property value="%{#current.name}" /></label>:<br />
	<s:if test="%{#current.multiSelect}"><s:set var="size" value="5" /></s:if>
	<s:else><s:set var="size" value="1" /></s:else>
	<wpsf:select name="%{#current.name}" cssClass="text" id="%{#htmlId}" list="%{#current.values}" multiple="%{#current.multiSelect}" listKey="value" listValue="key" size="%{#size}" />
</p>