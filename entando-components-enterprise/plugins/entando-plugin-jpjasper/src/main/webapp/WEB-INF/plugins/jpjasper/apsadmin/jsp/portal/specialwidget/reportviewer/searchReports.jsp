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

        <s:form action="search">
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
            <p>
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode"  />
                <wpsf:hidden name="mainReport"  />
                <s:if test="mainReport">
                    <wpsf:hidden name="uriStringDett"  />
                </s:if>
                <s:else>
                    <wpsf:hidden name="uriString"  />
                </s:else>
                <wpsf:hidden name="inputControls"  />
                <wpsf:hidden name="inputControlsDett"  />
                <s:iterator value="downloadFormats" var="dwnFromat">
                    <wpsf:hidden name="downloadFormats" value="%{#dwnFromat}" />
                </s:iterator>
                <s:iterator value="downloadFormatsDett" var="dwnFromatDett">
                    <wpsf:hidden name="downloadFormatsDett" value="%{#dwnFromatDett}" />
                </s:iterator>
                <s:iterator id="lang" value="langs">
                    <wpsf:hidden name="%{'titleDett_' + #lang.code}" value="%{showlet.config.get('titleDett_' + #lang.code)}" />
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
                    <div class="form-group">
                        <div class="col-xs-12">
                            <label for="jasperreport-search-text">
                                <s:text name="label.search.by"/>&#32;<s:text name="jpjasper.label.search.report"/></label>
                            <div class="input-group">
                                <wpsf:textfield  name="descr" id="text" cssClass="form-control" />
                                <span class="input-group-btn">              
                                    <wpsf:submit  type="button" action="search" value="Search" cssClass="btn btn-primary">
                                        <span class="sr-only">Search</span>
                                        <span class="icon fa fa-search"></span>
                                    </wpsf:submit>
                                </span>
                            </div>
                        </div>
                    </div>
                    <s:set var="listReports" value="reports" />
                    <wpsa:subset source="#listReports" count="10" objectName="listReportGroup" advanced="true" offset="5">
                        <s:set name="group" value="#listReportGroup" />
                        <div class="pager">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12">
                                <table class="table table-bordered">
                                    <tr>
                                        <th><s:text name="jpjasper.label.report.label" /></th>
                                        <th><s:text name="jpjasper.label.report.description" /></th>
                                        <th class="text-center"><s:text name="jpjasper.label.report.date" /></th>
                                        <th><s:text name="jpjasper.label.report.uri" /></th>
                                    </tr>
                                    <s:iterator var="jasperResourceDescriptor" status="status">
                                        <tr>
                                            <td><input id="radio-report-<s:property value="#status.count" />" type="radio" name="uriString<s:if test="!mainReport">Dett</s:if>" value="<s:property value="#jasperResourceDescriptor.uriString" />">&nbsp;<label for="radio-report-<s:property value="#status.count" />" title="<s:property value="#jasperResourceDescriptor.name" />"><s:property value="#jasperResourceDescriptor.label" /></label></td>
                                            <td><s:property value="#jasperResourceDescriptor.description" /></td>
                                            <%-- <td><s:property value="#jasperResourceDescriptor.propertyes['PROP_PARENT_FOLDER']" /></td>  --%>
                                            <td class="text-center"><code><s:date name="#jasperResourceDescriptor.creationDate" format="dd/MM/yyyy HH:mm" /></code></td>
                                            <td><s:property value='#jasperResourceDescriptor.uriString.replaceAll("/"," / ")' /></td>
                                        </tr>
                                    </s:iterator>
                                </table>
                            </div>
                        </div>
                        <div class="pager">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                        </div>
                    </wpsa:subset>
                    <div class="form-horizontal">        
                        <div class="form-group">
                            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                                <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="configReportUriString">
                                    <s:text name="%{getText('label.confirm')}"/>
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </s:form>
    </div>
</div>