<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="jpemailmarketing.title.formManagement" /></a>
		&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="jpemailmarketing.form.label.new" />
		</s:if>
		<s:elseif test="getStrutsAction() == 2">
			<s:text name="jpemailmarketing.form.label.edit" />
		</s:elseif>
	</span>
</h1>
<div id="main">

	<s:form action="save" enctype="multipart/form-data" method="post">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none">
					<s:text name="message.title.FieldErrors" />
					&ensp;<span
						class="icon fa fa-question-circle cursor-pointer small text-muted"
						title="<s:text name="label.all" />"
						data-toggle="collapse"
						data-target="#fielderror-messages"></span>
				</h2>
				<ul class="unstyled collapse margin-small-top" id="fielderror-messages">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><%-- <s:property value="key" />&emsp;|--%><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
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

		<p class="sr-only">
			<wpsf:hidden name="strutsAction" />
			<wpsf:hidden name="courseId" />
			<wpsf:hidden name="createdat" />
			<wpsf:hidden name="updatedat" />
			<s:if test="getStrutsAction() == 2">
				<s:if test="null != fileCoverName">
					<wpsf:hidden name="fileCoverName" />
				</s:if>
				<s:if test="null != fileIncentiveName">
					<wpsf:hidden name="fileIncentiveName" />
				</s:if>
			</s:if>
		</p>

		<fieldset class="col-xs-12">
			<legend><s:text name="label.parameters" /></legend>
			<%--
				A new Form, related to a course,
				should be created automatically when the course is created,
				so there is no need to create the input field.

				courseId
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['courseId']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12">
						<label for="form_courseId"><s:text name="label.courseId" /></label>
						<s:set var="formcourse" value="%{getCourse(courseId)}" />
						<s:property value="#formcourse.name"/>
					</div>
				</div>
			--%>

			<%-- fileCoverName --%>
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['fileCoverName']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />

				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<label for="form_fileCoverName" class="display-block"><s:text name="label.fileCoverName" /></label>
					<div class="row">
						<div class="col-xs-10 col-sm-10 col-md-8 col-lg-5">
							<input type="file" name="cover" class="form-control" id="form_fileCoverName" />
						</div>
						<s:if test="null != fileCoverName">
							<s:url action="viewFile" var="viewCoverUrl">
								<s:param name="courseId" value="courseId" />
								<s:param name="type">cover</s:param>
							</s:url>
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-5" title="<s:property value="fileCoverName"/>">
								<a href="#"
									class="btn btn-info"
									data-toggle="popover"
									data-placement="bottom"
									title="<s:property value="fileCoverName"/>"
									data-html="true"
									data-content="<img src='<s:property value="#viewCoverUrl" escapeHtml="false" />' />"
									><s:text name="label.preview" /></a>
									<%-- <small class="text-muted"><s:property value="fileCoverName"/></small> --%>
							</div>
						</s:if>
					</div>
					<s:if test="#fieldHasFieldErrorVar">
						<span class="help-block text-danger">
							<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;<br /></s:iterator>
						</span>
					</s:if>
				</div>
<%--
						<s:if test="null != fileCoverName">
							<p class="text-right">
								<wpsf:submit action="deleteCover" type="button" cssClass="btn btn-link">
									<span class="icon fa fa-times-circle"></span>&#32;
									<s:text name="label.DELETE_COVER" />
								</wpsf:submit>
							</p>
						</s:if>
