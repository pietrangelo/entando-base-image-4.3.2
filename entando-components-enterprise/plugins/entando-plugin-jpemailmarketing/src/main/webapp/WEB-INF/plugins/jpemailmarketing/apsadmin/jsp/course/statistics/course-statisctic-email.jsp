<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="jpemailmarketing.name" /></a>
		&#32;/&#32;
		<s:text name="jpemailmarketing.title.courseManagement" />
		&#32;/&#32;
		<s:text name="jpemailmarketing.title.courseStats" />
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
								<s:url action="configure" namespace="/do/jpemailmarketing/Course" var="configureActionURL">
									<s:param name="id" value="id" />
								</s:url>
								<a href="<s:property value="#configureActionURL"/>" class="pull-right margin-base-left">
									<span class="icon fa fa-cog"></span>
									<s:text name="label.configure" />
								</a>
					</div></div>
			</div>



			<s:set var="courseEmails" value="courseMailOccurences" />
			<s:set var="total" value="%{0}" />
			<s:iterator value="#courseEmails" var="email"><s:set var="total" value="#total+#email.value" /></s:iterator>
			<div class="panel-body">
					<div class="progress padding-none margin-none thick">
						<s:iterator value="#courseEmails" var="email" status="s">
							<s:set var="currentPercent" value="%{ (#email.value*100) / (#total) }" />
							<div class="progress-bar <s:if test="#s.count%2"> progress-bar-success </s:if><s:else> progress-bar-darkgray </s:else>" style="width: <s:property value="#currentPercent" />%">
								<s:property value="#currentPercent" />%
							</div>
						</s:iterator>
					</div>

					<div class="form-horizontal margin-large-top">
						<%-- choose --%>
							<div class="form-group">
									<div class="col-xs-12 text-right">
										<s:url action="exportEmails" var="emailCsvURL">
											<s:param name="id" value="id" />
										</s:url>
										<a href="<s:property value="#emailCsvURL"/>" class="btn btn-link">
											<span class="icon fa fa-download"></span>&#32;
											<s:text name="label.download.csv" />
										</a>
									</div>
							</div>
							<div class="table-responsive">
								<table class="table table-striped">
									<thead>
										<tr>
											<th class="text-right"><abbr title="<s:text name="label.number" />">#</abbr></th>
											<th><s:text name="label.subject" /></th>
											<th class="text-center"><s:text name="label.emailsSent" /></th>
										</tr>
									</thead>
									<s:iterator value="courseEmails" var="emailId" status="s">
										<s:set var="c" value="%{getCourseEmail(#emailId)}" />
										<tr>
												<%-- number --%>
													<td class="text-right">
														<span class="badge">
															<s:property value="#s.count" />
														</span>
													</td>
												<%-- subject --%>
													<td>
														<s:property value="#c.emailSubject"/>
													</td>
												<%-- email sent --%>
													<td class="text-center">
														<code>
															<s:if test="null != #courseEmails[#emailId]">
																<s:property value="#courseEmails[#emailId]"/></s:if>
																<s:else>0</s:else>
														</code>
													</td>
										</tr>

										<div class="text-center">
											<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
										</div>

									</s:iterator>
								</table>
							</div>

					</div>


			</div><%-- panel body //end --%>
		</div><%-- panel //end --%>
</div><%-- main end --%>
