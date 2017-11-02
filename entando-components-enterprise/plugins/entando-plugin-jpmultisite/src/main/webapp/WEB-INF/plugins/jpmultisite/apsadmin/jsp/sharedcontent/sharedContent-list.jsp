<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jacmsapsadmin" uri="/jacms-apsadmin-core" %>
<%@ taglib prefix="jpmultisite-apsadmin" uri="/jpmultisite-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpsharedcontent.title.sharedContentManagement" />
	</span>
</h1>
<s:form action="list" cssClass="form-horizontal" role="search">
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
	<div class="form-group">
		<div class="input-group col-sm-12 col-md-12">
			<span class="input-group-addon">
				<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.id"/>"></span>
			</span>
			<label for="search-description" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>
			<wpsf:textfield
				id="search-description"
				name="descr"
				cssClass="form-control input-lg"
				title="%{getText('label.search.by')+' '+getText('label.description')}"
				placeholder="%{getText('label.description')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" name="search-id" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
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
					<label class="control-label col-sm-2 text-right" for="sharedContent_contentid">
						<s:text name="label.key"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="sharedContent_contentid"
							name="contentId"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="sharedContent_multisitecodesrc">
						<s:text name="label.multisitecodesrc"/>
					</label>
					<div class="col-sm-5">
						<%--
						<wpsf:textfield id="sharedContent_multisitecodesrc" name="multisiteCodeSrc" cssClass="form-control" />
						--%>
						<select id="sharedContent_multisitecodesrc" name="multisiteCodeSrc" class="form-control">
							<jpmultisite-apsadmin:multisiteList var="multisiteListVar" />
							<option>All</option>
							<c:forEach items="${multisiteListVar}" var="multisiteCodeVar" varStatus="status">
								<jpmultisite-apsadmin:multisite code="${multisiteCodeVar}" var="multisiteVar" />
								<option
									title="<c:out value="${multisiteVar.titles[currentLang.code]}"/> - (<c:out value="${multisiteVar.code}" />)"
									value="<c:out value="${multisiteVar.code}"/>"
									<s:if test="multisiteCodeSrc==#attr.multisiteVar.code">selected="selected"</s:if>
									>
										<c:out value="${multisiteVar.titles[currentLang.code]}" />
								</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<%--
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="sharedContent_multisitecodeto">
						<s:text name="label.multisitecodeto"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="sharedContent_multisitecodeto"
							name="multisiteCodeTo"
							cssClass="form-control" />
					</div>
				</div>
				--%>
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

<%--
<a href="<s:url action="new" />" class="btn btn-default">
	<span class="icon fa fa-plus-circle" />
	&#32;<s:text name="jpsharedcontent.sharedContent.label.new" />
</a>
--%>

<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="id" />
		<wpsf:hidden name="contentId" />
		<wpsf:hidden name="multisiteCodeSrc" />
		<wpsf:hidden name="multisiteCodeTo" />
	</p>

	<s:set var="sharedContentsId_list" value="sharedContentsId" />
	<s:if test="#sharedContentsId_list.size > 0">
	<wpsa:subset source="#sharedContentsId_list" count="10" objectName="groupSharedContents" advanced="true" offset="5">
	<s:set var="group" value="#groupSharedContents" />
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-1"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<s:if test="id!=null && id!=''"><th><s:text name="label.key" /></s:if>
				<th><s:text name="label.description" /></th>
				<th><s:text name="label.multisitecodesrc" /></th>
				<%--
				<th><s:text name="label.multisitecodeto" /></th>
				--%>
			</tr>
			<s:iterator var="id">
				<s:set name="sharedContentVar" value="%{getSharedContent(#id)}" />
				<s:url action="edit" var="editSharedContentActionVar"><s:param name="id" value="#sharedContentVar.id"/></s:url>
				<s:url action="trash" var="trashSharedContentActionVar"><s:param name="id" value="#sharedContentVar.id"/></s:url>
				<s:url action="copyContent" var="copyContentShared" namespace="/do/jpmultisite/Content"><s:param name="contentId" value="#sharedContentVar.contentId" /><s:param name="copyPublicVersion" value="'false'" /></s:url>
				<s:set var="content" value="%{getContentVo(#sharedContentVar.contentId)}"></s:set>
				<tr>
					<td class="text-center text-nowrap">
						<div class="btn-group btn-group-xs">
							<%-- share import --%>
								<a
									class="btn btn-default"
									title="<s:text name="label.shareImport" />: <s:property value="#sharedContentVar.contentId" /> - <s:property value="#content.descr" />" href="<s:property value="#copyContentShared" escapeHtml="false" />"
									>
										<span class="icon fa fa-fw fa-share"></span>
										<span class="sr-only">
											<s:text name="label.shareImport" />: <s:property value="#sharedContentVar.contentId" /> - <s:property value="#content.descr" /></span>
								</a>
						</div>
					</td>
					<s:if test="id!=null && id!=''">
						<td><s:property value="#sharedContentVar.contentId" /></td>
					</s:if>
					<td>
						<s:property value="#content.descr" />
					</td>
					<td>
						<s:set var="multisiteCodeVar" value="#sharedContentVar.multisiteCodeSrc" scope="page"/>
						<jpmultisite-apsadmin:multisite code="${multisiteCodeVar}" var="multisiteVar" />
						<c:out value="${multisiteVar.titles[currentLang.code]}" />
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
		<div class="alert alert-info margin-base-vertical">
			<s:text name="jpsharedcontent.sharedContent.message.list.empty" />
		</div>
	</s:else>
</s:form>
