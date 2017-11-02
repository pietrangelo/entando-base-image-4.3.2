<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="jpconfig.title.management" /></h1>

<div id="main">

<h2>[<s:property value="item" />]</h2>

<s:form action="saveItem">
	<s:if test="hasActionErrors()">
		<div class="message message_error">
		<h3><s:text name="message.title.ActionErrors" /></h3>	
			<ul>
			<s:iterator value="actionErrors">
				<li><s:property/></li>
			</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
		<h3><s:text name="message.title.FieldErrors" /></h3>	
			<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property/></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
	</s:if>

<p>
	<s:hidden name="item" />
</p>
	<p>
		<s:text name="jpconfig.label.config" />:<br />
		<s:textarea name="config" cols="75" rows="25" />
	</p>
	<p>
		<s:submit value="%{getText('jpconfig.label.save')}" />
	</p>
</s:form>

<h2><s:text name="jpconfig.saveItem.instructions" /></h2>

</div>