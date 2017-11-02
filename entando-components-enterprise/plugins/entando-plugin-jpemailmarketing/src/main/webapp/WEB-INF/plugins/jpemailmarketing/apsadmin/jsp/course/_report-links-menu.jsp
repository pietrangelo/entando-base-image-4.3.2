<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

						<%-- reports link --%>
								<div class="btn-group pull-right">
									<s:url action="emails" namespace="/do/jpemailmarketing/Course/Statistics" var="statsByEmailURL">
										<s:param name="id" value="course.id" />
									</s:url>
									<s:url action="subscribers" namespace="/do/jpemailmarketing/Course/Statistics" var="statsBySubscribersURLConfirmed">
										<s:param name="id" value="course.id" />
										<s:param name="filterStatus">confirmed</s:param>
									</s:url>
									<s:url action="subscribers" namespace="/do/jpemailmarketing/Course/Statistics" var="statsBySubscribersURLUnconfirmed">
										<s:param name="id" value="course.id" />
										<s:param name="filterStatus">unconfirmed</s:param>
									</s:url>
									<s:url action="subscribers" namespace="/do/jpemailmarketing/Course/Statistics" var="statsBysubscribersURL">
										<s:param name="id" value="course.id" />
									</s:url>

									<span class="cursor-pointer text-primary pull-right margin-base-left" data-toggle="dropdown">
										<span class="icon fa fa-bar-chart-o"></span>
										<s:text name="jpemailmarketing.title.reports" /><span class="caret"></span>
									</span>
									<ul class="dropdown-menu" role="menu">
										<li>
											<a href="<s:property value="#statsByEmailURL"/>">
												<s:text name="jpemailmarketing.menu.emails" />
											</a>
										</li>
										<li class="divider"></li>
										<li>
											<a href="<s:property value="#statsBysubscribersURL"/>">
												<s:text name="jpemailmarketing.menu.subscribers" />
											</a>
										</li>
										<li>
											<a href="<s:property value="statsBySubscribersURLConfirmed" escapeHtml="false" />">
												<s:text name="jpemailmarketing.menu.confirmed" />
											</a>
										</li>
										<li>
											<a href="<s:property value="statsBySubscribersURLUnconfirmed" escapeHtml="false" />">
												<s:text name="jpemailmarketing.menu.unconfirmed" />
											</a>
										</li>
									</ul>
								</div>
