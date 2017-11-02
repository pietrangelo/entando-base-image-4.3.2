<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li>
		
		<a href="<s:url namespace="/do/jppentaho5/Config" action="edit" />">
			<s:text name="jppentaho5.admin.menu" />
		</a>
	</li>
</wp:ifauthorized>