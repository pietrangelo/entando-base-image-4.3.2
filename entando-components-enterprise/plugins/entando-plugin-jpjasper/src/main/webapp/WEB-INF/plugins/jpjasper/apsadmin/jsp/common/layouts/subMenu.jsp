<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:ifauthorized permission="superuser">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpjasper.admin.menu" /></span>
    	<ul class="nav nav-pills nav-stacked">
	    	<li>
	    		<a href="<s:url action="config" namespace="/do/jpjasper/Config" />" ><s:text name="jpjasper.title.Configuration" /></a>
			</li>
    	</ul>
	</li>
</wp:ifauthorized>