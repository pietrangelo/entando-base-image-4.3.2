<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.subsiteManagement" />"><s:text name="title.subsiteManagement" /></a>&#32;/&#32;
        <s:text name="title.subsiteManagement.clone" />
    </span>
</h1>

<div id="main" role="main">

<s:form action="saveClone" method="post" enctype="multipart/form-data" >
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
            <ul class="margin-base-top">
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </s:iterator>
            </ul>
        </div>
    </s:if>
    <p class="sr-only">
	<wpsf:hidden name="strutsAction" />
	<s:if test="getStrutsAction() == 2">
		<wpsf:hidden name="code" />
		<wpsf:hidden name="subsite.code" />
		<wpsf:hidden name="subsite.homepage" />
                <wpsf:hidden name="subsite.categoryCode" />
                 <wpsf:hidden name="subsite.groupName" />
	</s:if>
    </p>
    
    <s:if test="getStrutsAction() == 1">
    <div class="form-group">
        <label for="subsite.code"><s:text name="label.code"/></label>
        <wpsf:textfield name="subsite.code" id="subsite.code" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
    </div>
    </s:if>
    <s:else>
        <table class="table table-bordered">
            <tr>
                <th class="text-right"><s:text name="label.code"/></th>
                <td><s:property value="subsite.code"/></td>
            </tr>
            <tr>
                <th class="text-right"><s:text name="label.homepage"/></th>
                <s:set var="homepageVar" value="root"/>
                <td><s:property value="getTitle(#homepageVar.code, #homepageVar.titles)" /></td>
            </tr>
            <tr>
                <th class="text-right"><s:text name="label.group" /></th>
                <s:set var="groupVar" value="%{getGroup(subsite.groupName)}"/>
                <td>
                    <s:if test="%{null != #groupVar}"><s:property value="#groupVar.description" /></s:if>
                    <s:else>&ndash;</s:else>
                </td>
            </tr>
        </table>
    </s:else>
    <s:iterator value="langs">
        <div class="form-group">
            <label for="titles_<s:property value="code" />"><abbr title="<s:property value="descr" />"><code class="label label-info" ><s:property value="code" /></code></abbr>&#32;<s:text name="name.pageTitle" /></label>
            <wpsf:textfield name="%{'titles_'+code}" id="%{'titles_'+code}" value="%{subsite.getTitles().get(code)}" cssClass="form-control" />
        </div>
    </s:iterator>
    <s:iterator value="langs">
        <div class="form-group">
            <label for="descriptions_<s:property value="code" />"><abbr title="<s:property value="descr" />"><code class="label label-info" ><s:property value="code" /></code></abbr>&#32;<s:text name="name.subsiteTitle" /></label>
            <wpsf:textfield name="%{'descriptions_'+code}" id="%{'descriptions_'+code}" value="%{subsite.getDescriptions().get(code)}" cssClass="form-control" />
        </div>
    </s:iterator>
    <s:if test="getStrutsAction() == 2">
        <div class="form-group">
            <label for="subsite.contentViewerPage"><s:text name="label.contentViewerPage"/></label>
            <p><s:text name="note.contentViewerPage.intro"/></p>
            <wpsf:select name="subsite.contentViewerPage" id="subsite.contentViewerPage" 
                         list="subsitePages" listKey="code" listValue="getFullTitle(currentLang.code)" 
                         headerKey="" headerValue="%{getText('label.select')}" cssClass="form-control" />
        </div>
        <div class="form-group">
            <label for="subsite.categoryCode"><s:text name="label.categoryCode" />:</label><br />
            <s:set var="subsiteCategoryVar" value="%{getCategory(subsite.categoryCode)}" />
            <s:property value="#subsiteCategoryVar.getFullTitle()" />
        </div>
    </s:if>
	<div class="form-group">
		<label for="subsite.group"><s:text name="label.group" />:</label><br />
		<wpsf:select name="subsite.groupName" id="subsite.groupName" 
					 list="groupsForNewSubsite" listKey="name" listValue="description" 
					 headerKey="" cssClass="form-control" />

	</div>
	
    <div class="form-group">
        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                <span class="icon fa fa-floppy-o"></span>&#32;
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </div>
</s:form>
	
</div>