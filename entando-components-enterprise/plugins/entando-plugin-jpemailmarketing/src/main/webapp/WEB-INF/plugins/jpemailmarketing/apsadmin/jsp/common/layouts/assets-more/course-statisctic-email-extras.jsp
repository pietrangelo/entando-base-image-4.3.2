<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpemailmarketing/administration/css/jpemailmarketing-administration.css">

<script>
jQuery(function(){
	$('[data-toggle="tooltip"]').tooltip();
})
</script>
