<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.subsiteManagement" />"><s:text name="title.subsiteManagement" /></a>&#32;/&#32;
        <s:if test="strutsAction == 1"><s:text name="title.subsiteManagement.new" /></s:if>
        <s:elseif test="strutsAction == 2"><s:text name="title.subsiteManagement.edit" /></s:elseif>
        </span>
    </h1>

    <div id="main" role="main">

    <s:form action="save" method="post" enctype="multipart/form-data" >
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

        <label for="subsite.visibility"><span class="group-visibility"><s:text name="label.visibility"/></span></label>

        <p id="visibility" style="visibility: hidden" >STATUS:<s:property value="subsite.visibility" /></p>

        <div class="form-group">
            <div id="onStatus" class="btn-group btn-group ">
                <a class="btn btn-success" href="<s:url action="visibility"><s:param name="on" value="true"/><s:param name="code" value="subsite.code"/>
                   </s:url>" title="<s:text name="label.on" /> <s:property value="on" />">Set online
                    <span class="sr-only"><s:text name="label.on" />: <s:property value="true" /></span>
                </a>
            </div>

            <div class="btn-group btn-group">
                <a id="offStatus" class="btn btn-warning"  href="<s:url action="visibility"> <s:param name="off" value="false"/>
                       <s:param name="code" value="subsite.code"/></s:url>" title="<s:text name="label.off" /> <s:property value="off" />"  >Set offline
                    <span class="sr-only"><s:text name="label.off" />: <s:property value="false" /></span>
                </a>
            </div>
        </div>
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

            <s:set var="subsitePagesVar" value="subsitePages" />
            <div class="form-group">
                <label for="subsite.contentViewerPage"><s:text name="label.content.viewerPage"/></label>
                <p><s:text name="note.content.viewerPage.intro"/></p>
                <wpsf:select name="subsite.contentViewerPage" id="subsite.contentViewerPage"
                             list="#subsitePagesVar" listKey="code" listValue="getFullTitle(currentLang.code)"
                             headerKey="" headerValue="%{getText('label.select')}" cssClass="form-control" />
            </div>

            <s:set var="contentTypesVar" value="contentTypes" />
            <div class="table-responsive margin-base-vertical">
                <label for="subsite.contentViewerPages"><s:text name="label.content.viewerPages"/></label>
                <p><s:text name="note.content.viewerPages.intro"/></p>
                <s:if test="%{#contentTypesVar.size > 0}">
                    <table class="table table-bordered">
                        <tr>
                            <th><s:text name="label.content.type" /></th>
                            <th><s:text name="label.content.viewPage" /></th>
                        </tr>
                        <s:iterator value="#contentTypesVar" var="contentTypeVar">
                            <tr>
                                <td>
                                    <s:property value="#contentTypeVar.description" />&#32;<code><s:property value="#contentTypeVar.code" /></code>
                                </td>
                                <td>
                                    <wpsf:select name="%{#contentTypeVar.code + '_viewerPage'}" id="%{#contentTypeVar.code + '_viewerPage'}" list="#subsitePagesVar"
                                                 headerKey="" headerValue="%{getText('label.select')}" value="%{subsite.getViewerPages().get(#contentTypeVar.code)}"
                                                 listKey="code" listValue="getShortFullTitle(currentLang.code)" cssClass="form-control" />
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </s:if>
                <s:else>
                    <p><s:text name="no.contentytpes" /></p>
                </s:else>
            </div>


            <div class="form-group">
                <label for="subsite.categoryCode"><s:text name="label.categoryCode" />:</label><br />
                <s:set var="subsiteCategoryVar" value="%{getCategory(subsite.categoryCode)}" />
                <s:property value="#subsiteCategoryVar.getFullTitle()" />
            </div>
        </s:if>
        <!-- insert groupSelection -->
        <div class="form-group">
            <label for="subsite.group"><s:text name="label.group" />:</label><br />
            <s:if test="strutsAction == 1">
                <wpsf:select name="subsite.groupName" id="subsite.groupName"
                             list="groupsForNewSubsite" listKey="name" listValue="description"
                             headerKey="" cssClass="form-control" />
            </s:if>
            <s:else>
                <wpsf:select name="subsite.groupName" id="subsite.groupName"
                             list="groupsList" listKey="name" listValue="description"
                             headerKey="" cssClass="form-control"
                             disabled="%{getStrutsAction() == 2}"/>
            </s:else>
        </div>
        <!-- end groupSelection -->
        <div class="form-group">
            <label for="subsite.cssFileName"><s:text name="label.cssFileName"/></label>
            <a href="<s:property value="cssUrl" />" title="<s:text name="label.download" /> css" >
                <s:property value="subsite.cssFileName" /></a>
                <%-- <wpsf:textfield name="subsite.cssFileName" id="subsite.cssFileName" cssClass="form-control" /> --%>
                <wpsf:textarea cssClass="form-control" id="file-content"
                               placeholder="file content here..."
                               name="cssFileText" rows="20" cols="50" />
        </div>
        <div class="form-group">
            <label for="subsite.imageFileName"><s:text name="label.imageFileName"/></label>
            <a href="<s:property value="headerUrl" />" title="<s:text name="label.download" /> header" >
                <s:property value="subsite.imageFileName" /></a>
                <%-- <wpsf:textfield name="subsite.imageFileName" id="subsite.imageFileName" cssClass="form-control" /> --%>
            <br />
            <label for="file-upload">Upload new Header</label>
            <s:file name="uploadHeader" id="file-upload" cssClass="form-control" />
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
<script>
    $(function () {
        if ($('#visibility').text().indexOf("true") !== -1) {
            $('#onStatus').addClass('hidden');
        } else {
            $('#offStatus').addClass('hidden');
        }
    });
</script>

<style>
    .group-visibility{
        color: #fff;
        background-color: #5bc0de;
        border-color: #5bc0de;
        padding: 0.5em;
    }
</style>
