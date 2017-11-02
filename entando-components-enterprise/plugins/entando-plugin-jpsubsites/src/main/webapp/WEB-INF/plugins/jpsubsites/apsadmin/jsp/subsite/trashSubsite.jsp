<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list"></s:url>"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.subsiteManagement" />">
            <s:text name="title.subsiteManagement" />
        </a>&#32;/&#32;<s:text name="title.subsiteManagement.trash" />
    </span>
</h1>
<s:form action="delete">
    <p class="sr-only"><wpsf:hidden name="code"/></p>
    <div class="alert alert-warning">
        <p>
            <s:text name="note.subsite.trash.confirm" />&#32;<code><s:property value="subsite.code" /></code>?
        </p>
        <p>
            <s:text name="note.subsite.trash.important"><s:param ><s:property value="%{getGroup(subsite.groupName).description}" /></s:param></s:text>?
        </p>
        <div class="text-center margin-large-top">
            <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                <span class="icon fa fa-times-circle"></span>&#32;
                <s:text name="label.confirm" />
            </wpsf:submit>
            <a class="btn btn-link" href="<s:url action="list" />">
                <s:text name="note.goToSomewhere" />:&#32;<s:text name="title.subsiteManagement" /></a>
        </div>
    </div>
</s:form>
