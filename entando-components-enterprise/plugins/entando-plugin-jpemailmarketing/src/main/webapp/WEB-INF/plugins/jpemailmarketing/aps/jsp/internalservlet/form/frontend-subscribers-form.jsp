<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpemailmarketing" uri="/jpemailmarketing-core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>


<%-- this is the result of /WEB-INF/plugins/jpemailmarketing/aps/jsp/widgets/jpemailmarketingForm.jsp --%>
<jpemailmarketing:form var="form" />

	<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Form/signup" />" method="post">
		<p class="sr-only hide noscreen">
			<wpsf:hidden name="strutsAction" />
			<%-- NO NEED OF COURSE ID BEACAUSE IS ALWAYS SETTED BY THE TAG --%>
		</p>
		<c:choose>
			<c:when test="${not empty form}">
				<h1 class="text-center"><wp:i18n key="jpemailmarketing_FORM_COURSE_TITLE_${form.courseId}" /></h1>
				<div class="row-fluid">
					<div class="span4">
						<jpemailmarketing:formCover var="cover_action" />
						<wp:info key="systemParam" paramName="applicationBaseURL" var="base_url" />
						<s:set var="base_url" value="%{#attr.base_url.endsWith('/') ? #attr.base_url.substring(0, #attr.base_url.length()-1) : #attr.base_url  }" scope="page" />
						<img class="pull-right thumbnail" src="<c:out value="${base_url}${cover_action}" />" />
					</div>
					<div class="span8 margin-medium-top">
						<%-- errors start --%>
							<s:if test="hasFieldErrors()||hasActionErrors()">
								<div class="row-fluid">
									<div class="span8">
										<s:if test="hasFieldErrors()">
											<div class="alert alert-error">
												<p><s:text name="message.title.FieldErrors" /></p>
												<ul>
													<s:iterator value="fieldErrors">
														<s:iterator value="value">
															<li><s:property/></li>
														</s:iterator>
													</s:iterator>
												</ul>
											</div>
										</s:if>
										<s:if test="hasActionErrors()">
											<div class="alert alert-error">
												<p><s:text name="message.title.ActionErrors" /></p>
												<ul>
													<s:iterator value="actionErrors">
														<li><s:property/></li>
													</s:iterator>
												</ul>
											</div>
										</s:if>
									</div>
								</div>
							</s:if>
						<%-- errors end --%>

						<%-- subscribe form --%>
							<s:if test="strutsAction == 100">
								<div class="row-fluid">
									<div class="span4">
										<label for="jpemailmarketing-form-name<c:out value="${form.courseId}" />"><wp:i18n key="jpemailmarketing_YOUR_NAME" escapeXml="false" /></label>
										<input type="text" name="name" value="<s:property value="name" />" id="jpemailmarketing-form-name<c:out value="${form.courseId}" />" />
									</div>
									<div class="span4">
										<label for="jpemailmarketing-form-email<c:out value="${form.courseId}" />">
											<wp:i18n key="jpemailmarketing_EMAIL_ADDRESS" escapeXml="false" />
										</label>
										<input type="text" name="email" value="<s:property value="email" />" id="jpemailmarketing-form-email<c:out value="${form.courseId}" />" />
									</div>
								</div>
								<div class="row-fluid">
									<div class="span8">
										<label class="checkbox">
											<wpsf:checkbox  name="joinCourse" id="joinCourse" ></wpsf:checkbox>
											<%--
											<input type="checkbox" name="joinCourse" checked="checked" value="receive" />
											 --%>
											<wp:i18n key="jpemailmarketing_INCENTIVE_TEXT_${form.courseId}" escapeXml="false" />
										</label>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span8">
										<div class="row-fluid margin-medium-top">
											<div class="span8">
												<wpsf:submit type="button" cssClass="btn btn-large btn-success">
													<wp:i18n key="jpemailmarketing_BUTTON_GETCOURSE" escapeXml="false" />
												</wpsf:submit>
											</div>
										<div class="row-fluid">
											<div class="span8">
												<small class="muted"><wp:i18n key="jpemailmarketing_NOSPAM_WARNING" escapeXml="false" /></small>
											</div>
										</div>
									</div>
								</div>
							</s:if>
						<%-- signup success message --%>
							<s:elseif test="strutsAction==200">
								<div class="alert alert-success">
									<wp:i18n key="jpemailmarketing_SIGNUP_SUCCESS_MESSAGE_${form.courseId}" escapeXml="false" />
								</div>
							</s:elseif>
							<%-- ERROR --%>
							<s:elseif test="strutsAction==400">
								<div class="alert alert-error">
									<wp:i18n key="jpemailmarketing_SIGNUP_ERROR_MESSAGE" escapeXml="false" />
								</div>
							</s:elseif>
							<s:elseif test="strutsAction==210">
								<div class="alert alert-success">
									<wp:i18n key="jpemailmarketing_UNSUBSCRIBE_SUCCESS_MESSAGE" escapeXml="false" />
								</div>
							</s:elseif>
							<s:elseif test="strutsAction==410">
								<div class="alert alert-error">
									<wp:i18n key="jpemailmarketing_UNSUBSCRIBE_ERROR_MESSAGE" escapeXml="false" />
								</div>
							</s:elseif>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<s:if test="hasFieldErrors()">
					<div class="alert alert-error">
						<p><s:text name="message.title.FieldErrors" /></p>
						<ul>
							<s:iterator value="fieldErrors">
								<s:iterator value="value">
									<li><s:property/></li>
								</s:iterator>
							</s:iterator>
						</ul>
					</div>
				</s:if>
				<s:if test="hasActionErrors()">
					<div class="alert alert-error">
						<p><s:text name="message.title.ActionErrors" /></p>
						<ul>
							<s:iterator value="actionErrors">
								<li><s:property/></li>
							</s:iterator>
						</ul>
					</div>
				</s:if>
				<div class="alert alert-info">
					<wp:i18n key="jpemailmarketing_FORM_NOT_FOUND" />
				</div>
			</c:otherwise>
		</c:choose>
	</form>

