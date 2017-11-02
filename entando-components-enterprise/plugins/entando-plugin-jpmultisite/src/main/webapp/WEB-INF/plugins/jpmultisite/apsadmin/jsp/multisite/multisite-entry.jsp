<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%-- setup --%>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="jpmultisite.title.multisiteManagement" /></a>
		&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="jpmultisite.multisite.label.new" />
		</s:if>
		<s:elseif test="getStrutsAction() == 2">
			<s:text name="jpmultisite.multisite.label.edit" />
		</s:elseif>
		<s:elseif test="getStrutsAction() == 3">
			CLONE <%-- <s:text name="jpmultisite.multisite.label.edit" /> --%>
		</s:elseif>
	</span>
</h1>
<div id="main">
	<s:form action="save" method="post" enctype="multipart/form-data">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none">
					<s:text name="message.title.FieldErrors" />
					&ensp;<span
						class="small text-danger icon fa fa-question-circle cursor-pointer"
						title="<s:text name="label.all" />"
						data-toggle="collapse"
						data-target="#content-error-messages"></span>
					<span class="sr-only"><s:text name="label.all" /></span>
				</h2>
				<ul id="content-error-messages" class="margin-base-top collapse">
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
				<button class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
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
				<%--<wpsf:hidden name="code" />--%>
			</s:if>
			<s:if test="getStrutsAction() == 3">
				<wpsf:hidden name="multisiteCloneSource" />
			</s:if>
		</p>
		<fieldset class="col-xs-12">
			<legend><s:text name="label.info" /></legend>
			<%-- code --%>
			<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['code']}" />
			<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
					<s:set var="attribute" value="#{'required': true}" />
					<label class="display-block" for="multisite_code"><s:text name="label.code" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
					<s:set var="attribute" value="%{null}" />
					<wpsf:textfield name="code" id="multisite_code" maxlength="3" size="3" cssClass="form-control" />
					<s:if test="#fieldHasFieldErrorVar">
						<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
					</s:if>
			</div>
			<s:set var="fieldFieldErrorsVar" value="%{null}" />
			<s:set var="fieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />

			<%-- titles --%>
				<s:iterator value="langs">
					<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['titles_'+code]}" />
					<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
					<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
							<s:set var="attribute" value="#{'required': true}" />
							<label class="display-block" for="titles_<s:property value="code" />">
								<abbr title="<s:property value="descr" />">
									<code class="label label-info"><s:property value="code" /></code>
								</abbr>
								&#32;
								<s:text name="label.title" />
								&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
							</label>
							<s:set var="attribute" value="%{null}" />
							<wpsf:textfield
								name="titles_%{code}"
								id="titles_%{code}"
								value="%{multisite.getTitles().get(code)}"
								cssClass="form-control" />
							<s:if test="#fieldHasFieldErrorVar">
								<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
							</s:if>
					</div>
					<s:set var="fieldFieldErrorsVar" value="%{null}" />
					<s:set var="fieldHasFieldErrorVar" value="%{null}" />
					<s:set var="controlGroupErrorClassVar" value="%{null}" />
				</s:iterator>

				<%-- descriptions --%>
					<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['descriptions']}" />
					<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
					<s:iterator value="langs">
						<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
									<label
										for="descriptions_<s:property value="code" />">
											<abbr title="<s:property value="desc" />"><code class="label label-info"><s:property value="code" /></code></abbr>
											&#32;
											<s:text name="label.description" />
									</label>
									<wpsf:textarea
										name="%{'descriptions_'+code}"
										id="%{'descriptions_'+code}"
										value="%{multisite.getDescriptions().get(code)}"
										cssClass="form-control" />
									<s:if test="#fieldHasFieldErrorVar">
										<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
									</s:if>
						</div>
					</s:iterator>
		</fieldset>

		<fieldset class="margin-large-top margin-base-bottom col-xs-12">
			<legend><s:text name="label.settings" /></legend>
						<%-- url --%>
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['url']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
						<s:set var="attribute" value="#{'required': true}" />
						<label class="display-block" for="multisite_url"><s:text name="label.url" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
						<s:set var="attribute" value="%{null}" />
						<wpsf:textfield name="url" id="multisite_url" cssClass="form-control" />
						<s:if test="#fieldHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
						</s:if>
				</div>
				<s:set var="fieldFieldErrorsVar" value="%{null}" />
				<s:set var="fieldHasFieldErrorVar" value="%{null}" />
				<s:set var="controlGroupErrorClassVar" value="%{null}" />

			<%-- headerImage --%>
				<s:set var="headerImage" value="headerImage" />
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['headerImage']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
						<label for="multisite_headerImage"><s:text name="label.headerImage" /></label>
								<div class="input-group">
									<s:file name="headerImageFile" cssClass="form-control cursor-pointer" id="multisite_headerImage" />
									<%-- preview --%>
										<s:if test="#headerImage!=null || #headerImage.size()>0">
											<span class="input-group-btn">
												<button
													href="#"
													id="jpmultisite-img-preview-button"
													class="btn btn-default"
													data-toggle="popover"
													data-html="true"
													data-placement="top"
													data-template="<div class='popover' role='tooltip'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content' style='width: 226px;'></div></div>"
													data-content="<img id='jpmultisite-img-preview-image' class='cursor-pointer' style='max-width: 200px;' src='<wp:resourceURL /><s:property value="#headerImage" />' /></a>"
													type="button">
														<s:text name="label.headerImage.preview" />&#32;<span class="icon fa fa-eye"></span>
													</button>
											</span>
										</s:if>
								</div><!-- /input-group -->
								<s:if test="#fieldHasFieldErrorVar">
									<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
								</s:if>
				</div>
				<s:set var="fieldFieldErrorsVar" value="%{null}" />
				<s:set var="fieldHasFieldErrorVar" value="%{null}" />
				<s:set var="controlGroupErrorClassVar" value="%{null}" />

			<%-- templateCss --%>
				<s:set var="templateCss" value="templateCss" />
				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['templateCss']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
						<label for="multisite_templateCss"><s:text name="label.templateCss" /></label>
						<div class="input-group">
							<s:file name="templateCssFile" cssClass="form-control cursor-pointer" id="multisite_templateCss" />
							<%-- preview --%>
								<s:if test="#templateCss!=null || #templateCss.size()>0">
									<span class="input-group-btn">
										<a
											class="btn btn-default"
											href="<wp:resourceURL /><s:property value="%{#templateCss}" />"
											>
												<s:text name="label.templateCss.preview" />&#32;<span class="icon fa fa-eye"></span>
											</a>
									</span>
								</s:if>
						</div><!-- /input group -->
						<s:if test="#fieldHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
						</s:if>
				</div>
				<s:set var="fieldFieldErrorsVar" value="%{null}" />
				<s:set var="fieldHasFieldErrorVar" value="%{null}" />
				<s:set var="controlGroupErrorClassVar" value="%{null}" />

				<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['multisiteAdminPassword']}" />
				<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
						<s:set var="attribute" value="#{'required': true}" />
						<label class="display-block" for="multisite_multisiteAdminPassword"><s:text name="label.multisiteAdminPassword" />&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
						<s:set var="attribute" value="%{null}" />
						<wpsf:password name="multisiteAdminPassword" id="multisite_multisiteAdminPassword" cssClass="form-control" />
						<s:if test="#fieldHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator></p>
						</s:if>
				</div>
				<s:set var="fieldFieldErrorsVar" value="%{null}" />
				<s:set var="fieldHasFieldErrorVar" value="%{null}" />
				<s:set var="controlGroupErrorClassVar" value="%{null}" />
		</fieldset>


			<%-- save button --%>
			<div class="form-horizontal">
				<div class="form-group">
					<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
						<s:if test="getStrutsAction() == 2 || getStrutsAction() == 1">
							<s:submit type="button" action="save" cssClass="btn btn-primary btn-block">
								<span class="icon icon-save"></span>&#32;
								<s:text name="label.save" />
							</s:submit>
						</s:if>
						<s:if test="getStrutsAction() == 3">
							<s:submit type="button" action="clone" cssClass="btn btn-primary btn-block">
								<span class="icon icon-save"></span>&#32;
								<s:text name="label.save" />
							</s:submit>
						</s:if>
					</div>
				</div>
			</div>
			</s:form>

		</div>
</div>
