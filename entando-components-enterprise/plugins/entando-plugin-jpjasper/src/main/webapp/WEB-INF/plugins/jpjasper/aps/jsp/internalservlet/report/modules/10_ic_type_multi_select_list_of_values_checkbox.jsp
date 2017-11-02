<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<s:set var="state" value="#report_input_control_v2.state" />
<div class="control-group jpjasper-input-block-10_ic_type_multi_select_list_of_values_checkbox">
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#report_input_control_v2.listValue" />" />
	<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride" value="<s:property value="#current_ICC_config.frontEndOverride"/>" />
	<label class="control-label">
		<s:property value="#report_input_control_v2.label" />
		<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc/mandatory-mark.jsp" />
	</label>
	<div class="controls">
		<s:iterator var="option" value="#state.options" status="status">
			<s:set var="check_this_check" value="#option.selected && formInputControlConfigMap[#state.id].value.contains(#option.value)" />
			<label
				class="checkbox"
				for="jpjasper-input-<s:property value="#state.id" />-<s:property value="#status.count" />-<s:property value="#attr.random" />"
				>
				<input
					type="checkbox"
					id="jpjasper-input-<s:property value="#state.id" />-<s:property value="#status.count" />-<s:property value="#attr.random" />"
					name="formInputControlConfigMap.<s:property value="#state.id" />.value"
					value="<s:property value="#option.value" />"
					<s:if test="#check_this_check"> checked="checked" </s:if>
					/>
				<s:property value="#option.label" />
			</label>
		</s:iterator>
	</div>
</div>