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

        <s:form action="search" namespace="/do/jpjasper/Page/SpecialWidget/ReportPreviewConfig">
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
            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
                </div>
                <div class="panel-body">
                    <h2 class="h5 margin-small-vertical">
                        <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                        <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                    </h2>
                    <p>
                        <wpsf:hidden name="pageCode" />
                        <wpsf:hidden name="frame" />
                        <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />

                        <wpsf:hidden name="uriString" />
                        <wpsf:hidden name="inputControls"  />


                        <wpsf:hidden name="uriStringDett"  />
                        <wpsf:hidden name="inputControlsDett" value="%{showlet.config['inputControlsDett']}" />

                        <s:iterator value="downloadFormatsDett" var="dwnFromatDett">
                            <wpsf:hidden name="downloadFormatsDett" value="%{#dwnFromatDett}" />
                        </s:iterator>

                        <s:iterator id="lang" value="langs">
                            <wpsf:hidden name="%{'titleDett_' + #lang.code}" value="%{showlet.config.get('titleDett_' + #lang.code)}" />
                        </s:iterator>

                        <wpsf:hidden name="mainReport"  />

                    </p>
                    <div class="alert alert-warning">
                        <wpsa:actionParam action="deleteDetailReportConfig*" var="actionName" >
                            <wpsa:actionSubParam name="mainReport" value="true" />
                        </wpsa:actionParam>
                        <s:set var="detailReportToDelete" value="%{getReport(uriStringDett)}" />
                        <s:text name="jpjasper.note.removing" />&#32;<em><s:property value="#detailReportToDelete.name" /></em>.
                        <div class="text-center margin-large-top">
                            <wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-warning btn-lg" >
                                <span class="icon fa fa-times-circle"></span>&#32;
                                <s:text name="%{getText('jpjasper.button.remove.ok')}" />
                            </wpsf:submit>
                        </div>    
                        <p class="text-center margin-small-top">
                            <wpsa:actionParam action="entryReport" var="actionName" >
                                <wpsa:actionSubParam name="mainReport" value="true" />
                            </wpsa:actionParam>
                            <wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-link">
                                <s:text name="%{getText('jpjasper.button.remove.no.goback')}"/>
                            </wpsf:submit>


                        </p>
                    </div>
                </div>
            </div>
        </s:form>



    </div>
</div>
