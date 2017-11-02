<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<wp:ifauthorized permission="superuser">
    <li class="margin-large-bottom"><span class="h5"><s:text name="jpsocial.admin.menu" /></span>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="<s:url action="list" namespace="/do/jpsocial/SocialPost" />" tabindex="<wpsa:counter />"><s:text name="jpsocial.socialPost.list"/></a></li>
        </ul>
    </li>
</wp:ifauthorized>
