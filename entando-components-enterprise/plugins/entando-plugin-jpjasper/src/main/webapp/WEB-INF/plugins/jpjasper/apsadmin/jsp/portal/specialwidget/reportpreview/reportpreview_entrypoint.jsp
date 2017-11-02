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

        <s:form action="search" namespace="/do/jpjasper/Page/SpecialWidget/ReportPreviewConfig">
            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
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
                    <fieldset class="col-xs-12">
                        <legend><s:text name="jpjasper.joinwidget.reportpreview.preview" /></legend>
                        <s:if test="showlet.config['uriString'] != null">
                            <s:set var="currentReport" value="%{getReport(showlet.config['uriString'])}" />
                            <s:if test="null != #currentReport">
                                <p class="noscreen">
                                    <wpsf:hidden name="uriString" value="%{showlet.config['uriString']}" />
                                    <wpsf:hidden name="inputControls" value="%{showlet.config['inputControls']}" />
                                    <%-- PER ANTEPRIMA NO
                                    <s:iterator value="downloadFormats" var="dwnFromat">
                                            <wpsf:hidden name="downloadFormats" value="%{#dwnFromat}" />
                                    </s:iterator>
                                    --%>
                                </p>
                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/inc/current-report-info.jsp" />
                            </s:if>
                            <s:else>
                                <div class="alert alert-info"><s:text name="jpjasper.note.report.not.found" /></div>
                            </s:else>
                            <div class="form-group">

                                <wpsa:actionParam action="entryReportConfig" var="actionName" >
                                    <wpsa:actionSubParam name="mainReport" value="true" />
                                </wpsa:actionParam>
                                <wpsf:submit 
                                    action="%{#actionName}"
                                    value="%{getText('jpjasper.joinwidget.reportpreview.button.configure.preview')}"
                                    cssClass="btn btn-default"
                                    />
                            </div>
                        </s:if>
                        <s:else>
                            <div class="form-group">
                                <wpsa:actionParam action="search" var="actionName" >
                                    <wpsa:actionSubParam name="mainReport" value="true" />
                                </wpsa:actionParam>
                                <wpsf:submit 
                                    action="%{#actionName}"
                                    value="%{getText('jpjasper.joinwidget.reportpreview.button.configure.preview')}"
                                    cssClass="btn btn-default" />
                            </div>
                        </s:else>
                    </fieldset>
                    <fieldset class="col-xs-12">
                        <legend><s:text name="jpjasper.joinwidget.reportpreview.detail" /></legend>
                        <s:if test="showlet.config['uriStringDett'] != null">
                            <s:set var="currentReport" value="%{getReport(showlet.config['uriStringDett'])}" />
                            <s:if test="null != #currentReport">
                                <p class="noscreen">
                                    <wpsf:hidden name="uriStringDett" value="%{showlet.config['uriStringDett']}" />
                                    <wpsf:hidden name="inputControlsDett" value="%{showlet.config['inputControlsDett']}" />
                                    <s:iterator value="downloadFormatsDett" var="dwnFromatDett">
                                        <wpsf:hidden name="downloadFormatsDett" value="%{#dwnFromatDett}" />
                                    </s:iterator>
                                    <s:iterator id="lang" value="langs">
                                        <wpsf:hidden name="%{'titleDett_' + #lang.code}" value="%{showlet.config.get('titleDett_' + #lang.code)}" />
                                    </s:iterator>
                                </p>
                                <s:include value="/WEB-INF/plugins/jpjasper/apsadmin/jsp/portal/specialwidget/reportviewer/inc/current-report-info.jsp" />
                            </s:if>
                            <s:else>
                                <div class="alert alert-info">
                                    Configured report was not found within the Jasper Server.
                                </div>
                            </s:else>
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="col-xs-12 btn-toolbar">
                                        <wpsa:actionParam action="entryReportConfig" var="actionName" >
                                            <wpsa:actionSubParam name="mainReport" value="false" />
                                        </wpsa:actionParam>
                                        <wpsf:submit  cssClass="btn btn-default" action="%{#actionName}"  value="%{getText('label.configure')}" />
                                        &#32;
                                        <wpsa:actionParam action="trashDetailReportConfig" var="actionName" >
                                            <wpsa:actionSubParam name="mainReport" value="false" />
                                        </wpsa:actionParam>

                                        <wpsf:submit type="button" cssClass="btn btn-warning margin-small-left text-center" action="%{#actionName}">
                                            <span class="icon fa fa-times-circle"></span>&#32;
                                            <s:text name="%{getText('label.remove')}"/>
                                        </wpsf:submit>

                                    </div>
                                </div>
                            </div>
                        </s:if>
                        <s:else>
                            <div class="form-group">
                                <wpsa:actionParam action="search" var="actionName" >
                                    <wpsa:actionSubParam name="mainReport" value="false" />
                                </wpsa:actionParam>
                                <wpsf:submit

                                    cssClass="btn btn-default"
                                    action="%{#actionName}"
                                    value="%{getText('jpjasper.joinwidget.reportpreview.button.configure.detail')}"
                                    />
                            </div>
                        </s:else>
                    </fieldset>
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

