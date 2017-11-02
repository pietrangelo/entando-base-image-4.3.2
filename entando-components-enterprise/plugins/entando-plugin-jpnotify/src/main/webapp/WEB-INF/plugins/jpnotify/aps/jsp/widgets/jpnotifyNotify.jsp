<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="jpnotify" uri="/jpnotify-core"%>

<jpnotify:notify var="notify" />
<article>
<c:choose>
	<c:when test="${not empty notify}">
	<h1><wp:i18n key="jpnotify_NOTIFY_ID" />: <c:out value="${notify.id}" /></h1>
	<ul>
		<li>
			<wp:i18n key="jpnotify_NOTIFY_PAYLOAD" />: <c:out value="${notify.payload}" /><br />
			<wp:i18n key="jpnotify_NOTIFY_SENDDATE" />: <c:out value="${notify.senddate}" /><br />
			<wp:i18n key="jpnotify_NOTIFY_ATTEMPTS" />: <c:out value="${notify.attempts}" /><br />
			<wp:i18n key="jpnotify_NOTIFY_SENT" />: <c:out value="${notify.sent}" /><br />
			<wp:i18n key="jpnotify_NOTIFY_SENDER" />: <c:out value="${notify.sender}" /><br />
			<wp:i18n key="jpnotify_NOTIFY_RECIPIENT" />: <c:out value="${notify.recipient}" /><br />
		</li>
	</ul>
	</c:when>
	<c:otherwise>
	<div class="alert alert-error">
		<p><wp:i18n key="jpnotify_NOTIFY_NOT_FOUND" /></p>
	</div>
	</c:otherwise>
</c:choose>
</article>