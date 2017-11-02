<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:text name="title.configPage" />
	</span>
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
	<s:param name="selectedNode" value="currentPage.code"></s:param>
</s:action>

<s:form action="add" namespace="/do/jpsalesforce/Page/SpecialWidget/form" cssClass="form-horizontal">

<div class="panel panel-default">
	<div class="panel-heading">
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
	</div>

	<div class="panel-body">

		<h2 class="h5 margin-small-vertical">
			<label class="sr-only"><s:text name="name.widget" /></label>
			<span class="icon fa fa-puzzle-piece"></span>&#32;
			<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
			<%
				/* //FIXME:
					showlet.type.code: is null
					showlet.type.titles: is null
				*/
			%>
		</h2>

		<p class="sr-only">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" />
			<wpsf:hidden name="strutsAction" />
			<s:if test="%{strutsAction == 2}">
				<wpsf:hidden name="bindingId" />
			</s:if>
		</p>

		<%-- error //start --%>
			<s:if test="hasFieldErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.FieldErrors" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<s:if test="hasActionErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.ActionErrors" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="actionErrors">
							<li><s:property /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
		<%-- error //end --%>

			<fieldset class="col-xs-12 margin-large-top">

				<h4>
					<s:if test="%{!(strutsAction == 2)}">
						<s:text name="jpsaleforce.title.new.binding" />
					</s:if>
					<s:else>
						<s:text name="jpsaleforce.title.edit.binding" />
					</s:else>
				</h4>
				<s:if test="%{!(strutsAction == 2)}"><%-- struts action != 2 --%>
						<div class="form-group">
							<div class="col-xs-12 col-md-6 col-lg-6">
								<label for="jpsalesforce-salesforce-campaing-field-from"><s:text name="jpsalesforce.salesforce.field" /></label>
								<s:if test="%{profileFields.size > 0}">
									<wpsf:select
										name="bindLeadField"
										list="leadFields"
										headerKey="%{getNoBindingKey()}"
										headerValue="%{getNoBindingValue()}"
										id="jpsalesforce-salesforce-campaing-field-from"
										cssClass="form-control"
										/>
								</s:if>
								<s:else>
									<wpsf:select
										name="bindLeadField"
										list="leadFields"
										id="jpsalesforce-salesforce-campaing-field-from"
										cssClass="form-control"
										/>
								</s:else>
							</div>
							<div class="col-xs-12 col-md-6 col-lg-6">
								<s:if test="%{profileFields != null && profileFields.size > 0}">
									<label for="jpsalesforce-local-profile-bind-to"><s:text name="jpsalesforce.local.field" /></label>
									<s:select
										name="bindProfileField"
										list="profileFields"
										headerKey="%{getNoBindingKey()}"
										headerValue="%{getNoBindingValue()}"
										id="jpsalesforce-local-profile-bind-to"
										cssClass="form-control"
										/>
								</s:if>
							</div>
						</div>
				</s:if>

				<s:else><%-- struts action 2 --%>
					<s:set var="bindingVar" value="%{getBinding(bindingId)}" />
					<s:iterator value="langs" var="langVar" status="stat">
						<s:hidden name="bindLeadField" value="anythingWillFit" />
						<s:hidden name="bindProfileField" value="anythingWillFit" />
					</s:iterator>
				</s:else>

				<%-- labels --%>
					<s:iterator value="langs" var="langVar" status="stat">
						<div class="form-group">
							<div class="col-xs-12">
								<label for="<s:property value="%{'jpsalesforce-mapping-descr-'+#langVar.code}" />"><s:property value="%{#langVar.descr}" /></label>
								<wpsf:textfield
									id="%{'jpsalesforce-mapping-descr-'+#langVar.code}"
									name="label_%{#langVar.code}"
									value="%{(strutsAction == 2) ? getLabel(#langVar.code) : '' }"
									cssClass="form-control"
									/>
							</div>
						</div>
					</s:iterator>

				<%-- mandatory --%>
					<div class="form-group">
						<div class="col-xs-12">
							<div class="checkbox">
								<label>
									<s:checkbox
										name="mandatory"
										value="%{(strutsAction == 2) ? #bindingVar.mandatory : false }"
										/>
				  				<s:text name="jpsalesforce.label.mandatory" />
			  				</label>
							</div>
						</div>
					</div>

				<%-- add/save mapping --%>
					<div class="form-group">
						<div class="col-xs-12">
							<s:if test="%{strutsAction == 2}">
								<wpsf:submit type="button" cssClass="btn btn-default">
									<s:text name="label.confirm" />
								</wpsf:submit>
								<a
									href="<s:url action="cancel"><s:param name="pageCode" value="%{pageCode}" /><s:param name="frame" value="%{frame}" /><s:param name="widgetTypeCode" value="%{widgetTypeCode}" /></s:url>"
									title="<s:text name="jpsaleforce.label.binding.cancel" />"
									cssClass="btn btn-link"
									>
									<s:text name="label.reset" />
								</a>
							</s:if>
							<s:else>
								<wpsf:submit type="button" cssClass="btn btn-default">
									<span class="icon fa fa-plus-square"></span>&#32;
									<s:text name="jpsaleforce.label.add.binding" />
								</wpsf:submit>
							</s:else>
						</div>
					</div>

					<hr />

					<div class="margin-large-top">
							<s:if test="%{bindings.size > 0}">
								<div class="table-responsive">
									<table class="table table-bordered">
										<caption>
											<s:text name="jpssalesforce.label.bindings.list" />
										</caption>
										<tr>
											<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><span title="<s:text name="label.actions" />">&mdash;</span></th>
											<th class="text-left"><s:text name="jpsalesforce.field" /></th>
											<th class="text-left"><s:text name="jpsalesforce.salesforce.field" /></th>
											<th class="text-left"><s:text name="jpsalesforce.local.field" /></th>
											<th class="text-center"><abbr title="<s:text name="Entity.attribute.flag.mandatory.full" />"><s:text name="Entity.attribute.flag.mandatory.short" /></abbr></th>
										</tr>
										<s:iterator value="bindings" var="bindingVar" status="stat">
											<tr>
												<%-- actions --%>
													<td class="text-center text-nowrap">
														<div class="btn-group btn-group-xs">
															<%-- edit --%>
																<a
																	href="<s:url action="edit" namespace="/do/jpsalesforce/Page/SpecialWidget/form"><s:param name="bindingId" value="%{#stat.count - 1}"/><s:param name="pageCode" value="%{pageCode}" /><s:param name="frame" value="%{frame}" /><s:param name="widgetTypeCode" value="%{widgetTypeCode}" /></s:url>"
																	title="<s:text name="jpsalesforce.label.binding.edit" /> "
																	class="btn btn-default"
																	>
																		<span class="icon fa fa-pencil-square-o"></span>
																		<span class="sr-only"><s:text name="jpsalesforce.label.binding.edit" /></span></a>

																<button type="submit" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
																	<span class="caret"></span>
																</button>
																<ul class="dropdown-menu text-left" role="menu">
																	<li>
																		<%-- up --%>
																			<a
																				href="<s:url action="up" namespace="/do/jpsalesforce/Page/SpecialWidget/form"><s:param name="bindingId" value="%{#stat.count - 1}"/><s:param name="pageCode" value="%{pageCode}" /><s:param name="frame" value="%{frame}" /><s:param name="widgetTypeCode" value="%{widgetTypeCode}" /></s:url>"
																				title="<s:text name="jpsalesforce.label.binding.moveUp" />"
																				>
																					<span class="icon fa fa-sort-desc fa-fw"></span>
																					<s:text name="jpsalesforce.label.binding.moveUp" />
																			</a>
																	</li>
																	<li>
																		<%-- down --%>
																			<a
																				href="<s:url action="down" namespace="/do/jpsalesforce/Page/SpecialWidget/form"><s:param name="bindingId" value="%{#stat.count - 1}"/><s:param name="pageCode" value="%{pageCode}" /><s:param name="frame" value="%{frame}" /><s:param name="widgetTypeCode" value="%{widgetTypeCode}" /></s:url>"
																				title="<s:text name="jpsalesforce.label.binding.moveDown" />"
																				>
																					<span class="icon fa fa-sort-asc fa-fw"></span>
																					<s:text name="jpsalesforce.label.binding.moveDown" />
																			</a>
																	</li>
																</ul>
														</div>
														<div class="btn-group btn-group-xs">
															<%-- delete --%>
																<a
																	href="<s:url action="delete" namespace="/do/jpsalesforce/Page/SpecialWidget/form"><s:param name="bindingId" value="%{#stat.count - 1}"/><s:param name="pageCode" value="%{pageCode}" /><s:param name="frame" value="%{frame}" /><s:param name="widgetTypeCode" value="%{widgetTypeCode}" /></s:url>"
																	title="<s:text name="jpsalesforce.label.binding.delete" />"
																	class="btn btn-warning"
																	>
																		<span class="icon fa fa-times-circle-o"></span>
																		<span class="sr-only"><s:text name="jpsalesforce.label.binding.delete" /></span></a>
														</div>
													</td>
												<%-- labels --%>
													<td>
													<ul class="list-unstyled">
															<s:iterator value="langs" var="langVar">
																<li>
																	<span class="label label-default"><s:property value="#langVar.code" /></span>
																	<s:property value="#bindingVar.labels.get(#langVar.code)" />
																</li>
															</s:iterator>
													</ul>
													</td>
												<%-- fields --%>
													<td class="text-left">
															<code><s:property value="#bindingVar.leadField" /></code>
													</td>
													<td class="text-left">
															<code><s:property value="#bindingVar.profileField" /></code>
													</td>
												<%-- mandatory --%>
													<td class="text-center">
														<s:if test="#bindingVar.mandatory">
															<span title="<s:text name="label.mandatory" />: <s:text name="label.yes" />" class="icon fa fa-check text-warning"></span>
															<span class="sr-only"><s:text name="label.yes" /></span>
														</s:if>
														<s:else>
															<span class="sr-only"><s:text name="label.no" /></span>
														</s:else>
													</td>
											</tr>
										</s:iterator>
									</table>
								</div>
							</s:if>
					</div>
			</fieldset>
	</div>
</div>

<div class="form-group">
	<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
	
		<wpsf:submit action="saveConfig" type="button" cssClass="btn btn-default">
			<s:text name="label.continue" />
			&#32;
			<span class="icon fa fa-long-arrow-right"></span>
		</wpsf:submit>
		<%--
		<wpsf:submit action="saveConfig" type="button" cssClass="btn btn-primary btn-block">
			<span class="icon fa fa-floppy-o"></span>&#32;
			<s:text name="label.save" />
		</wpsf:submit>
		--%>
	</div>
</div>


</s:form><%-- form end --%>
</div>
