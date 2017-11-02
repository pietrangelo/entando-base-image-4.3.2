<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/inc_report_input_control_detail.jsp"></s:include>
<s:set var="state" value="#report_input_control_v2.state" />
<div class="form-group">
    <div class="checkbox">
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#report_input_control_v2.listValue" />" />
	<label for="jasper-report-param-value-<s:property value="#state.id" />">
		<s:property value="#report_input_control_v2.label" />
		<s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/inc/parameter-info.jsp" />
	</label>
	<input
		type="checkbox"
		id="jasper-report-param-value-<s:property value="#state.id" />"
		name="formInputControlConfigMap.<s:property value="#state.id" />.value"
		value="<s:property value="#state.value" />"
		<s:if test="#state.value==\"true\""> checked="checked" </s:if>
		/>
    </div>
</div>
<s:if test="#displayFrontendOverrideVar">
<div class="form-group">
    <div class="checkbox">
        <input class="radiocheck" id="jasper-report-<s:property value="%{#state.id}"/>" type="checkbox" name="formInputControlConfigMap.<s:property value="%{#state.id}"/>.frontEndOverride" <s:if test="formInputControlConfigMap[#state.id].frontEndOverride==\"true\""> checked="checked"</s:if>  value="true"  />
	<label for="jasper-report-<s:property value="%{#state.id}" />-FO"><s:text name="jpjasper.label.frontend.override" />&#32;<em><s:property value="#report_input_control_v2.label"/></em></label>
    </div>
</div>
</s:if>