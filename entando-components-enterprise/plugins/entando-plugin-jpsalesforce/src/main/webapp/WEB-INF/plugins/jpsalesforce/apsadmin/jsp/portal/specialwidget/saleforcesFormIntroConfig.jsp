<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>



<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:text name="title.configPage" />
	</span>
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
	<s:param name="selectedNode" value="currentPage.code"></s:param>
</s:action>

<s:form action="configure" namespace="/do/jpsalesforce/Page/SpecialWidget/form" cssClass="form-horizontal">

	<%-- error //start --%>
			<s:if test="hasFieldErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.FieldErrors" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<s:if test="hasActionErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.ActionErrors" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="actionErrors">
							<li><s:property /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
		<%-- error //end --%>

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

		<p class="noscreen">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
		</p>

		<div class="alert alert-info">
			<s:text name="jpsalesforce.mapping.intro" />
		</div>

		<div class="form-group">
			<div class="col-xs-12">
				<label for="jpsalesforce-config-campaign"><s:text name="jpsalesforce.label.campaignId" /></label>
				<wpsf:select name="campaignId" list="campaigns" id="jpsalesforce-config-campaign" cssClass="form-control" />
			</div>
		</div>

    <div class="form-group">
			<div class="col-xs-12">
				<label for="jpsalesforce-config-description"><s:text name="jpsalesforce.label.description" /></label>
				<wpsf:textfield name="formDescription" id="jpsalesforce-config-description" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<div class="col-xs-12">
				<label for="jpsalesforce-config-profile"><s:text name="jpsalesforce.label.userProfile" /></label>
				<wpsf:select name="profileId" list="profiles" id="jpsalesforce-config-profile" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<div class="col-xs-12">
				<label for="jpwebdynamicform_formProtectionType"><s:text name="jpsalesforce.label.formProtectionType" /></label>
				<wpsf:select id="jpwebdynamicform_formProtectionType" name="formProtectionType" list="formProtectionTypeSelectItems"
					listKey="key" listValue="value" cssClass="form-control" />
			</div>
		</div>

		<div class="form-group">
			<div class="col-xs-12">
				<wpsf:submit type="button" cssClass="btn btn-default">
					<s:text name="label.continue" />
					&#32;
					<span class="icon fa fa-long-arrow-right"></span>
				</wpsf:submit>
			</div>
		</div>

</s:form>

</div><%-- main //end --%>
