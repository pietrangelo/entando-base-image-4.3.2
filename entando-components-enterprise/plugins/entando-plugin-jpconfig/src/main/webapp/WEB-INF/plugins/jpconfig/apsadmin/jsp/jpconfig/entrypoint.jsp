<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="jpconfig.title.management" /></h1>

<div id="main">

<s:form action="viewItem">
	<p>
		<s:text name="jpconfig.label.items" />:<wpsf:select name="item" list="items" listKey="key" listValue="value"></wpsf:select>
	</p>
	<p>
		<s:submit value="%{getText('jpconfig.label.view')}" />
	</p>
</s:form>

</div>