<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a>&#32;/&#32;
        <s:text name="title.configPage" />
    </span>
</h1>

<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="saveConfig" namespace="/do/jpactiviti/Page/SpecialWidget/Activiti" cssClass="form-horizontal">
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
            <p class="sr-only">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}"/>
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
                            <label for="jpactiviti_height"><s:text name="jpactiviti.label.height"/></label>
                            <wpsf:textfield  name="height" id="jpactiviti_height" cssClass="form-control" />
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12">
                            <label for="jpactiviti_section"><s:text name="jpactiviti.label.section"/></label>
                            <wpsf:select id="jpactiviti_section" name="section" list="#@java.util.LinkedHashMap@{'process':getText('jpactiviti.label.process'), 'myprocess': getText('jpactiviti.label.myprocess'), 'processmodel': getText('jpactiviti.label.processmodel')}" cssClass="form-control"/>
                        </div>	
                    </div>

                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </s:form>
    </div>
</div>