--%>


		<%-- signup message label --%>
			<s:include value="/WEB-INF/plugins/jpemailmarketing/apsadmin/jsp/form/inc/i18n-labels.jsp">
				<s:param name="labelCode">jpemailmarketing_FORM_COURSE_TITLE</s:param>
				<s:param name="labelName"><s:text name="label.formTitle" /></s:param>
			</s:include>

		<%-- signup message label --%>
			<s:include value="/WEB-INF/plugins/jpemailmarketing/apsadmin/jsp/form/inc/i18n-labels.jsp">
				<s:param name="labelCode">jpemailmarketing_SIGNUP_SUCCESS_MESSAGE</s:param>
				<s:param name="labelName"><s:text name="label.signupMessage" /></s:param>
			</s:include>


		<%-- incentive text label --%>
			<s:include value="/WEB-INF/plugins/jpemailmarketing/apsadmin/jsp/form/inc/i18n-labels.jsp">
				<s:param name="labelCode">jpemailmarketing_INCENTIVE_TEXT</s:param>
				<s:param name="labelName"><s:text name="label.incentiveText" /></s:param>
			</s:include>


		<%-- fileIncentiveName --%>
			<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['fileIncentiveName']}" />
			<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<label for="form_fileIncentiveName"><s:text name="label.fileIncentiveName" /></label>
					<div class="row">
						<div class="col-xs-10 col-sm-10 col-md-8 col-lg-5">
							<input type="file" name="incentive" class="form-control" id="form_fileIncentiveName" />
						</div>
						<%-- preview --%>
							<s:if test="null != fileIncentiveName">
								<s:url action="downloadFile" var="viewInvecentiveUrl">
									<s:param name="courseId" value="courseId" />
									<s:param name="type">incentive</s:param>
								</s:url>
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-5" title="<s:property value="fileIncentiveName"/>">
									<a href="<s:property value="#viewInvecentiveUrl" escapeHtml="false" />" class="btn btn-info">
										<s:text name="label.preview" />
									</a>
								</div>
							</s:if>
					</div>
					<%--
					<s:if test="null != fileIncentiveName">
							<p class="text-right">
								<wpsf:submit action="deleteIncentive" type="button" cssClass="btn btn-link">
									<span class="icon fa fa-times-circle"></span>&#32;
									<s:text name="label.DELETE_INCENTIVE" />
								</wpsf:submit>
							</p>
					</s:if>
					--%>
					<s:if test="!#fieldHasFieldErrorVar">
						<span class="help-block text-danger">
							<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
						</span>
					</s:if>
			</div>
	</fieldset>

	<fieldset class="col-xs-12">
		<legend><s:text name="label.emailSettings" /></legend>

		<%-- emailSubject --%>
			<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['emailSubject']}" />
			<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<label for="form_emailSubject"><s:text name="label.emailSubject" /></label>
					<wpsf:textfield name="emailSubject" id="form_emailSubject" cssClass="form-control" />
					<s:if test="#fieldHasFieldErrorVar">
						<span class="help-block text-danger">
							<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
						</span>
					</s:if>
			</div>

		<%-- emailText --%>
			<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['emailText']}" />
			<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<label for="form_emailText"><s:text name="label.emailText" /></label>
					<wpsf:textarea cols="50" rows="7" name="emailText" id="form_emailText" cssClass="form-control" />
					<s:if test="#fieldHasFieldErrorVar">
						<span class="help-block text-danger">
							<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
						</span>
					</s:if>
			</div>

		<%-- emailButton --%>
			<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['emailButton']}" />
			<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<label for="form_emailButton"><s:text name="label.emailButton" /></label>
					<wpsf:textfield name="emailButton" id="form_emailButton" cssClass="form-control" />
					<s:if test="#fieldHasFieldErrorVar">
						<span class="help-block text-danger">
							<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
						</span>
					</s:if>
			</div>

		<%-- emailMessageFriendly --%>
			<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['emailMessageFriendly']}" />
			<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<label for="form_emailMessageFriendly"><s:text name="label.emailMessageFriendly" /></label>
					<wpsf:textarea cols="50" rows="3" name="emailMessageFriendly" id="form_emailMessageFriendly" cssClass="form-control" />
					<s:if test="#fieldHasFieldErrorVar">
						<span class="help-block text-danger">
							<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
						</span>
					</s:if>
			</div>
<%--
		<s:if test="getStrutsAction() == 2">
			<!-- createdat -->
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['createdat']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12 text-muted">
						<label for="form_createdat"><s:text name="label.createdat" /></label>
						<p class="form-control-static">
							<s:date name="createdat" format="dd/MM/yyyy HH:mm:ss"/>
						</p>
					</div>
				</div>
			<!-- updatedat -->
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['updatedat']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<div class="col-xs-12 text-muted">
						<label for="form_updatedat"><s:text name="label.updatedat" /></label>
						<p class="form-control-static">
							<s:date name="updatedat" format="dd/MM/yyyy HH:mm:ss"/>
						</p>
					</div>
				</div>
		</s:if>
--%>
			<%-- save button --%>
				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
							<wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
								<span class="icon fa fa-floppy-o"></span>&#32;
								<s:text name="label.save" />
							</wpsf:submit>
							<s:if test="getStrutsAction() == 2">
								<span class="help-block small text-muted text-center" data-toggle="tooltip" data-placement="bottom" title="<s:date name="updatedat" format="EEEE dd/MMM/yyyy HH:mm:ss" nice="false" />">
									<s:text name="label.updatedat" />&#32;
									<s:date name="updatedat" format="dd/MM/yyyy HH:mm:ss" nice="true" />
								</span>
							</s:if>
						</div>
					</div>
				</div>
		</fieldset>

	</s:form>
</div>
