<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="labelCode" value="${(empty param.labelCode) ? null : param.labelCode}"  />
<c:set var="labelName" value="${(empty param.labelName) ? null : param.labelName}"  />
<s:set var="labelCode" value="#attr.labelCode" />
<s:set var="labelName" value="#attr.labelName" />
<s:if test="#labelCode != null">
	<s:set var="l" value="defaultLang" />
	<s:set var="labelName" value="%{(#labelName == null) ? #labelCode : #labelName}" />

	<s:set var="label" value="%{getLabel(#labelCode, courseId)}" />
	<s:set var="labelkey" value="#labelCode+ '_' + courseId" />
	<div class="form-group<s:property value="#controlGroupErrorClassVar" />">

			<label><s:property value="#labelName" /></label>
			<p class="form-control-static">
				<s:if test="null == #label">
					<s:url action="new" namespace="/do/LocaleString" var="newLabelActionUrl">
						<s:param name="key" value="#labelkey" />
					</s:url>
					<a id="jpemail-marketing-message-<s:property value="#labelCode" />" href="<s:property value="#newLabelActionUrl" />">
						<span class="sr-only"><s:text name="label.edit" /></span><span class="icon fa fa-edit"></span></a>
					<span class="text-muted"><s:property value="#labelkey" /></span>
				</s:if>
				<s:else>
						<s:url action="edit" namespace="/do/LocaleString" var="editLabelActionUrl">
							<s:param name="key" value="#labelkey" />
						</s:url>
						<a id="jpemail-marketing-message-<s:property value="#labelCode" />" href="<s:property value="#editLabelActionUrl" />">
							<span class="sr-only"><s:text name="label.edit" /></span><span class="icon fa fa-edit"></span></a>
						<s:property value="%{#label[#l.code]}" />
						<%--
						<label for="lang<s:property value="#l.code"/>">
							<span lang="<s:property value="#l.code" />"><s:property value="#l.descr" /></span>&#32;
							<span class="label label-success"><s:text name="label.default" /></span>
						</label>
						--%>
				</s:else>
			</p>
	</div>

</s:if>
