<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
&#32;
<s:if test="#report_input_control_v2.mandatory">
	<abbr title="Mandatory">M</abbr>&#32;
</s:if>
<s:if test="#report_input_control_v2.readOnly">
	<abbr title="Read Only">R</abbr>
</s:if>
<s:if test="#report_input_control_v2.visible">
	<abbr title="Visible">V</abbr>
</s:if>

