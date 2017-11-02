<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li>
		
		<a href="<s:url namespace="/do/jpsalesforce/Config" action="edit" />">
			<s:text name="jpsalesforce.admin.menu" />
		</a>
	</li>
</wp:ifauthorized>
