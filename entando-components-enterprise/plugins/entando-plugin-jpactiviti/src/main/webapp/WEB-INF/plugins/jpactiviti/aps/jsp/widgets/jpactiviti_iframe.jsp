<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="awp" uri="/jpactiviti-aps-core" %>
<wp:headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />

<c:set var="javascript_iframe_code">
	jQuery(document).ready(function(){
	jQuery('#login').attr('target','iframe');
		jQuery('#login').submit();
	});
</c:set>
	<wp:info key="systemParam" paramName="jpactiviti_activiti_baseUrl" />			
<wp:headInfo type="JS_RAW" info="${javascript_iframe_code}" />
<form id="login"  method="post" action="<wp:info key="systemParam" paramName="jpactiviti_explorer_baseUrl" />/#<wp:currentWidget param="config" configParam="section" />/">
    <input type="hidden" name="username" value="${sessionScope.currentUser.username}" />
    <input type="hidden" name="password" value="${sessionScope.currentUser.password}" />
</form>
<iframe id="iframe" name="iframe" width="100%" height="<wp:currentWidget param="config" configParam="height" />">
	<p><wp:i18n key="jpactiviti_ERROR_NO_IFRAME" /></p>
</iframe>