<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="jpemailmarketing.name" /></a>
		&#32;/&#32;
		<s:text name="jpemailmarketing.title.configure" />
	</span>
</h1>

<div id="main">

	<s:form action="save">

		<div class="panel panel-default">
			<div class="panel-heading display-block-float2">
					<div class="row"><div class="col-xs-12">
							<h2 class="h4 margin-none display-inline">
								<s:property value="name" />
							</h2>
					</div></div>
			</div>
			<div class="panel-body">
				<s:if test="hasFieldErrors()">
					<div class="alert alert-danger alert-dismissable fade in">
						<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
						<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
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
					<s:if test="getStrutsAction() == 2">
						<wpsf:hidden name="id" />
					</s:if>
					<wpsf:hidden name="createdat" />
					<wpsf:hidden name="updatedat" />
					<wpsf:hidden name="cronexp" />
				</p>

				<%-- name --%>
					<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['name']}" />
					<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
					<div class="margin-base-top form-group<s:property value="#controlGroupErrorClassVar" />">
							<label for="course_name"><s:text name="label.name" /></label>
							<wpsf:textfield name="name" id="course_name" cssClass="form-control" />
							<s:if test="#fieldHasFieldErrorVar">
									<span class="help-block text-danger">
										<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
								</span>
							</s:if>
					</div>

					<%-- sender --%>
						<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['sender']}" />
						<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
						<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
						<div class="margin-large-bottom form-group<s:property value="#controlGroupErrorClassVar" />">
								<label for="course_sender"><s:text name="label.sender" /></label>
									<wpsf:textfield name="sender" id="course_sender" cssClass="form-control" />
								<s:if test="#fieldHasFieldErrorVar">
										<span class="help-block text-danger">
											<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
									</span>
								</s:if>
						</div>

					<fieldset class="col-xs-12 margin-large-top">
						<legend class="h4"><s:text name="label.scheduler.options" /></legend>

						<%-- active --%>
							<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['active']}" />
							<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
							<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
							<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
									<label for="course_active"><s:text name="label.status" /></label>

									<div class="radio">
										<label>
											<input type="radio" name="active" id="activetrue" value="1" <s:if test="%{active==1}">checked="checked"</s:if>" />
											<s:text name="label.status.active" />
											<s:set var="date_future" value="%{getExecutionTime(id)}" />
											<s:if test="%{null != #date_future}">
												<small class="text-muted">(<s:text name="note.next.email.will.be.sent" />&#32;<s:date name="#date_future" nice="true"/>)</small>
											</s:if>
										</label>
									</div>
									<div class="radio">
										<label>
											<input type="radio" name="active" id="activefalse" value="0" <s:if test="%{active==0}">checked="checked"</s:if>" />
											<s:text name="label.status.active.not" />
										</label>
									</div>
									<%-- <wpsf:select list="#{0:0, 1:1}" name="active" id="course_active" cssClass="form-control" /> --%>
										<s:if test="#fieldHasFieldErrorVar">
											<span class="help-block">
												<span class="text-danger"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></span>
											</span>
										</s:if>
							</div>


						<%-- send hour --%>
							<s:set var="fieldFieldErrorsVarHour" value="%{fieldErrors['cronHour']}" />
							<s:set var="fieldHasFieldErrorVarHour" value="#fieldFieldErrorsVarHour != null && !#fieldFieldErrorsVarHour.isEmpty()" />

							<s:set var="fieldFieldErrorsVarTZ" value="%{fieldErrors['cronHour']}" />
							<s:set var="fieldHasFieldErrorVarTZ" value="#fieldFieldErrorsVarTZ != null && !#fieldFieldErrorsVarHour.isEmpty()" />

							<s:set var="controlGroupErrorClassVarFF" value="%{(#fieldHasFieldErrorVarHour||#fieldHasFieldErrorVarTZ) ? ' has-error' : ''}" />

							<div class="form-group<s:property value="#controlGroupErrorClassVarFF" />">
								<label><s:text name="label.sendtime" /></label>
								<div class="row">
									<div class="col-xs-2">
										<label for="course_cronHour" class="sr-only"><s:text name="label.cronHour" /></label>
										<wpsf:select name="cronHour" id="course_cronHour" cssClass="form-control" list="#{
											'00:00':'00:00','01:00':'01:00','02:00':'02:00','03:00':'03:00','04:00':'04:00','05:00':'05:00','06:00':'06:00','07:00':'07:00','08:00':'08:00','09:00':'09:00','10:00':'10:00','11:00':'11:00','12:00':'12:00','13:00':'13:00','14:00':'14:00','15:00':'15:00','16:00':'16:00','17:00':'17:00','18:00':'18:00','19:00':'19:00','20:00':'20:00','21:00':'21:00','22:00':'22:00','23:00':'23:00'
											}" />
											<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['cronHour']}" />
											<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
											<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
											<s:if test="#fieldHasFieldErrorVar">
										<span class="help-block text-danger">
											<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
										</span>
									</s:if>
									</div>
									<div class="col-xs-4">
										<div class="display-block">
											<label for="course_crontimezoneid" class="sr-only"><s:text name="label.crontimezoneid" /></label>
											<input
												type="text"
												placeholder="Location/City or time (ex. Rome or +1)"
												id="course-crontimezoneid"
												class="form-control"
												data-ref="#course-crontimezoneid-value"
												value="<s:property value="%{getSelectedItemTimezone(crontimezoneid).value}" />"
												/>
											<wpsf:hidden name="crontimezoneid" id="course-crontimezoneid-value" />
										</div>
										<%--
										<s:select list="timezones" name="crontimezoneid" id="course_crontimezoneid" listKey="key" listValue="value" cssClass="form-control"/>
										--%>
									</div>
								</div>
									<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['crontimezoneid']}" />
									<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
									<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
									<s:if test="#fieldHasFieldErrorVar">
										<span class="help-block text-danger">
											<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
										</span>
									</s:if>
							</div>
					</fieldset>

					<%-- save button --%>
						<div class="form-horizontal">
							<div class="form-group">
								<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
									<wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
										<span class="icon fa fa-floppy-o"></span>&#32;
										<s:text name="label.save" />
									</wpsf:submit>
									<span class="help-block small text-muted text-center" data-toggle="tooltip" data-placement="bottom" title="<s:date name="updatedat" format="EEEE dd/MMM/yyyy HH:mm:ss" nice="false" />">
										<s:text name="label.updatedat" />&#32;
										<s:date name="updatedat" format="dd/MM/yyyy HH:mm:ss" nice="true" />
									</span>
								</div>
							</div>
						</div>

			</div><%-- panel body //end --%>
		</div><%-- panel //end --%>


	</s:form>
</div>


