<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<s:set var="email" value="%{getCourseEmail(courseMailId)}" />
<div class="panel panel-default">
	<div class="panel-heading">
		<h4 class="panel-title">
			<a class="display-block" data-toggle="collapse" data-parent="#accordion" href="#email-<s:property value="courseid" />-collapse-<s:property value="courseMailId" />">
				<span class="badge margin-base-right"><s:property value="position+1" /></span>&#32;
				<s:property value="#email.emailSubject"/>
			</a>
		</h4>
	</div>
	<div id="email-<s:property value="courseid" />-collapse-<s:property value="courseMailId" />" class="panel-collapse collapse <s:if test="currentForm"> in </s:if>">
		<div class="panel-body">

			<p class="margin-large-bottom">
				<s:date name="#date_future" format="HH:mm a" var="sendDate" />
				<s:set var="date_future" value="%{getExecutionTime(courseid)}" />
				<s:if test="#date_future==null"><s:set var="date_future">00:00</s:set></s:if>
				<s:else><s:date name="#date_future" format="HH:mm a" var="date_future" /></s:else>
				<s:property value="%{getText('note.nextSend', {subscriberDelay,  #date_future })}" escapeHtml="false" />.
			</p>


			<%-- error messages --%>
			<s:if test="currentForm && hasFieldErrors()">
				<div class="alert alert-danger alert-dismissable fade in">
					<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
					<h5 class="h4 margin-none">
						<s:text name="message.title.FieldErrors" />
						&ensp;<span
							class="small icon fa fa-question-circle cursor-pointer"
							title="<s:text name="label.all" />"
							data-toggle="collapse"
							data-target="#content-error-messages"></span>
					</h5>
					<ul class="unstyled collapse margin-small-top" id="content-error-messages">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><%-- <s:property value="key" />&emsp;|--%><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</s:if>

			<s:form action="saveEmail" cssClass="form-horizontal margin-large-top">
				<p class="sr-only">
					<wpsf:hidden name="id" value="%{id}" />

					<wpsf:hidden name="courseMailId" value="%{courseMailId}" />
					<wpsf:hidden name="courseid" value="%{courseid}" />
					<wpsf:hidden name="position" value="%{position}" />
					<wpsf:hidden name="createdat" value="%{createdat}"/>
					<wpsf:hidden name="updatedat" value="%{updatedat}"/>
					<wpsf:hidden name="dalayDays" value="%{dalayDays}"/>

				</p>


				<%-- subject --%>
					<div class="form-group">
						<div class="col-xs-12">
							<label for="email-<s:property value="courseid" />-<s:property value="courseMailId" />-field-subject">
								<s:text name="label.email.subject" />
							</label>
							<wpsf:textfield name="emailSubject" id="%{'email-'+ courseid +'-'+ courseMailId +'-field-subject'}"  cssClass="form-control" />
						</div>
					</div>


				<%-- content --%>
					<div class="form-group">
						<div class="col-xs-12">
							<label for="email-<s:property value="courseid" />-<s:property value="courseMailId" />-field-text">
								<s:text name="label.email.body" />
							</label>
							<wpsf:textarea name="emailContent"
							id="%{'email-'+ courseid +'-'+ courseMailId +'-field-text'}" cssClass="form-control" data-component="fckeditor" value="%{emailContent}" />
						</div>
					</div>

					<div class="form-group">
						<div class="col-xs-6">
								<s:submit type="button" data-button="preview" data-ref="%{'#email-'+ courseid +'-'+ courseMailId +'-field-text'}" cssClass="btn btn-info pull-left">
									<span class="icon fa fa-eye"></span>&#32;
									<s:text name="label.preview" />
								</s:submit>
						</div>
						<div class="col-xs-6">
								<s:submit type="button" cssClass="btn btn-primary pull-right">
									<span class="icon fa fa-save"></span>&#32;
									<s:text name="label.save" />
								</s:submit>
						</div>
					</div>

			</s:form>

		</div>
	</div>
</div>
