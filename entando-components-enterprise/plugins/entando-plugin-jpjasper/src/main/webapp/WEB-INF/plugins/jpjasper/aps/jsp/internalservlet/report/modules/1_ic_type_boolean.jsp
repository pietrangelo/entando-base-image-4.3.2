<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="state" value="#report_input_control_v2.state" />
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<div class="control-groups jpjasper-input-block-1_ic_type_boolean">
	<div class="controls">
		<label
			class="checkbox"
			for="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />"
			>
			<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
			<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#report_input_control_v2.listValue" />" />
			<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride" value="<s:property value="#current_ICC_config.frontEndOverride"/>" />
			<input
				type="checkbox"
				id="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />"
				name="formInputControlConfigMap.<s:property value="#state.id" />.value"
				value="<s:property value="#state.value" />"
				<s:if test="#state.value==\"true\""> checked="checked" </s:if>
				/>
			<s:property value="#report_input_control_v2.label" />
			<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc/mandatory-mark.jsp" />
		</label>
	</div>
</div>