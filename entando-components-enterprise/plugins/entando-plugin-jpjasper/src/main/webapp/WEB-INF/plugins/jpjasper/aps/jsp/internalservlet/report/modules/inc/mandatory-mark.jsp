<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#report_input_control_v2.mandatory">
	<abbr title="<wp:i18n key="jpjasper_MANDATORY_CONTROL" />" class="icon icon-asterisk"><span class="noscreen sr-only">*</span></abbr>
</s:if>