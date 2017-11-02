<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li class="margin-large-bottom"><a href="<s:url action="edit" namespace="/do/jpactiviti/Config" />" >Activiti configuration</a></li>
</wp:ifauthorized>