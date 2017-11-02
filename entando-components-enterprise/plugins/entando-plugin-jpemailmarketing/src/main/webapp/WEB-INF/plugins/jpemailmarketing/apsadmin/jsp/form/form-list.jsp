<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpemailmarketing.title.formManagement" />
	</span>
</h1>
<s:form action="list" cssClass="form-horizontal" role="search">
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<div class="form-group">
		<div class="input-group col-sm-12 col-md-12">
			<span class="input-group-addon">
				<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.courseId"/>"></span>
			</span>
			<label for="search-courseId" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.courseId"/></label>
			<wpsf:textfield
				id="form_courseId"
				name="courseId"
				cssClass="form-control input-lg"
				title="%{getText('label.search.by')+' '+getText('label.courseId')}"
				placeholder="%{getText('label.courseId')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" name="search-courseId" id="search-courseId" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
					<span class="sr-only"><s:text name="label.search" /></span>
					<span class="icon fa fa-search" title="<s:text name="label.search" />"></span>
				</wpsf:submit>
				<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
					<span class="sr-only"><s:text name="title.searchFilters" /></span>
					<span class="caret"></span>
				</button>
			</div>
		</div>

	  <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div id="search-advanced" class="collapse well collapse-input-group">
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_fileCoverName">
						<s:text name="label.fileCoverName"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_fileCoverName"
							name="fileCoverName"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_fileIncentiveName">
						<s:text name="label.fileIncentiveName"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_fileIncentiveName"
							name="fileIncentiveName"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_emailSubject">
						<s:text name="label.emailSubject"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_emailSubject"
							name="emailSubject"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_emailText">
						<s:text name="label.emailText"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_emailText"
							name="emailText"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_emailButton">
						<s:text name="label.emailButton"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_emailButton"
							name="emailButton"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_emailMessageFriendly">
						<s:text name="label.emailMessageFriendly"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_emailMessageFriendly"
							name="emailMessageFriendly"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_createdatStart">
						<s:text name="label.createdatStart"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_createdatStart"
							name="createdatStart"
							cssClass="form-control datepicker" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_createdatEnd">
						<s:text name="label.createdatEnd"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_createdatEnd"
							name="createdatEnd"
							cssClass="form-control datepicker" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_updatedatStart">
						<s:text name="label.updatedatStart"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_updatedatStart"
							name="updatedatStart"
							cssClass="form-control datepicker" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="form_updatedatEnd">
						<s:text name="label.updatedatEnd"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="form_updatedatEnd"
							name="updatedatEnd"
							cssClass="form-control datepicker" />
					</div>
				</div>
			<div class="form-group">
				<div class="col-sm-5 col-sm-offset-2">
					<s:submit type="button" cssClass="btn btn-primary">
						<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
					</s:submit>
				</div>
			</div>
		</div>
	</div>
	</div>
</s:form>

<%-- new disabled
<a href="<s:url action="new" />" class="btn btn-default">
	<span class="icon fa fa-plus-circle" />
	&#32;<s:text name="jpemailmarketing.form.label.new" />
</a>
 --%>

<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="courseId" />
		<wpsf:hidden name="fileCoverName" />
		<wpsf:hidden name="fileIncentiveName" />
		<wpsf:hidden name="emailSubject" />
		<wpsf:hidden name="emailText" />
		<wpsf:hidden name="emailButton" />
		<wpsf:hidden name="emailMessageFriendly" />
		<wpsf:hidden name="createdatStart" />
		<wpsf:hidden name="createdatEnd" />
		<wpsf:hidden name="updatedatStart" />
		<wpsf:hidden name="updatedatEnd" />
	</p>

	<s:set var="formsId_list" value="formsId" />
	<s:if test="#formsId_list.size > 0">
	<wpsa:subset source="#formsId_list" count="10" objectName="groupForms" advanced="true" offset="5">
	<s:set var="group" value="#groupForms" />
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th class="text-right"><s:text name="label.courseId" /></th>
				<th><s:text name="label.fileCoverName" /></th>
				<th><s:text name="label.fileIncentiveName" /></th>
				<th><s:text name="label.emailSubject" /></th>
				<th><s:text name="label.emailText" /></th>
				<th><s:text name="label.emailButton" /></th>
				<th><s:text name="label.emailMessageFriendly" /></th>
				<th class="text-center"><s:text name="label.createdat" /></th>
				<th class="text-center"><s:text name="label.updatedat" /></th>
			</tr>
			<s:iterator var="courseId">
			<s:set name="form_var" value="%{getForm(#courseId)}" />
			<s:url action="edit" var="editFormActionVar"><s:param name="courseId" value="#form_var.courseId"/></s:url>
			<s:url action="trash" var="trashFormActionVar"><s:param name="courseId" value="#form_var.courseId"/></s:url>
			<tr>
			<td class="text-center text-nowrap">
				<div class="btn-group btn-group-xs">
					<%-- edit --%>
						<a class="btn btn-default" title="<s:text name="label.edit" />&#32;<s:property value="#form_var.courseId" />" href="<s:property value="#editFormActionVar" escapeHtml="false" />">
							<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#form_var.courseId" /></span>
							<span class="icon fa fa-pencil-square-o"></span>
						</a>
				</div>
				<%-- remove DISABLED
				<div class="btn-group btn-group-xs">
					<a
						href="<s:property value="#trashFormActionVar" escapeHtml="false" />"
						title="<s:text name="jpemailmarketing.form.label.delete" />: <s:property value="#form_var.courseId" />"
						class="btn btn-warning"
						>
						<span class="icon fa fa-times-circle-o"></span>&#32;
						<span class="sr-only"><s:text name="jpemailmarketing.form.label.delete" /></span>
					</a>
				</div>
				--%>
			</td>
					<td class="text-right"><code><s:property value="#form_var.courseId"/></code></td>
					<td><s:property value="#form_var.fileCoverName"/></td>
					<td><s:property value="#form_var.fileIncentiveName"/></td>
					<td><s:property value="#form_var.emailSubject"/></td>
					<td><s:property value="#form_var.emailText"/></td>
					<td><s:property value="#form_var.emailButton"/></td>
					<td><s:property value="#form_var.emailMessageFriendly"/></td>
					<td class="text-center"><code><s:date name="#form_var.createdat" format="dd/MM/yyyy"/></code></td>
					<td class="text-center"><code><s:date name="#form_var.updatedat" format="dd/MM/yyyy"/></code></td>
			</tr>
			</s:iterator>
		</table>
	</div>
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	</wpsa:subset>
	</s:if>
	<s:else>
		<div class="alert alert-info margin-base-vertical">
			<s:text name="jpemailmarketing.form.message.list.empty" />
		</div>
	</s:else>
</s:form>