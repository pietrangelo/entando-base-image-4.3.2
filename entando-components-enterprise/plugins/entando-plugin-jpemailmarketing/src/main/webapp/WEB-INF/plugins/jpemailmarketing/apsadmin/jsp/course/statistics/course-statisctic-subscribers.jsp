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
								<s:url action="configure" var="configureActionURL">
									<s:param name="id" value="id" />
								</s:url>
								<a href="<s:property value="#configureActionURL"/>" class="pull-right margin-base-left">
									<span class="icon fa fa-cog"></span>
									<s:text name="label.configure" />
								</a>
					</div></div>
			</div>

			<s:set var="subscribers" value="subscribersOccurences" />
			<s:set var="subscribers_confirmed" value="#subscribers.confirmed == null ? 0 : #subscribers.confirmed" />
			<s:set var="subscribers_unconfirmed" value="#subscribers.unconfirmed == null ? 0 : #subscribers.unconfirmed" />
			<s:set var="total" value="%{#subscribers_confirmed + #subscribers_unconfirmed}" />



			<div class="panel-body">
					<div class="progress padding-none margin-none thick">
							<s:set var="perc" value="#subscribers_confirmed * 100 / #total" />
							<div class="progress-bar progress-bar-success" style="width: <s:property value="#perc" />%">
								<s:property value="#perc" />% <s:text name="jpemailmarketing.label.confirmed" />
							</div>

							<s:set var="perc" value="#subscribers_unconfirmed * 100 / #total" />
							<div class="progress-bar progress-bar-primary" style="width: <s:property value="#perc" />%">
								<s:property value="#perc" />% <s:text name="jpemailmarketing.label.unconfirmed" />
							</div>
					</div>

					<form action="<s:url action="subscribers" namespace="/do/jpemailmarketing/Course/Statistics" />" class="form-horizontal margin-large-top">
						<%-- choose --%>
							<div class="form-group">
								<label for="subscribe-choose" class="sr-only"><s:text name="label.subscriber.status" /></label>
									<wpsf:hidden name="id" value="%{course.id}" />
									<div class="col-xs-12 col-md-6">
										<div class="input-group">
											<wpsf:select list="#{
													'': getText('jpemailmarketing.label.all'),
													'confirmed': getText('jpemailmarketing.label.confirmed'),
													'unconfirmed': getText('jpemailmarketing.label.unconfirmed')
													}"
													name="filterStatus"
													id="subscribe-choose"
													cssClass="form-control input-sm" />

											<span class="input-group-btn">
												<button type="submit" class="btn btn-default btn-sm" type="button">
													<span class="icon fa fa-filter"></span>
													&#32;
													<s:text name="label.filter" />
												</button>
											</span>
										</div><!-- /input-group -->
									</div><!-- /.col-lg-6 -->
									<div class="col-xs-12 col-md-6 text-right">
										<s:url action="exportSubscribers" var="subscribersCsvURL">
											<s:param name="id" value="id" />
										</s:url>
										<a href="<s:property value="#subscribersCsvURL"/>" class="btn btn-link">
											<span class="icon fa fa-download"></span>&#32;
											<s:text name="label.download.csv" />
										</a>
									</div>

							</div>
							<div class="table-responsive">
								<table class="table table-striped">
									<thead>
										<tr>
											<th class="text-right"><abbr title="Number">#</abbr></th>
											<th><s:text name="label.name" /></th>
											<th class="text-center"><s:text name="label.date" /></th>
											<th><s:text name="label.status" /></th>
										</tr>
									</thead>
									<s:iterator value="subscribers" var="subscriberid" status="status">
										<s:set var="c" value="%{getSubscriber(#subscriberid)}" />
										<tr>
												<%-- number --%>
													<td class="text-right">
														<span class="badge">
															<s:property value="#c.id" />
														</span>
													</td>
												<%-- name with email tooltip --%>
													<td>
														<span data-toggle="tooltip" data-placement="top">
															<a href="mailto:<s:property value="#c.email" />">
																<s:property value="#c.name" />
																&ensp;
																<s:property value="#c.email" />
															</a>
														</span>
													</td>
												<%-- date --%>
													<td class="text-center">
														<code>
															<s:date name="#c.createdat" format="dd/MM/yyyy HH:ss" />
														</code>
													</td>
												<%-- status --%>
													<td>
														<s:property value="#c.status" />
													</td>
										</tr>

										<div class="text-center">
											<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
										</div>

									</s:iterator>
								</table>
							</div>

					</form>


			</div><%-- panel body //end --%>
		</div><%-- panel //end --%>
</div><%-- main end --%>
