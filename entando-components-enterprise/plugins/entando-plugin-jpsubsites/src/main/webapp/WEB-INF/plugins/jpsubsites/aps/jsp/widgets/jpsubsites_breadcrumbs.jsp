<%@ taglib prefix="jpss" uri="/jpsubsites-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="breadcrumbs">
    <p class="noMargin">
        <c:set var="first" value="true" />
        <c:set var="thisPageCode"><wp:currentPage param="code" /></c:set>
        <jpss:breadcrumbs var="currentTarget">
            <c:set var="currentCode"><c:out value="${currentTarget.code}" /></c:set>
            <c:if test="${!currentTarget.voidPage}">
                <c:choose>
                    <c:when test="${thisPageCode == currentCode}"><c:if test="${first != 'true'}">&raquo; </c:if><c:out value="${currentTarget.title}" /></c:when>
                    <c:otherwise>
                        <c:if test="${first != 'true'}">&raquo; </c:if><a href="<c:out value="${currentTarget.url}" />" title="<c:out value="${currentTarget.title}" />"><c:out value="${currentTarget.title}" /></a>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:set var="first" value="false" />
        </jpss:breadcrumbs>
    </p>
</div>
