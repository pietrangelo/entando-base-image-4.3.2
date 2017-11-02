<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpmultisite.title.multisiteManagement" />
	</span>
</h1>

<div id="main">
	<s:form action="list" cssClass="form-horizontal">
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<div class="form-group">
			<div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<span class="input-group-addon">
					<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.title"/>"></span>
				</span>
				<label for="search-title" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.code"/></label>
				<wpsf:textfield
					id="search-title"
					name="titles"
					cssClass="form-control input-lg"
					title="%{getText('label.search.by')+' '+getText('label.title')}"
					placeholder="%{getText('label.title')}" />
				<div class="input-group-btn">
					<wpsf:submit type="button" name="search-code" id="search-code" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
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
					<label class="control-label col-sm-2 text-right" for="multisite_titles">
						<s:text name="label.code"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="multisite_titles"
							name="code"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="multisite_descriptions">
						<s:text name="label.descriptions"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="multisite_descriptions"
							name="descriptions"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="multisite_url">
						<s:text name="label.url"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="multisite_url"
							name="url"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="multisite_headerImage">
						<s:text name="label.headerImage"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="multisite_headerImage"
							name="headerImage"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="multisite_templateCss">
						<s:text name="label.templateCss"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="multisite_templateCss"
							name="templateCss"
							cssClass="form-control" />
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

	<a class="btn btn-default margin-base-bottom" href="<s:url action="new" />">
		<span class="icon fa fa-plus-circle" />
		&#32;<s:text name="jpmultisite.multisite.label.new" />
	</a>


	<s:form action="search">
		<p class="sr-only">
			<wpsf:hidden name="code" />
			<wpsf:hidden name="titles" />
			<wpsf:hidden name="descriptions" />
			<wpsf:hidden name="url" />
			<wpsf:hidden name="headerImage" />
			<wpsf:hidden name="templateCss" />
			<wpsf:hidden name="catCode" />
		</p>

		<s:set var="multisitesId_list" value="multisitesId" />
		<s:if test="#multisitesId_list.size > 0">
			<wpsa:subset source="#multisitesId_list" count="10" objectName="groupMultisites" advanced="true" offset="5">
				<s:set name="group" value="#groupMultisites" />
				<div class="text-center">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>

				<div class="table-responsive">
					<table class="table table-bordered">
						<tr>
							<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
							<%-- <th><s:text name="label.code" /></th> --%>
							<th><s:text name="label.title" /></th>
							<%-- <th><s:text name="label.descriptions" /></th> --%>
							<th><s:text name="label.url" /></th>
						</tr>
						<wp:info key="langs" var="langs" />
						<wp:info key="defaultLang" var="defaultLang" />
						<s:set var="langs" value="#attr.langs" />
						<s:set var="defaultLang" value="#attr.defaultLang" />
						<s:iterator var="currentMultisiteCode">
							<s:set name="multisite" value="%{getMultisite(#currentMultisiteCode)}" />
							<s:url action="edit" var="editMultisiteActionVar"><s:param name="code" value="#multisite.code" /></s:url>
							<s:url action="entryClone" var="cloneMultisiteActionVar"><s:param name="code" value="#multisite.code" /></s:url>
							<s:url action="trash" var="trashMultisiteActionVar"><s:param name="code" value="#multisite.code" /></s:url>
							<tr>
								<td class="text-center text-nowrap">
									<div class="btn-group btn-group-xs">
										<%-- clone --%>
											<a
												class="btn btn-default"
												title="<s:text name="label.clone" />&#32;<s:property value="#multisite.code" />"
												href="<s:property value="#cloneMultisiteActionVar" escapeHtml="false" />"
												>
												<span class="sr-only"><s:text name="label.clone" />&#32;<s:property value="#multisite.code" /></span>
												<span class="icon fa fa-files-o"></span>
											</a>
									</div>
									<div class="btn-group btn-group-xs">
										<%-- edit --%>
											<a
												class="btn btn-default"
												title="<s:text name="label.edit" />&#32;<s:property value="#multisite.code" />"
												href="<s:property value="#editMultisiteActionVar" escapeHtml="false" />"
												>
												<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#multisite.code" /></span>
												<span class="icon fa fa-pencil-square-o"></span>
											</a>
									</div>
									<div class="btn-group btn-group-xs">
										<%-- remove --%>
											<a
												class="btn btn-warning"
												href="<s:property value="#trashMultisiteActionVar" escapeHtml="false" />"
												title="<s:text name="label.remove" />: <s:property value="#multisite.code" />"
												>
												<span class="sr-only"><s:text name="label.alt.clear" /></span>
												<span class="icon fa fa-times-circle-o"></span>&#32;
											</a>
									</div>
								</td>
								<%--
								<td>
									<code><s:property value="#multisite.code" /></code>
								</td>
								--%>
								<td title="<s:iterator value="#langs" var="l" status="statusVar"><s:if test="#statusVar.first"><s:set var="title" value="#multisite.titles.get(#l.code)" />[<s:property value="#l.descr" />] <s:property value="#title" />;</s:if><s:if test="#statusVar.count==2"><s:iterator begin="1" value="#langs" var="l" status="statusVar2"><s:set var="title" value="#multisite.titles.get(#l.code)" />
