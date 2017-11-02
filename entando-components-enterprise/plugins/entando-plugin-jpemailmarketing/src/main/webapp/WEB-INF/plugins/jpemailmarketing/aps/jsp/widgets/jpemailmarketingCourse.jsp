<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="jpemailmarketing" uri="/jpemailmarketing-core"%>

<jpemailmarketing:course var="course" />
<article>
	<c:choose>
		<c:when test="${not empty course}">
	<h1><wp:i18n key="jpemailmarketing_COURSE_ID" />: <c:out value="${course.id}" /></h1>
			<ul>
				<li>
			<wp:i18n key="jpemailmarketing_COURSE_NAME" />: <c:out value="${course.name}" /><br />
			<wp:i18n key="jpemailmarketing_COURSE_SENDER" />: <c:out value="${course.sender}" /><br />
			<wp:i18n key="jpemailmarketing_COURSE_ACTIVE" />: <c:out value="${course.active}" /><br />
			<wp:i18n key="jpemailmarketing_COURSE_CRONEXP" />: <c:out value="${course.cronexp}" /><br />
			<wp:i18n key="jpemailmarketing_COURSE_CRONTIMEZONEID" />: <c:out value="${course.crontimezoneid}" /><br />
			<wp:i18n key="jpemailmarketing_COURSE_CREATEDAT" />: <c:out value="${course.createdat}" /><br />
			<wp:i18n key="jpemailmarketing_COURSE_UPDATEDAT" />: <c:out value="${course.updatedat}" /><br />
				</li>
			</ul>
		</c:when>
		<c:otherwise>
			<div class="alert alert-error">
		<p><wp:i18n key="jpemailmarketing_COURSE_NOT_FOUND" /></p>
			</div>
		</c:otherwise>
	</c:choose>
</article>