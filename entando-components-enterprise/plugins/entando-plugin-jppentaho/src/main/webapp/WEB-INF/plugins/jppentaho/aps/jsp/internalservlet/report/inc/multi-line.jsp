<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%-- 
	MULTILINE
--%>
<p>
	<label class="bold" for="<s:property value="%{#htmlId}"/>"><s:property value="%{#current.name}"/></label>:<br />
	<textarea id="<s:property value="%{#htmlId}"/>" rows="5" cols="30" class="text" name="<s:property value="%{#current.name}"/>"></textarea>
</p>