<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpsubsites.admin.menu" /></span>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="<s:url action="edit" namespace="/do/jpsubsites/Config" />" ><s:text name="jpsubsites.config.menu" /></a></li>
            <li><a href="<s:url action="list" namespace="/do/jpsubsites/Subsite" />" ><s:text name="jpsubsites.subsite.menu" /></a></li>
        </ul>
    </li>
</wp:ifauthorized>