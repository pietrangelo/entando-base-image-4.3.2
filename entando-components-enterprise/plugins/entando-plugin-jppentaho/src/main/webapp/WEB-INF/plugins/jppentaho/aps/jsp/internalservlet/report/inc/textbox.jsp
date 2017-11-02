<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
	TEXTBOX
--%>
<p>
	<label class="bold" for="<s:property value="%{#htmlId}"/>"><s:property value="%{#current.name}"/></label>:<br />
	<input name="<s:property value="%{#current.name}"/>" id="<s:property value="%{#htmlId}"/>" value="" type="text" class="text"/>
</p>