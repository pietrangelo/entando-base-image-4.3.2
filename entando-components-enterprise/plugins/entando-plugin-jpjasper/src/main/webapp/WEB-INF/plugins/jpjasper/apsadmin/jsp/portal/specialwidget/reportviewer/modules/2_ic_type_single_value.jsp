<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<div class="form-group">
	<s:set var="state" value="#report_input_control_v2.state" />
	<label for="jasper-report-param-value-<s:property value="#state.id" /><s:if test="#report_input_control_v2.type =='singleValueDate'">_cal</s:if>">
		<s:property value="#report_input_control_v2.label" />
		<s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/inc/parameter-info.jsp" />
	</label>
	<s:if test="#report_input_control_v2.type =='singleValueDate'">
		<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
		<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#control.listValue" />" />
		<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.dataType" value="3" />
		<input
			class="form-control"
			type="text"
			id="jasper-report-param-value-<s:property value="#state.id" />_cal"
			name="formInputControlConfigMap.<s:property value="#state.id" />.value"
			value="<s:property value="#state.value" />" class="input-date"/>
	</s:if>
	<s:elseif test="#report_input_control_v2.type =='singleValueNumber'">
		<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
		<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#control.listValue" />" />
		<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.dataType" value="2" />
		<input type="text" class="form-control" id="jasper-report-param-value-<s:property value="#state.id" />" name="formInputControlConfigMap.<s:property value="#state.id" />.value" value="<s:property value="#state.value" />" />
	</s:elseif>
	<s:else>
            <div class="alert alert-info">&#32;Type <em><s:property value="#report_input_control_v2.type"/></em> Not Implemented Yet.</div>
	</s:else>
</div>
<s:if test="#displayFrontendOverrideVar">
<div class="form-group">
    <div class="checkbox">
	<input
		class="radiocheck"
		id="jasper-report-<s:property value="%{#state.id}" />-FO"
		type="checkbox"
		name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride"
		<s:if test="formInputControlConfigMap[#state.id].frontEndOverride==\"true\""> checked="checked" </s:if>
		value="true"  />
	<label for="jasper-report-<s:property value="%{#state.id}" />-FO"><s:text name="jpjasper.label.frontend.override" />&#32;<em><s:property value="#report_input_control_v2.label"/></em></label>
    </div>
</div>
</s:if>