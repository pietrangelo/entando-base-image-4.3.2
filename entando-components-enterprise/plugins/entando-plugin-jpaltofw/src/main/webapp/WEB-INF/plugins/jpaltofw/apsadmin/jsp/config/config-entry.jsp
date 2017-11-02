<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement"/>
        </a>
    </li>
    <li>
        <a href="<s:url action="configure" namespace="/do/Page">
               <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
           </s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text
                name="title.configPage"/>
        </a>
    </li>
    <li class="page-title-container">

        <s:text name="jpaltofw.config"/>
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpaltofw.config"/>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="jpaltofw.name.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>
<hr>
<div class="mb-20">


    <s:form id="configurationForm" name="configurationForm" action="save" method="post" cssClass="form-horizontal">

        <!--test -->

        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.FieldErrors"/></strong>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false"/></li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <fieldset>
            <legend><s:text name="title.altoConfiguration"/></legend>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="baseUrl"><s:text name="label.altoId"/></label>
                <div class="col-xs-10">

                    <wpsf:textfield name="id" id="id" cssClass="form-control"/>

                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="pid"><s:text name="label.altoBaseurl"/></label>
                <div class="col-xs-10">

                    <wpsf:textfield name="baseUrl" id="baseUrl" cssClass="form-control"/>

                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="pid"><s:text name="label.altoPid"/></label>
                <div class="col-xs-10">

                    <wpsf:textfield name="pid" id="pid" cssClass="form-control"/>

                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="pid"><s:text name="label.altoProjectPassword"/></label>
                <div class="col-xs-10">

                    <wpsf:textfield name="projectPassword" id="projectPassword" cssClass="form-control"/>

                </div>
            </div>
        </fieldset>

        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" action="save" cssClass="btn btn-primary pull-right ">
                    <s:text name="label.save"/>
                </wpsf:submit>
            </div>
        </div>

    </s:form>

</div>
