<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:if test="transformation"><s:text name="jppentaho.title.dataIntegration.ktrManagement" /></s:if><s:else><s:text name="jppentaho.title.dataIntegration.jobManagement" /></s:else></h1>

<h2><s:text name="jppentaho.title.processManagement.processResult" /></h2>

<s:set var="process" value="process" />

<s:text name="jppentaho.label.name"/>:
&#32;
<s:property value="#process.repositoryDirectory.path"/><s:if test="%{#process.repositoryDirectory.path.length() > 1}">/</s:if>
<s:property value="#process.name"/>

<br />

<s:text name="jppentaho.label.description"/>:
&#32;
<s:property value="#process.description"/>


<br />

<s:if test="hasActionErrors()">
<div class="message message_error">	
	<h3><s:text name="message.title.ActionErrors" /></h3>
	<ul>
		<s:iterator value="actionErrors">
			<li><s:property escape="false" /></li>
		</s:iterator>
	</ul>
</div>
</s:if>
<s:if test="hasActionMessages()">
<div class="message message_confirm">	
	<h3><s:text name="messages.confirm" /></h3>
	<ul>
		<s:iterator value="actionMessages">
			<li><s:property escape="false" /></li>
		</s:iterator>
	</ul>
</div>
</s:if>

