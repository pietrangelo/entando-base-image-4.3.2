<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="state" value="#report_input_control_v2.state" />
<label class="control-label jpjapser-input-block-3-4_ic_type_single_select" for="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />">
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#report_input_control_v2.listValue" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride" value="<s:property value="#current_ICC_config.frontEndOverride"/>" />
	<s:property value="#report_input_control_v2.label" />
	<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc/mandatory-mark.jsp" />
</label>
<div class="controls">
	<select
		id="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />"
		name="formInputControlConfigMap.<s:property value="#state.id" />.value"
		>
		<s:iterator var="option" value="#state.options">
			<option
				value="<s:property value="#option.value" />"
				<s:if test="#option.selected"> selected="selected" </s:if>
				>
					<s:property value="#option.label" />
			</option>
		</s:iterator>
	</select>
	<s:if test="null != #report_input_control_v2.slaveDependencies && !#report_input_control_v2.slaveDependencies.empty">
			<wpsa:actionParam action="renderReport" var="actionName" >
				<wpsa:actionSubParam name="refreshedControlId" value="%{#report_input_control_v2.id}" />
			</wpsa:actionParam>
			<s:set var="btn_value"><wp:i18n key="jpjasper_REFRESH_INPUT_CONTROLS" /></s:set>
			<wpsf:submit cssClass="btn btn-default" action="%{#actionName}"  value="%{#btn_value}" />
	</s:if>
</div>