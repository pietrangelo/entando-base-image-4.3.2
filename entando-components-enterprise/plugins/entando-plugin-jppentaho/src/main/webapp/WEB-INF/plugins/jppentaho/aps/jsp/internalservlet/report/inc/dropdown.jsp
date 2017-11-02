<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
	DROPDOWN
	<s:set var="htmlId" value="%{'jppentaho_params_'+#status.count}" />
--%>
<p>
	<label class="bold" for="<s:property value="#htmlId" />"><s:property value="#current.name" /></label>:<br />
	<select name="<s:property value="#current.name" />" id="<s:property value="#htmlId" />">
		<s:iterator value="%{#current.values}" var="item">
			<option value="<s:property value="#item.value" />"><s:property value="#item.key" /></option>
		</s:iterator>
	</select>
</p>