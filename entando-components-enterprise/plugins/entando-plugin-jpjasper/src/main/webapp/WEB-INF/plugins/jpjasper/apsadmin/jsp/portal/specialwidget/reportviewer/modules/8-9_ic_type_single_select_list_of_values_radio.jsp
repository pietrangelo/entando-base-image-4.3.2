<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%--
<s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/inc_report_input_control_detail.jsp"></s:include>
--%>
<s:set var="state" value="#report_input_control_v2.state" />
<div class="form group">
    <input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
    <input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#report_input_control_v2.listValue" />" />
    <span class="important"><s:property value="#report_input_control_v2.label"/><s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/inc/parameter-info.jsp" />:</span>
</div>
<s:iterator var="option" value="#state.options" status="status">
    <%--
            the checkbox is checked when inputcontrol.state.option == selected (this comes from jasper)
            but the entando backoffice user can specify that this field has no values (in conjuncion with frontendOverride==true)

                        The checkbox is checked when inputcontrol.state.option == selected and the specific inputControlconfig contains the value
    --%>
    <s:set var="check_this_check" value="#option.selected && formInputControlConfigMap[#state.id].value.contains(#option.value)" />
    <div class="form-group">
        <div class="radio">
            <input
                type="radio"
                id="jasper-report-param-value-<s:property value="%{#state.id}" />-<s:property value="%{#status.count}" />"
                name="formInputControlConfigMap.<s:property value="#state.id" />.value"
                value="<s:property value="#option.value" />"
                <s:if test="#check_this_check"> checked="checked" </s:if>
                tabindex="<wpsa:counter />"
                />
            <label for="jasper-report-param-value-<s:property value="%{#state.id}" />-<s:property value="%{#status.count}" />">
                <s:property value="#option.label" />
            </label>
        </div>
    </div>
</s:iterator>

<s:if test="#displayFrontendOverrideVar">

    <div class="form group">
        <div class="checkbox">
            <input
                class="radiocheck"
                for="jasper-report-<s:property value="%{#state.id}" />-FO"
                type="checkbox"
                name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride"
                <s:if test="formInputControlConfigMap[#state.id].frontEndOverride==\"true\""> checked="checked" </s:if>
                    value="true"  />
                <label for="jasper-report-<s:property value="%{#state.id}" />-FO"><s:text name="jpjasper.label.frontend.override" />&#32;<em><s:property value="#report_input_control_v2.label"/></em></label>
        </div>
    </div>
</s:if>