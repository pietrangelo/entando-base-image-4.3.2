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

<s:form action="saveConfig" namespace="/do/jpjasper/Page/SpecialWidget/ReportListViewerConfig">
	<p class="noscreen">
		<wpsf:hidden name="pageCode" />
		<wpsf:hidden name="frame" />
		<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
		<wpsf:hidden name="uriString" />
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
	<fieldset class="col-xs-12">
		<legend><s:text name="label.info" /></legend>
		<s:iterator value="langs" var="lang">
			<div class="form-group">
				<label for="title_<s:property value="#lang.code" />"  ><span class="monospace">(<s:property value="#lang.code" />)</span><s:text name="label.title" />:</label>
				<wpsf:textfield  name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{showlet.config.get('title_' + #lang.code)}" cssClass="form-control" />
			</div>
		</s:iterator>
	</fieldset>
	<fieldset class="col-xs-12">
		<legend><s:text name="jpjasper.joinwidget.reportlist.configuration" /></legend>
		<s:set var="folderList" value="folders" />
		<s:if test="null != #folderList">
		<div class="form-group">
			<label for="jpjasper-folder" ><s:text name="jpjasper.joinwidget.reportlist.label.folder" />:</label>
			<wpsf:select
				name="folder"
				listKey="uriString"
				list="#folderList"
				cssClass="form-control"
				listValue="uriString+ ' (' +label+ ')'"
				id="jpjasper-folder"
				/>
		</div>
		<div class="form-group">
			<label for="jpjasper-filter" ><s:text name="jpjasper.joinwidget.reportlist.label.filter" />:</label>
			<wpsf:textfield
				name="filter"
				cssClass="form-control"
				id="jpjasper-filter"
				/>
		</div>
		<div class="form-group">
                    <div class="checkbox">
			<wpsf:checkbox
				name="recursive"
				value="%{null != recursive && recursive.equals('true')}"
				id="jpjasper-recursive"
				cssClass="radiocheck"
				/>
			<label for="jpjasper-recursive"><s:text name="jpjasper.joinwidget.reportlist.label.subfolders" /></label>
                    </div>
		</div>
		</s:if>
		<s:else>
                    <div class="alert alert-info">
                        <s:text name="jpjasper.joinwidget.reportlist.note.nofolders" />
                    </div>
		</s:else>
	</fieldset>
                    <div class="form-horizontal">
                        <div class="form-group">
                            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                                <wpsf:submit type="button" cssClass="btn btn-primary btn-block" action="saveConfig">
                                    <span class="icon fa fa-floppy-o"></span>&#32;
                                    <s:text name="%{getText('label.save')}"/>
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>
                </div>
                    </div>
</s:form>


</div>
</div>
