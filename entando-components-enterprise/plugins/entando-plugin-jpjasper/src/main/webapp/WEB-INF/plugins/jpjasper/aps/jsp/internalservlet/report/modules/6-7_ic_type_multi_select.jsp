<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="state" value="#report_input_control_v2.state" />
<div class="jpjasper-input-block-6-7_ic_type_multi_select">
<label class="control-label">
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#report_input_control_v2.listValue" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride" value="<s:property value="#current_ICC_config.frontEndOverride"/>" />
	<s:property value="#report_input_control_v2.label" />
	<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc/mandatory-mark.jsp" />
</label>
	<%--
		<select id="<s:property value="#state.id" />"
		name="formInputControlConfigMap.<s:property value="#state.id" />.value"
		multiple="multiple">
			<s:iterator var="option" value="#state.options">
				<option value="<s:property value="#option.value" />" <s:if test="#option.selected">selected="selected"</s:if>><s:property value="#option.label" /></option>
			</s:iterator>
		</select>
	 --%>
	<div class="controls">
		<s:iterator var="option" value="#state.options" status="status">
			<label
				for="jpjasper-input-<s:property value="#state.id" />-<s:property value="#status.count" />-<s:property value="%{#attr.random}" />"
				class="checkbox">
					<input
						type="checkbox"
						name="formInputControlConfigMap.<s:property value="#state.id" />.value"
						value="<s:property value="#option.value" />"
						<s:if test="#option.selected"> checked="checked" </s:if>
						id="jpjasper-input-<s:property value="#state.id" />-<s:property value="#status.count" />-<s:property value="%{#attr.random}" />"
						/>
					<s:property value="#option.label" />
			</label>
		</s:iterator>
		<s:if test="null != #report_input_control_v2.slaveDependencies && !#report_input_control_v2.slaveDependencies.empty">
			<wpsa:actionParam action="renderReport" var="actionName" >
				<wpsa:actionSubParam name="refreshedControlId" value="%{#report_input_control_v2.id}" />
			</wpsa:actionParam>
			<s:set var="btn_value"><wp:i18n key="jpjasper_REFRESH_INPUT_CONTROLS" /></s:set>
			<wpsf:submit cssClass="btn btn-default" action="%{#actionName}"  value="%{#btn_value}" />
		</s:if>
	</div>
</div>