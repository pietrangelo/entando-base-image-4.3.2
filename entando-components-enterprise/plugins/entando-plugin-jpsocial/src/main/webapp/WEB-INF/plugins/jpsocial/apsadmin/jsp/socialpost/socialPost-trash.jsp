<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="targetNS" value="%{'/do/jpsocial/SocialPost'}" />
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />"><s:text name="jpsocial.title.socialPostManagement" /></a>
    </span>
</h1>
<div id="main">
    <s:form action="delete" class="form-horizontal">
        <div class="alert alert-warning">
            <wpsf:hidden name="strutsAction" />
            <wpsf:hidden name="id" />
            <s:text name="note.deleteSocialPost.areYouSure" />&#32;
            <em class="important"><code><s:property value="id"/></code></em>
            &#32;/
            <code><s:property value="provider" /></code>
            &#32;/
            <code><s:date name="date" format="dd/MM/yyyy HH:mm"/></code>
            &#32;/
            <code><s:property value="objectid" /></code>
            ?
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;
                    <s:text name="%{getText('label.remove')}"/>

                </wpsf:submit>
                <p class="text-center margin-small-top">
                    <a class="btn btn-link" href="<s:url action="list"/>">
                        <s:text name="%{getText('label.back')}"/>
                    </a>
                </p> 
            </div>
        </s:form>
    </div>