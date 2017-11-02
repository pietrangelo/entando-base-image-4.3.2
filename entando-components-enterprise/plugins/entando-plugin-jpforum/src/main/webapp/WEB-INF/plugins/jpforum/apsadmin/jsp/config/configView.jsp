<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="jpforum.title.forumManagement" /></h1>
<div id="main">
	<h2><s:text name="jpforum.configSummary" /></h2>   

	<dl class="table-display">
		<dt><s:text name="jpforum.label.postsPerPage" /></dt>
			<dd><s:property value="config.postsPerPage"/></dd>
		<dt><s:text name="jpforum.label.attachsPerPost" /></dt>
			<dd><s:property value="config.attachsPerPost"/></dd>
		<dt><s:text name="jpforum.label.profileTypecode" /></dt>
			<dd><s:property value="config.profileTypecode"/></dd>
		<dt><s:text name="jpforum.label.profileNickAttributeName" /></dt>
			<dd><s:property value="config.profileNickAttributeName"/></dd>
	</dl>
	
	<s:form action="edit">
			<p class="centerText"> 
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.edit')}" cssClass="button" /> 
			</p>
	</s:form> 
</div>