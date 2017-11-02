<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1><s:if test="transformation"><s:text name="jppentaho.title.dataIntegration.ktrManagement" /></s:if><s:else><s:text name="jppentaho.title.dataIntegration.jobManagement" /></s:else></h1>

<h2><s:text name="jppentaho.title.processManagement.configure" /></h2>

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

<s:form action="run" >
	<s:if test="hasFieldErrors()">
<div class="message message_error">	
<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
</div>
	</s:if>
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
<p class="noscreen">
	<wpsf:hidden name="processName" />
	<wpsf:hidden name="directoryId" />
	<wpsf:hidden name="transformation" />
</p>


<s:set var="parameters" value="#process.listParameters()" />
<s:if test="#parameters==null || #parameters.size()==0" >
	<s:text name="jppentaho.note.parameters.none"/>
</s:if>
<s:else>

	<s:text name="jppentaho.title.process.parameters.caption"/>
	
	<br />
	
	<s:text name="jppentaho.label.name"/>
	
	&#32;
	
	<s:text name="jppentaho.label.description"/>
	
	&#32;
	
	<s:text name="jppentaho.label.value"/>
	
	<br />
	
	<s:iterator var="param" value="#parameters" >
		<s:property value="%{#param}"/>
		
		&#32;
		
		<s:property value="%{#process.getParameterDescription(#param)}"/>
		
		&#32;
		
		<wpsf:textfield name="params['%{#param}']" id="params_%{#param}" />
		
		<s:property value="%{params(#param)}"/>
		
		&#32;
		
		<br />
		
	</s:iterator>
</s:else>

<wpsf:submit value="%{getText('jppentaho.label.run')}" cssClass="button" />

</s:form>