<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<s:set var="searchText" value="searchText" />
<wp:headInfo type="CSS" info="../../plugins/jpsalesforce/static/css/jpsalesforce.css" />

<div class="jpsaleforce-lead-list">
	<h2 class="title"><wp:i18n key="jpsalesforce_LEAD_SEARCH_TITLE" /></h2>
	<div class="row-fluid">
		<div class="span12 padding-medium-top">
			<s:if test="logged">
				<form
					action="<wp:action path="/ExtStr2/do/salesforce/leads/search.action"/>"
					class="form-inline"
					>
						<label
							for="jpsalesforce-lead-list-searc-field-text"
							class="noscreen sr-only"
							><wp:i18n key="jpsalesforce_SEARCH" />
						</label>
						<wpsf:textfield
							name="searchText"
							id="jpsalesforce-lead-list-searc-field-text"
							/>
						<button type="submit" class="btn">
							<wp:i18n key="jpsalesforce_SEARCH" />
							&#32;
							<span class="icon-search"></span>
						</button>
				</form>
				<p>
				<%-- add lead button --%>
					<wp:currentPage param="code" var="currentPageCode" />
					<wp:pageWithWidget var="newPage" widgetTypeCode="jpsalesforce_lead_new" />
					<c:set var="newPageCode" value="${newPage.code}" />
					<a
						class="btn btn-primary"
						href="<wp:url page="${newPageCode}">
							<wp:parameter name="pageCode"><c:out value="${currentPageCode}" /></wp:parameter>
							<wp:parameter name="searchText"><s:property value="#searchText" /></wp:parameter>
						</wp:url>"
						>
							<span class="icon-plus icon-white"></span>&#32;
							<wp:i18n key="jpsalesforce_LEAD_NEW" />
					</a>
				</p>

				<s:set var="leadList" value="leads" />
				<s:if test="%{#leadList.size() > 0}">
							<wp:pager listName="leadList" objectName="groupLeads" max="15" pagerIdFromFrame="true" advanced="true" offset="5">
								<c:set var="group" value="${groupLeads}" scope="request" />
								<table class="table table-striped">
									<tr>
										<th><abbr title="jpsalesforce_ACTIONS">&ndash;</abbr></th>
										<th><wp:i18n key="jpsalesforce_FIELD_NAME" /></th>
										<th><wp:i18n key="jpsalesforce_FIELD_COMPANY" /></th>
									</tr>
									<s:iterator value="#leadList" var="leadVar" begin="%{#attr.groupLeads.begin}" end="%{#attr.groupLeads.end}">
										<s:set var="leadDetailVar" value="%{getLead(#leadVar)}" />
										<tr>
											<th>
												<div class="jpsalesforce-nowrap">
													<%-- edit button --%>
														<a
															title="<wp:i18n key="jpsalesforce_LEAD_EDIT" />: <s:property value="#leadDetailVar.name" /> (<s:property value="#leadVar" />)"
															href="<wp:action path="/ExtStr2/do/salesforce/leads/edit.action"><wp:parameter name="searchText"><s:property value="#searchText" /></wp:parameter><wp:parameter name="sobj"><s:property value="#leadVar" /></wp:parameter></wp:action>"
															><span class="icon-edit"></span><span class="sr-only noscreen"><wp:i18n key="jpsalesforce_LEAD_EDIT" /></span></a>
													<%-- trash button --%>
														<a
															title="<wp:i18n key="jpsalesforce_LEAD_TRASH" />: <s:property value="#leadDetailVar.name" /> (<s:property value="#leadVar" />)"
															href="<wp:action path="/ExtStr2/do/salesforce/leads/trash.action"><wp:parameter name="searchText"><s:property value="#searchText" /></wp:parameter><wp:parameter name="sobj"><s:property value="#leadVar" /></wp:parameter></wp:action>"
															><span class="icon-trash"></span><span class="sr-only noscreen"><wp:i18n key="jpsalesforce_LEAD_TRASH" /></span></a>
												</div>
											</th>
											<td>
												<a
													class="jpsalesforce-nowrap"
													title="<wp:i18n key="jpsalesforce_LEAD_VIEW" />: <s:property value="#leadDetailVar.name" /> (<s:property value="#leadVar" />)"
													href="<wp:action path="/ExtStr2/do/salesforce/leads/view.action"><wp:parameter name="searchText"><s:property value="#searchText" /></wp:parameter><wp:parameter name="sobj"><s:property value="#leadVar" /></wp:parameter></wp:action>"
													>
														<s:property value="#leadDetailVar.name" />
												</a>
											</td>
											<td>
												<a
													class="jpsalesforce-nowrap"
													title="<wp:i18n key="jpsalesforce_LEAD_VIEW" />: <s:property value="#leadDetailVar.name" /> (<s:property value="#leadVar" />)"
													href="<wp:action path="/ExtStr2/do/salesforce/leads/view.action"><wp:parameter name="searchText"><s:property value="#searchText" /></wp:parameter><wp:parameter name="sobj"><s:property value="#leadVar" /></wp:parameter></wp:action>"
													>
														<s:property value="#leadDetailVar.getAttribute('Company')" />
												</a>
											</td>
										</tr>
									</s:iterator>
								</table>
								<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
							</wp:pager>
				</s:if>
				<s:else>
					<div class="alert alert-info">
						<wp:i18n key="jpsalesforce_LEAD_SEARCH_NO_RESULT" />
					</div>
				</s:else>
			</s:if>
			<s:else>
				<s:include value="/WEB-INF/plugins/jpsalesforce/aps/jsp/internalservlet/inc/sign-in-message.jsp" />
			</s:else>
		</div>
	</div>
</div>
