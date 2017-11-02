<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="state" value="#report_input_control_v2.state" />
<div class="form-group">
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#report_input_control_v2.listValue" />" />
	<label for="jasper-report-param-value-<s:property value="#state.id" />">
		<s:property value="#report_input_control_v2.label" />
		<s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/inc/parameter-info.jsp" />
	</label>
	<select class="form-control" id="jasper-report-param-value-<s:property value="#state.id" />" name="formInputControlConfigMap.<s:property value="#state.id" />.value">
		<s:iterator var="option" value="#state.options">
			<option value="<s:property value="#option.value" />" <s:if test="#option.selected">selected="selected"</s:if>><s:property value="#option.label" /></option>
		</s:iterator>
	</select>
</div>
<s:if test="#displayFrontendOverrideVar">
<div class="form-group">
    <div class="checkbox">
	<input
		class="radiocheck"
		type="checkbox"
		id="jasper-report-<s:property value="%{#state.id}" />-FO"
		name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride"
		<s:if test="formInputControlConfigMap[#state.id].frontEndOverride==\"true\""> checked="checked" </s:if>
		value="true"  />
		&#32;
	<label for="jasper-report-<s:property value="%{#state.id}" />-FO"><s:text name="jpjasper.label.frontend.override" />&#32;<em><s:property value="#report_input_control_v2.label"/></em></label>
    </div>
</div>
</s:if>
<s:if test="null != #report_input_control_v2.slaveDependencies && !#report_input_control_v2.slaveDependencies.empty">
<div class="form-group">
	<wpsa:actionParam action="refreshInputControls" var="actionName" >
		<wpsa:actionSubParam name="refreshedControlId" value="%{#report_input_control_v2.id}" />
	</wpsa:actionParam>
	<wpsf:submit class="btn btn-default" action="%{#actionName}"  value="%{getText('jpjasper.button.refresh.inputcontrol')}" />
</div>
</s:if>