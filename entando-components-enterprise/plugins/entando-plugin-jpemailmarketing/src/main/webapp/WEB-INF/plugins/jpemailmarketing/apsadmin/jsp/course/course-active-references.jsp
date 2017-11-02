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

	<s:form>
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
					<wpsf:hidden name="id" />
					<wpsf:hidden name="forceSuspend" />
				</p>

				<%-- save button --%>
				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-xs-12 col-sm-12 col-md-12 margin-small-vertical">

							<div class="alert alert-warning alert-dismissable fade in">
                                <s:text name="note.disable.course.now.1" />
							</div>

                            <p>
                                <s:text name="note.disable.course.now.2" />
                            </p>


							<s:set var="courseMailOccurencesVar" value="courseMailOccurences" />
							<div class="table-responsive">
								<table class="table table-striped">
									<thead>
										<tr>
											<th><s:text name="label.subject" /></th>
											<th class="text-right">
                                                <s:text name="label.emailToSend" />
                                            </th>
										</tr>
									</thead>
									<s:iterator value="#courseMailOccurencesVar" var="occurr">
										<s:set var="c" value="%{getCourseEmail(#occurr.key)}" />
										<tr>
											<td>
												<span class="badge"><s:property value="#occurr.key" /></span>
												<s:property value="#c.emailSubject"/>
											</td>
											<td class="text-right"><s:property value="#occurr.value" /></td>
										</tr>
									</s:iterator>
								</table>
							</div>

						</div>

						<div class="col-xs-12 margin-small-vertical">
							<wpsf:submit type="button" action="forceSuspend" cssClass="btn btn-warning">
								<span class="icon fa fa-pause"></span>&#32;
									<s:text name="label.button.deactivate.suspend" />
							</wpsf:submit>
							<wpsf:submit type="button" action="configure" cssClass="btn btn-link text-left">
								<s:text name="label.button.deactivate.cancel" />
							</wpsf:submit>
						</div>

					</div>
				</div>

			</div><%-- panel body //end --%>
		</div><%-- panel //end --%>


	</s:form>
</div>


