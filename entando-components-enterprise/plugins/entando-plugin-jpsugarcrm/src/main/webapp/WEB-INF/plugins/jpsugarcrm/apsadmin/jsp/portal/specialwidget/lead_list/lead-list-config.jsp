<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<a href="<s:url action="configure" namespace="/do/Page"><s:param name="pageCode"><s:property value="currentPage.code"/></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" /></a>&#32;/&#32;
		<s:text name="name.widget" />
	</span>
</h1>

<div id="main">

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

<s:form action="saveConfig" namespace="/do/jpsugarcrm/Lead/Page/SpecialWidget/jpsugarcrmLeadListConfig">
<div class="panel panel-default">
	<div class="panel-heading">
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
	</div>

	<div class="panel-body">

		<h2 class="h5 margin-small-vertical">
			<label class="sr-only"><s:text name="name.widget" /></label>
			<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
			<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
		</h2>

		<p class="sr-only">
			<s:hidden name="pageCode" />
			<s:hidden name="frame" />
			<s:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
		</p>

		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
				<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
				</ul>
			</div>
		</s:if>


		<h3><s:text name="label.info" /></h3>
			<div class="form-group">
				<div class="col-xs-12">
					<label for="maxResults">
						<s:text name="label.maxResults" />
					</label>
					<wpsf:textfield name="maxResults" id="maxResults" value="%{widget.config.get('maxResults')}" cssClass="form-control" />
				</div>
			</div>

	</div>
</div>
<div class="form-group">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
		<s:submit value="%{getText('label.save')}" cssClass="btn btn-primary btn-block" />
	</div>
</div>
</s:form>
</div>