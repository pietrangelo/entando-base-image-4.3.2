<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpjasper/static/css/jpjasper.css" />
<%--
	<pre>
	TYPE_CONSTANT:
	ic_type_boolean = 1;
	ic_type_single_value = 2;
	ic_type_single_select_list_of_values = 3;
	-- ic_type_single_select_query = 4;
	ic_type_multi_value = 5;
	ic_type_multi_select_list_of_values = 6;
	-- ic_type_multi_select_query = 7;
	ic_type_single_select_list_of_values_radio = 8;
	ic_type_single_select_query_radio = 9;
	ic_type_multi_select_list_of_values_checkbox = 10;
	ic_type_multi_select_query_checkbox = 11;

	---------------
	SHW:
	<s:textarea cols="105" rows="10" value="%{getShowletInputControlValues()}" />
	-------------
	FORM:
	<s:textarea cols="105" rows="10" value="%{getReportParams()}" />
	-------------
	</pre>
--%>
<div class="jpjasper-report-input">
	<h2 class="title"><s:property value="showletTitleParam" /></h2>
	<s:if test="hasActionErrors()">
		<div class="alert alert-error">
			<h2><wp:i18n key="ERRORS" /></h2>
			<ul class="unstyled">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="%{null != currentReport.getInputControls()}">
		<form
			action="<wp:action path="/ExtStr2/do/jpjasper/FrontEnd/Report/renderReport.action" />"
			method="post"
			class="form-horizontal"
			>
			<s:set var="report_input_control_v2_map" value="%{getReportInputControlsConfigMap()}" />
			<s:iterator var="report_input_control" value="currentReport.inputControls">
				<%--
					per ogni elemento della lista (per mantenere l'ordinamento)
					deve renderizzare il "modulo" se
					- il valore è nullo (all'inteno della configurazione della showlet) (altrimenti deve essere sempre visibile)
					oppure
					- è dichiarato frontend override
				--%>
				<s:set var="current_ICC_config" value="%{getInputControlConfig(#report_input_control.name)}" />
				<s:if test="null != #current_ICC_config">
					<div class="control-group">
						<s:set var="input_control_module_type" value="#report_input_control.getProperty('PROP_INPUTCONTROL_TYPE').value" />
						<s:set  var="report_input_control_v2" value="#report_input_control_v2_map[#report_input_control.name]" />
						<s:if test="%{#input_control_module_type == \"1\" }">
							<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/1_ic_type_boolean.jsp"></s:include>
						</s:if>
						<s:elseif test="%{#input_control_module_type == \"2\"}">
							<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/2_ic_type_single_value.jsp"></s:include>
						</s:elseif>
						<s:elseif test="%{#input_control_module_type == \"3\" || #input_control_module_type == \"4\"}">
							<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/3-4_ic_type_single_select.jsp"></s:include>
						</s:elseif>
						<%-- 5 --%>
						<s:elseif test="%{#input_control_module_type == \"6\" || #input_control_module_type == \"7\"}">
							<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/6-7_ic_type_multi_select.jsp"></s:include>
						</s:elseif>
						<s:elseif test="%{#input_control_module_type == \"8\" || #input_control_module_type == \"9\"}">
							<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/8-9_ic_type_single_select_list_of_values_radio.jsp"></s:include>
						</s:elseif>

						<s:elseif test="%{#input_control_module_type == \"10\"}">
							<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/10_ic_type_multi_select_list_of_values_checkbox.jsp"></s:include>
						</s:elseif>
						<s:else>
							<p>
								The input type "<s:property value="#input_control_module_type" />" hasn't been implemented yet.
							</p>
						</s:else>
					</div>
					<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc_report_input_control_detail.jsp" />
				</s:if>
			</s:iterator>
			<div class="control-group">
				<div class="controls">
					<s:hidden name="uriString" value="%{actionUriString}" />
					<s:hidden name="showForm" value="0" />

					<s:if test="null != showletDownloadFormats && showletDownloadFormats.size > 0">
						<s:iterator var="currentFormat" value="showletDownloadFormats">
							<s:hidden name="downloadFormats" value="%{#currentFormat}" />
						</s:iterator>
					</s:if>

					<s:set var="btn_value"><wp:i18n key="jpjasper_RENDER_REPORT" /></s:set>
					<wpsf:submit cssClass="btn btn-primary" useTabindexAutoIncrement="false" action="renderReport" value="%{#btn_value}" />
				</div>
			</div>
		</form>
	</s:if>
</div>