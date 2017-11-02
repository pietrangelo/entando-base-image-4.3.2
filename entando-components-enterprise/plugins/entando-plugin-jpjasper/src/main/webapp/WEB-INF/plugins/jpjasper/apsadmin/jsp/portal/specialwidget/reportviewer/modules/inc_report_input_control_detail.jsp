<%-- debug data
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<pre>
TYPE:			<s:property value="#report_input_control_v2.type"/>
MASTER_DEPEND:		<s:property value="#report_input_control_v2.masterDependencies"/>
SLAVE_DEPEND:		<s:property value="#report_input_control_v2.slaveDependencies"/>
id:			<s:property value="#report_input_control_v2.id"/>
label:			<s:property value="#report_input_control_v2.label"/>
mandatory:		<s:property value="#report_input_control_v2.mandatory"/>
readOnly:		<s:property value="#report_input_control_v2.readOnly"/>
visible:		<s:property value="#report_input_control_v2.visible"/>
</pre>
--%>