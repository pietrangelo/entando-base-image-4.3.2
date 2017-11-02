<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li><a href="<s:url namespace="/do/jpnotify/Notify" action="list" />" ><s:text name="jpnotify.title.notifyManagement" /></a></li>
</wp:ifauthorized>
