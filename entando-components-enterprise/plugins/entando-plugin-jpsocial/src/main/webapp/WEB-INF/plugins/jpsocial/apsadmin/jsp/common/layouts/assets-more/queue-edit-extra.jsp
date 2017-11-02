<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:set var="myClient"><wpsa:backendGuiClient /></s:set>
<s:if test="#myClient == 'normal'">
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpsocial/administration/common/css/jpsocial-administration.css" media="screen" />
</s:if>
<s:if test="#myClient == 'advanced'">
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpsocial/administration/common/css/jpsocial-administration.css" media="screen" />
</s:if>