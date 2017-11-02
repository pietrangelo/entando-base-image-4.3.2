<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="pth" uri="/jppentaho5-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<%--<wp:headInfo type="JS_EXT" info="http://code.jquery.com/jquery-2.1.0.min.js" />--%>
<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.11.2/jquery-ui.js" />
<wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.min.css" />
<link href="<wp:resourceURL />plugins/jppentaho5/administration/css/jppentaho5.css" rel="stylesheet" type="text/css">

<c:set var="pathVar"><wp:currentWidget param="config" configParam="pathParam"/></c:set>
<c:set var="csvArgsVar"><wp:currentWidget param="config" configParam="argsParam"/></c:set>
<c:set var="cssClassVar"><wp:currentWidget param="config" configParam="cssClassParam"/></c:set>

<p>
	<wp:i18n key="jppentaho5_PUBLISH_REPORT" />
</p>

<%--
<c:out value="${pathVar}"/><br/>
<c:out value="${csvArgsVar}"/><br/>
--%>

<pth:report paramVar="parametersVar" urlVar="urlVar" path="${pathVar}" csvArgs="${csvArgsVar}" customPrefix="title">
	<wp:i18n key="jppentaho5_MESSAGE_NO_REPORT_TO_DISPLAY"></wp:i18n>
</pth:report>

<c:if test="${urlVar != 'none'}">
	<c:choose>
		<c:when test="${not empty cssClassVar}">
                    test
			<iframe width="100%" height="500px" src="${urlVar}&output-target=table/html;page-mode=stream" class="${cssClassVar}" frameborder=0>
			</iframe>
		</c:when>
		<c:otherwise>
                    test
			<iframe width="100%" height="500px" src="${urlVar}&output-target=table/html;page-mode=stream" frameborder=0 class="jppentaho5">
			</iframe>
		</c:otherwise>
	</c:choose>
</c:if>

<%--
    dropdown
    list
    radio
    checkbox
    togglebutton
    textbox
    datepicker
    multi-line
--%>