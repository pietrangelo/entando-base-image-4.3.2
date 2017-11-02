<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="jpemailmarketing.name" /></a>
		&#32;/&#32;
		<s:text name="jpemailmarketing.title.courseManagement" />
	</span>
</h1>

<div id="main">
		<div class="panel panel-default">
			<div class="panel-heading display-block-float2">
					<div class="row"><div class="col-xs-12">
							<h2 class="h4 margin-none display-inline">
								<s:property value="course.name" />
							</h2>

							<%-- reports link --%>
								<s:include value="/WEB-INF/plugins/jpemailmarketing/apsadmin/jsp/course/_report-links-menu.jsp" />

							<%-- configure link --%>
								<s:url action="configure" var="configureActionURL">
									<s:param name="id" value="id" />
								</s:url>
								<a href="<s:property value="#configureActionURL"/>" class="pull-right margin-base-left">
									<span class="icon fa fa-cog"></span>
									<s:text name="label.configure" />
								</a>
					</div></div>
			</div>
			<div class="panel-body">
						<div class="panel-group" id="accordion">
							<s:set var="MotherHasFieldErrorsVar" value="hasFieldErrors" scope="request" />
							<s:set var="MotherfieldErrorsVar" value="fieldErrors" scope="request" />

							<s:iterator value="%{getCourseEmails(course.id)}" var="emailId" status="status">
								<s:if test="courseMailId == #emailId">
									<%-- active email form --%>
										<s:action name="back_edit" namespace="/do/jpemailmarketing/Course/CourseMail" executeResult="true">
											<s:param name="courseMailId" value="#emailId"></s:param>
											<s:param name="currentForm" value="true"></s:param>
										</s:action>
								</s:if>
									<s:else>
										<%-- others email forms --%>
											<s:action name="edit" namespace="/do/jpemailmarketing/Course/CourseMail" executeResult="true">
												<s:param name="courseMailId" value="#emailId"></s:param>
												<s:param name="currentForm" value="false"></s:param>
											</s:action>
									</s:else>
							</s:iterator>
						</div>
			</div><%-- panel body //end --%>
		</div><%-- panel //end --%>
</div><%-- main end --%>
