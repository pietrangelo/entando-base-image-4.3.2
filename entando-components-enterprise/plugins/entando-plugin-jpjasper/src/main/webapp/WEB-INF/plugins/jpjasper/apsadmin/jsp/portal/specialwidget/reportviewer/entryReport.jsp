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
</h1>

<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="search" namespace="/do/jpjasper/Page/SpecialWidget/ReportConfig">
            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
                <s:iterator id="lang" value="langs">
                    <wpsf:hidden name="%{'title_' + #lang.code}" value="%{showlet.config.get('title_' + #lang.code)}" />
                </s:iterator>

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
                    <s:if test="showlet.config['uriString'] != null">
                        <s:set var="currentReport" value="%{getReport(showlet.config['uriString'])}" />
                        <s:if test="null != #currentReport">
                            <div class="form-group">
                                <table class="table table-bordered">
                                    <tr>
                                        <th>Label</th>
                                        <td><s:property value="#currentReport.label"/></td>
                                    </tr>
                                    <tr>
                                        <th>Description</th>
                                        <td><s:property value="#currentReport.getDescription()"/></td>        
                                    </tr>                
                                    <tr>
                                        <th>Name</th>
                                        <td><s:property value="#currentReport.name"/></td>
                                    </tr>              
                                </table>
                            </div>

                            <p>
                                <wpsf:hidden name="uriString" value="%{showlet.config['uriString']}" />
                                <wpsf:hidden name="inputControls" value="%{showlet.config['inputControls']}" />
                                <s:iterator value="downloadFormats" var="dwnFromat">
                                    <wpsf:hidden name="downloadFormats" value="%{#dwnFromat}" />
                                </s:iterator>
                            </p>
                            <div class="form-group">
                                <wpsf:submit  action="entryReportConfig" value="Configure" cssClass="btn btn-default" />
                            </div>
                        </s:if>
                        <s:else>
                            <div class="form-group">
                                <div class="alert alert-info">The previously configured Report was not found within JasperServer.</div>					</div>
                                <wpsf:submit  type="button" action="search" value="Change Report" cssClass="btn btn-default" />
                        </div>
                    </s:else>
                </s:if>
                <s:else>

                    <div class="form-group">
                        <div class="alert alert-info"><s:text name="jpjasper.joinwidget.note.choose" /></div>
                        <div class="btn-group">
                            <wpsf:submit  type="button" action="search" value="%{getText('jpjasper.joinwidget.button.configure')}" cssClass="btn btn-default" />
                            <wpsf:submit  type="button" action="saveConfig" value="%{getText('jpjasper.joinwidget.button.configureOnTheFlyPublishing')}"  cssClass="btn btn-default"/>
                        </div>
                    </div>

                </s:else>
            </div>
        </div>
    </s:form>
</div>
</div>
