<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%-- PCR custom hide this jpconfig menu --%>
<wp:ifauthorized permission="superuser" >
<li style="display: none;"><a href="<s:url action="entryPoint" namespace="/do/jpconfig" />" tabindex="<wpsa:counter />">System Config</a></li>
</wp:ifauthorized>