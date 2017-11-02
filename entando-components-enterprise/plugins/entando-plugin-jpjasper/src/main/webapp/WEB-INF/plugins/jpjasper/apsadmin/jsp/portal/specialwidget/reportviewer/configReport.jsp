<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block"> 
        <a href="<s:url action="viewTree" namespace="/do/Page" />" 
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>&#32;/&#32;
        <s:text name="title.configPage" />
    </span>
</h1>
<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="save" namespace="/do/jpjasper/Page/SpecialWidget/ReportConfig" cssClass="jpjasper-form">
            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
                <wpsf:hidden name="uriString" />
            </p>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
                </div>
                <div class="panel-body">
                    <h2 class="h5 margin-small-vertical">
                        <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                        <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                    </h2>
                    <s:if test="hasFieldErrors()">
                        <div class="alert alert-danger alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                            <h4 class="margin-none"><s:text name="message.title.FieldErrors" /></h4>
                            <ul class="margin-base-vertical">
                                <s:iterator value="fieldErrors">
                                    <s:iterator value="value">
                                        <li><s:property escape="false" /></li>
                                        </s:iterator>
                                    </s:iterator>
                            </ul>
                        </div>
                    </s:if>
                    <s:if test="hasActionErrors()">
                        <div class="alert alert-danger alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                            <h4 class="margin-none"><s:text name="message.title.ActionErrors" /></h4>
                            <ul class="margin-base-vertical">
                                <s:iterator value="actionErrors">
                                    <li><s:property escape="false" /></li>
                                    </s:iterator>
                            </ul>
                        </div>
                    </s:if>

                    <s:set var="currentReport" value="%{getReport(showlet.config['uriString'])}" />
                    <s:if test="null != #currentReport">
                        <div class="form-group">
                            <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/inc/current-report-info.jsp" />
                        </div>
                        <div class="form-group">
                            <wpsa:actionParam action="search" var="actionName" >
                            </wpsa:actionParam>
                            <wpsf:submit action="%{#actionName}"   value="%{getText('jpjasper.button.changeReport')}" cssClass="btn btn-default"  />
                        </div>

                        <div class="subsection">
                            <fieldset class="col-xs-12">
                                <legend><s:text name="label.info" /></legend>
                                <s:iterator value="langs" var="lang">
                                    <div class="form-group">
                                        <label class="control-label" for="title_<s:property value="#lang.code" />" >
                                            <code class="label label-info"><s:property value="#lang.code" /></code>
                                            <s:text name="label.title" /></label>
                                            <wpsf:textfield  name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{showlet.config.get('title_' + #lang.code)}" cssClass="form-control" />
                                    </div>
                                </s:iterator>
                            </fieldset>
                            <fieldset class="col-xs-12">
                                <legend><s:text name="jpjasper.parameter.config" /></legend>
                                <s:if test="%{null != #currentReport.getInputControls()}">
                                    <s:set var="report_input_control_v2_map" value="%{getReportInputControlsConfigMap()}" />
                                    <s:set var="displayFrontendOverrideVar" value="%{isFrontendOverrideParamEnabled()}" />
                                    <s:iterator var="report_input_control" value="#currentReport.inputControls">
                                        <div class="report-input-control-item">
                                            <s:set var="input_control_module_type" value="#report_input_control.getProperty('PROP_INPUTCONTROL_TYPE').value" />
                                            <property value="input_control_module_type"/>
                                            <s:set  var="report_input_control_v2" value="#report_input_control_v2_map[#report_input_control.name]" />
                                            <s:if test="%{#input_control_module_type == \"1\" }">
                                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/1_ic_type_boolean.jsp"></s:include>
                                            </s:if>
                                            <s:elseif test="%{#input_control_module_type == \"2\"}">
                                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/2_ic_type_single_value.jsp"></s:include>
                                            </s:elseif>
                                            <s:elseif test="%{#input_control_module_type == \"3\" || #input_control_module_type == \"4\"}">
                                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/3-4_ic_type_single_select.jsp"></s:include>
                                            </s:elseif>
                                            <%-- 5 --%>
                                            <s:elseif test="%{#input_control_module_type == \"6\" || #input_control_module_type == \"7\"}">
                                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/6-7_ic_type_multi_select.jsp"></s:include>
                                            </s:elseif>
                                            <s:elseif test="%{#input_control_module_type == \"8\" || #input_control_module_type == \"9\"}">
                                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/8-9_ic_type_single_select_list_of_values_radio.jsp"></s:include>
                                            </s:elseif>
                                            <s:elseif test="%{#input_control_module_type == \"10\"}">
                                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/modules/10_ic_type_multi_select_list_of_values_checkbox.jsp"></s:include>
                                            </s:elseif>
                                            <s:else>
                                                <div class="alert alert-info">
                                                    The <em><s:property value="#input_control_module_type" /></em> module hasn't been implemented yet.
                                                </div>
                                            </s:else>
                                        </div>
                                    </s:iterator>
                                </s:if>
                                <div class="report-input-control-item last">
                                    <p>
                                        <span class="important"><s:text name="jpjasper.label.downloadFormat" />:</span>
                                    </p>
                                    <%--
                                    <s:checkboxlist name="downloadFormats" list="reportDownloadFormats" listKey="key" listValue="value.extension" cssClass="noBullet radiocheck" />
                                    --%>
                                    <s:iterator value="%{reportDownloadFormats}" var="opt" status="status">
                                        <div class="form-group">
                                            <div class="checkbox">
                                                <input
                                                    id="jasper-downloadFormats-<s:property value="%{#status.count}" />"
                                                    name="downloadFormats"
                                                    tabindex="<wpsa:counter />"
                                                    value="<s:property value="%{#opt.value.extension}" />"
                                                    type="checkbox"
                                                    <s:if test="%{downloadFormats.contains(#opt.value.extension)}">checked="checked"</s:if>
                                                        />
                                                    <label for="jasper-downloadFormats-<s:property value="%{#status.count}" />"><s:property value="#opt.value.extension" /></label>
                                            </div>
                                        </div>
                                    </s:iterator>
                                    </ul>
                                </div>
                            </fieldset>
                        </div>
                    </s:if>
                    <s:else>
                        <div class="alert alert-info">
                            <s:text name="jpjasper.note.report.not.found" />
                        </div>
                    </s:else>
                    <div class="form-horizontal">        
                        <div class="form-group">
                            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                                <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="saveConfig">
                                    <span class="icon fa fa-floppy-o"></span>&#32;
                                    <s:text name="%{getText('label.save')}"/>
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </s:form>

    </div>
</div>