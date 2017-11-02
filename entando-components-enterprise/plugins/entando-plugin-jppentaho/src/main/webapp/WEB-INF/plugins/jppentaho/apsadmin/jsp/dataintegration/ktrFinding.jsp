<%@ taglib prefix="s" uri="/struts-tags" %>

<h1><s:text name="jppentaho.title.dataIntegration.ktrManagement" /></h1>


<s:if test="hasActionErrors()">
<div class="message message_error">	
<h2><s:text name="message.title.ActionErrors" /></h2>
	<ul>
		<s:iterator value="actionErrors">
			<li><s:property escape="false" /></li>
		</s:iterator>
	</ul>
</div>
</s:if>

<s:set var="processes" value="transformations" />
<s:if test="#processes==null || #processes.size()==0" >
	<s:text name="jppentaho.note.ktr.list.none"/>
</s:if>
<s:else>

	<s:text name="jppentaho.title.ktr.list.caption"/>
	
	<br />
	
	<s:text name="jppentaho.label.name"/>
	
	&#32;
	
	<s:text name="jppentaho.label.description"/>
	
	<br />
	
	<s:iterator var="process" value="#processes" >
	<a href="<s:url action="edit">
		<s:param name="processName" value="%{#process.name}" />
		<s:param name="directoryId" value="%{#process.repositoryDirectory.path}" />
		<s:param name="transformation" value="true" />
	</s:url>" title="<s:text name="jppentaho.label.run"/>: <s:property value="#process.name"/>" >
		<s:property value="#process.repositoryDirectory.path"/><s:if test="%{#process.repositoryDirectory.path.length() > 1}">/</s:if>
		<s:property value="#process.name"/>
	</a>
		
		&#32;
		
		<s:property value="#process.description"/>
		
		<br />
		
	</s:iterator>
</s:else>