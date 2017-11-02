<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpemailmarketing.title.courseMailManagement" />
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
				<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.id"/>"></span>
			</span>
			<label for="search-id" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.id"/></label>
			<wpsf:textfield
				id="courseMail_id"
				name="id"
				cssClass="form-control input-lg"
				title="%{getText('label.search.by')+' '+getText('label.id')}"
				placeholder="%{getText('label.id')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" name="search-id" id="search-id" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
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
					<label class="control-label col-sm-2 text-right" for="courseMail_courseid">
						<s:text name="label.courseid"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_courseid"
							name="courseid"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="courseMail_dalayDays">
						<s:text name="label.dalayDays"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_dalayDays"
							name="dalayDays"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="courseMail_emailSubject">
						<s:text name="label.emailSubject"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_emailSubject"
							name="emailSubject"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="courseMail_emailContent">
						<s:text name="label.emailContent"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_emailContent"
							name="emailContent"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="courseMail_createdatStart">
						<s:text name="label.createdatStart"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_createdatStart"
							name="createdatStart"
							cssClass="form-control datepicker" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="courseMail_createdatEnd">
						<s:text name="label.createdatEnd"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_createdatEnd"
							name="createdatEnd"
							cssClass="form-control datepicker" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="courseMail_updatedatStart">
						<s:text name="label.updatedatStart"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_updatedatStart"
							name="updatedatStart"
							cssClass="form-control datepicker" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="courseMail_updatedatEnd">
						<s:text name="label.updatedatEnd"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="courseMail_updatedatEnd"
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

<a href="<s:url action="new" />" class="btn btn-default">
	<span class="icon fa fa-plus-circle" />
	&#32;<s:text name="jpemailmarketing.courseMail.label.new" />
</a>

<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="id" />
		<wpsf:hidden name="courseid" />
		<wpsf:hidden name="dalayDays" />
		<wpsf:hidden name="emailSubject" />
		<wpsf:hidden name="emailContent" />
		<wpsf:hidden name="createdatStart" />
		<wpsf:hidden name="createdatEnd" />
		<wpsf:hidden name="updatedatStart" />
		<wpsf:hidden name="updatedatEnd" />
	</p>

	<s:set var="courseMailsId_list" value="courseMailsId" />
	<s:if test="#courseMailsId_list.size > 0">
	<wpsa:subset source="#courseMailsId_list" count="10" objectName="groupCourseMails" advanced="true" offset="5">
	<s:set var="group" value="#groupCourseMails" />
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th class="text-right"><s:text name="label.id" /></th>
				<th class="text-right"><s:text name="label.courseid" /></th>
				<th class="text-right"><s:text name="label.dalayDays" /></th>
				<th><s:text name="label.emailSubject" /></th>
				<th><s:text name="label.emailContent" /></th>
				<th class="text-center"><s:text name="label.createdat" /></th>
				<th class="text-center"><s:text name="label.updatedat" /></th>
			</tr>
			<s:iterator var="id">
			<s:set name="courseMail_var" value="%{getCourseMail(#id)}" />
			<s:url action="edit" var="editCourseMailActionVar"><s:param name="id" value="#courseMail_var.id"/></s:url>
			<s:url action="trash" var="trashCourseMailActionVar"><s:param name="id" value="#courseMail_var.id"/></s:url>
			<tr>
			<td class="text-center text-nowrap">
				<div class="btn-group btn-group-xs">
					<%-- edit --%>
						<a class="btn btn-default" title="<s:text name="label.edit" />&#32;<s:property value="#courseMail_var.id" />" href="<s:property value="#editCourseMailActionVar" escapeHtml="false" />">
							<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#courseMail_var.id" /></span>
							<span class="icon fa fa-pencil-square-o"></span>
						</a>
				</div>
				<%-- remove --%>
				<div class="btn-group btn-group-xs">
					<a
						href="<s:property value="#trashCourseMailActionVar" escapeHtml="false" />"
						title="<s:text name="jpemailmarketing.courseMail.label.delete" />: <s:property value="#courseMail_var.id" />"
						class="btn btn-warning"
						>
						<span class="icon fa fa-times-circle-o"></span>&#32;
						<span class="sr-only"><s:text name="jpemailmarketing.courseMail.label.delete" /></span>
					</a>
				</div>
			</td>
					<td class="text-right"><code><s:property value="#courseMail_var.id"/></code></td>
					<td class="text-right"><s:property value="#courseMail_var.courseid"/></td>
					<td class="text-right"><s:property value="#courseMail_var.dalayDays"/></td>
					<td><s:property value="#courseMail_var.emailSubject"/></td>
					<td><s:property value="#courseMail_var.emailContent"/></td>
					<td class="text-center"><code><s:date name="#courseMail_var.createdat" format="dd/MM/yyyy"/></code></td>
					<td class="text-center"><code><s:date name="#courseMail_var.updatedat" format="dd/MM/yyyy"/></code></td>
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
			<s:text name="jpemailmarketing.courseMail.message.list.empty" />
		</div>
	</s:else>
</s:form>