[<s:property value="#l.descr" />] <s:if test="#title==null">&mdash;;</s:if><s:else><s:property value="#title" />;</s:else></s:iterator></s:if></s:iterator>">
									<%--
									<s:iterator value="#langs" var="l" status="statusVar">
										<s:if test="#statusVar.first">
											<span class="margin-base-bottom display-block">
												<s:set var="title" value="#multisite.titles.get(#l.code)" />
												<abbr title="<s:property value="#l.descr" />" class="label label-default margin-base-bottom"><s:property value="#l.code" /></abbr>
												<s:property value="#title" />
												<s:if test="#langs.size()>1">
													&#32;<a data-toggle="collapse" href="#titles-<s:property value="#multisite.code"/>">&hellip;</a>
												</s:if>
											</span>
										</s:if>
										<s:if test="#statusVar.count==2">
											<div id="titles-<s:property value="#multisite.code"/>" class="collapse out extra-<s:property value="#multisite.code"/>">
												<s:iterator begin="1" value="#langs" var="l" status="statusVar2">
													<span class="margin-base-bottom display-block">
														<s:set var="title" value="#multisite.titles.get(#l.code)" />
														<abbr title="<s:property value="#l.descr" />" class="label label-default margin-base-bottom"><s:property value="#l.code" /></abbr>
														<s:if test="#title==null">
															<abbr title="empty title">&mdash;</abbr>
														</s:if>
														<s:else>
															<s:property value="#title" />
														</s:else>
														<s:if test="!#statusVar2.last"></s:if>
													</span>
												</s:iterator>
											</div>
										</s:if>
									</s:iterator>
									--%>
									<s:set var="title" value="#multisite.titles.get(#defaultLang)" />
									<span lang="<s:property value="#defaultLang" />"><s:property value="#title" /></span>
								</td>
								<%-- <td>
									<s:iterator value="#langs" var="l" status="statusVar">
										<s:if test="#statusVar.first">
											<span class="margin-base-bottom display-block">
												<s:set var="description" value="#multisite.descriptions.get(#l.code)" />
												<abbr title="<s:property value="#l.descr" />" class="label label-default margin-base-bottom"><s:property value="#l.code" /></abbr>
												<s:property value="#description" />
												<s:if test="#langs.size()>1">
													&#32;<a data-toggle="collapse" href="#description-<s:property value="#multisite.code"/>">&hellip;</a>
												</s:if>
											</span>
										</s:if>
										<s:if test="#statusVar.count==2">
											<div id="description-<s:property value="#multisite.code"/>" class="collapse out extra-<s:property value="#multisite.code"/>">
												<s:iterator begin="1" value="#langs" var="l" status="statusVar2">
													<span class="margin-base-bottom display-block">
														<s:set var="description" value="#multisite.descriptions.get(#l.code)" />
														<abbr title="<s:property value="#l.descr" />" class="label label-default margin-base-bottom"><s:property value="#l.code" /></abbr>
														<s:if test="#description==null">
															<abbr title="empty description">&mdash;</abbr>
														</s:if>
														<s:else>
															<s:property value="#description" />
														</s:else>
														<s:if test="!#statusVar2.last"></s:if>
													</span>
												</s:iterator>
											</div>
										</s:if>
									</s:iterator>
								</td>
								--%>
								<td class="text-nowrap">
									<a href="<s:property value="#multisite.url"/>">
										<span class="icon fa fa-globe"></span>&#32;
										<s:property value="#multisite.url"/>
									</a>
								</td>
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
			<div class="alert alert-info">
				<s:text name="jpmultisite.multisite.message.list.empty" />
			</div>
		</s:else>
	</s:form>

</div>
