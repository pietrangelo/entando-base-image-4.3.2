<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%-- 
	TOOGLEBUTTON
--%>
<p>
	<span class="bold"><s:property value="%{#current.name}" /></span>
</p>
<ul>
	<s:if test="%{#current.multiSelect}">
		<s:iterator value="%{#current.values}" var="item" status="status">
			<s:set var="itemId" value="%{#htmlId+'_n_'+#status.count}" />
			<li> 
				<input id="<s:property value="%{itemId}" />" name="<s:property value="%{#current.name}" />" value="<s:property value="%{#item.value}"/>" type="checkbox"/>&#32;<label for="<s:property value="%{#itemId}" />"><s:property value="%{#item.key}"/></label>
			</li>
		</s:iterator>
	</s:if>
	<s:else>
		<s:iterator value="%{#current.values}" var="item" status="status">
			<s:set var="itemId" value="%{#htmlId+'_n_'+#status.count}" />
			<li> 
				<input id="<s:property value="%{itemId}" />" name="<s:property value="%{#current.name}" />" value="<s:property value="%{#item.value}"/>" type="radio"/>&#32;<label for="<s:property value="%{#itemId}" />"><s:property value="%{#item.key}"/></label>
			</li>
		</s:iterator>
	</s:else>
</ul>