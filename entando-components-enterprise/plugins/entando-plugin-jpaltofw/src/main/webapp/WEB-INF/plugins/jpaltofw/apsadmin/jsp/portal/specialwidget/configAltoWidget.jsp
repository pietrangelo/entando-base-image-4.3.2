<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li>
        <a href="<s:url action="configure" namespace="/do/Page">
               <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
           </s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="jpaltofw.name" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpaltofw.name" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="jpaltofw.name.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>
<hr>
<div class="mb-20">
    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <%--<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />--%>
    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
    <s:form action="save" namespace="/do/jpaltofw/Page/SpecialWidget/AltoWidget" cssClass="form-horizontal">
        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>

            <div class="panel-body">
                <h2 class="h5" >
                    <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(widget.type.code, widget.type.titles)}" />
                </h2>
                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" value="%{widget.type.code}" />
                    <wpsf:hidden name="validServer" />
                </p>
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <strong><s:text name="message.title.FieldErrors" /></strong>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                                </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <fieldset>
                    <legend><s:text name="title.serverConfiguration" /></legend>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="baseUrl"><s:text name="label.gokibiBaseUrl" /></label>
                        <div class="col-xs-10">
                            <s:if test="validServer" >
                                <s:property value="%{widget.config.get('baseUrl')}" />
                            </s:if>
                            <s:else>
                                <wpsf:textfield name="baseUrl" id="baseUrl" value="%{widget.config.get('baseUrl')}" cssClass="form-control" />
                            </s:else>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="pid"><s:text name="label.gokibiPid" /></label>
                        <div class="col-xs-10">
                            <s:if test="validServer" >
                                <s:property value="%{widget.config.get('pid')}" />
                            </s:if>
                            <s:else>
                                <wpsf:textfield name="pid" id="pid" value="%{widget.config.get('pid')}" cssClass="form-control" />
                            </s:else>
                        </div>
                    </div>
                    <s:if test="validServer" >
                        <p class="sr-only">
                            <wpsf:hidden name="baseUrl" value="%{widget.getConfig().get('baseUrl')}" />
                            <wpsf:hidden name="pid" value="%{widget.getConfig().get('pid')}" />
                        </p>
                        <div class="form-group">
                            <div class="col-xs-12">
                                <wpsf:submit action="changeServer" type="button" cssClass="btn btn-primary pull-right">
                                    <s:text name="label.changeServer" />
                                </wpsf:submit>
                            </div>
                        </div>
                    </s:if>

                </fieldset>
                <s:if test="validServer" >
                    <fieldset class="margin-base-top">
                        <legend><s:text name="title.gokibiWidget" /></legend>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="widgetCode"><s:text name="label.widgetCode" /></label>
                            <div class="col-xs-10">
                                <wpsf:select list="gokibiWidgets" name="widgetCode" id="widgetCode" listKey="key" listValue="value"
                                             value="%{widget.config.get('widgetCode')}" cssClass="form-control" />
                            </div>
                        </div>
                    </fieldset>
                </s:if>
            </div>
        </div>
        <s:if test="validServer" >
            <div class="form-group">
                <div class="col-xs-12 col-sm-4">
                    <wpsf:submit action="save" type="button" cssClass="btn btn-primary  pull-right ">
                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
            </div>
        </s:if>
        <s:else>
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:submit action="configServer" type="button" cssClass="btn btn-primary pull-right ">
                        <s:text name="label.continue" />
                    </wpsf:submit>
                </div>
            </div>
        </s:else>
    </s:form>
</div>
