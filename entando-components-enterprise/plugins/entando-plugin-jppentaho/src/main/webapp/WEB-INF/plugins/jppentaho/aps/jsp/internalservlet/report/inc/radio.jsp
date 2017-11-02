<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%-- 
	RADIO
--%>
<p>
	<span class="bold"><s:property value="%{#current.name}" /></span>
</p>
<ul>
	<s:iterator value="%{#current.values}" var="item" status="status">
		<s:set var="itemId" value="%{#htmlId+'_n_'+#status.count}" />
		<li>
			<wpsf:radio name="%{#current.name}" value="%{#item.value}" id="%{#itemId}" />&#32;<label for="<s:property value="%{#itemId}" />"><s:property value="%{#item.key}"/></label>
		</li>
	</s:iterator>
</ul>