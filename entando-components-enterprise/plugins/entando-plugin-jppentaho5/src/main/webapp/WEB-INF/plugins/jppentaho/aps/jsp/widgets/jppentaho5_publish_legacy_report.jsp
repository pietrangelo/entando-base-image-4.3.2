<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="ptho" uri="/jppentaho5-core" %>

<c:set var="argVar"><wp:currentWidget param="config" configParam="argsParam"/></c:set>
<c:set var="pathVar"><wp:currentWidget param="config" configParam="pathParam"/></c:set>
<c:set var="cssClassVar"><wp:currentWidget param="config" configParam="cssClassParam"/></c:set>
<link href="<wp:resourceURL />plugins/jppentaho5/administration/css/jppentaho5.css" rel="stylesheet" type="text/css">

<ptho:reportUrl var="urlVar" path="${pathVar}" csvArgs="${argVar}"/>

<!-- Pentaho debug information
path: ${pathVar} </br>
args: ${argVar} </br>
class: ${cssClassVar} </br>
 -->
 
<c:if test="${urlVar != 'none'}">
	<c:choose>
		<c:when test="${not empty cssClassVar}">
			<iframe width="100%"  height="1000px" src="${urlVar}" class="${cssClassVar}" frameborder=0>
			</iframe>
		</c:when>
		<c:otherwise>
			<iframe width="100%"  height="1000px" src="${urlVar}" class="jppentaho5" frameborder="0">
			</iframe>
		</c:otherwise>
	</c:choose>
</c:if>