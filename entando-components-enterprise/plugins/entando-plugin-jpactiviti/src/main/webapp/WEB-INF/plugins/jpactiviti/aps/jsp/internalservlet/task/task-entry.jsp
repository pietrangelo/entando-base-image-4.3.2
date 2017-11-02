<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- 
<wp:headInfo type="ACTIVITI_CSS_RESOURCES" info="bower_components/bootstrap/dist/css/bootstrap.min.css" />
<wp:headInfo type="ACTIVITI_CSS_RESOURCES" info="bower_components/bootstrap/dist/css/bootstrap-theme.min.css" />
<wp:headInfo type="ACTIVITI_CSS_RESOURCES" info="bower_components/pickadate/lib/compressed/themes/classic.css" />
<wp:headInfo type="ACTIVITI_CSS_RESOURCES" info="bower_components/pickadate/lib/compressed/themes/classic.date.css" />


<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/jquery/dist/jquery.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.concat.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/pickadate/lib/compressed/picker.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/pickadate/lib/compressed/picker.date.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/bootstrap/dist/js/bootstrap.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/angular/angular.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/angular-sanitize/angular-sanitize.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/angular-resource/angular-resource.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/tv4/tv4.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/objectpath/lib/ObjectPath.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/angular-schema-form/dist/schema-form.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/angular-schema-form/dist/bootstrap-decorator.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="bower_components/angular-schema-form-datepicker/bootstrap-datepicker.min.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="app.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="directives/schema.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="directives/data.js" />
<wp:headInfo type="ACTIVITI_JS_RESOURCES" info="services/services.js" />
 -->
<wp:pageWithWidget widgetTypeCode="jpactiviti_task_entry" var="taskEntryPageVar"  listResult="false" />
<wp:pageWithWidget widgetTypeCode="jpactiviti_tasks" var="taskListPageVar"  listResult="false" />
<input id="taskId" type="hidden" name="taskId" value="<c:out value='${param.taskId}'/>" />
<!-- 
<div class="container">
			<div class="row">
				<div class="col-md-12">
					<h1>The Test</h1>
					<p>Little test to check dynamic form building</p>
				</div>
				
				<div class="col-xs-12 col-sm-6 col-md-8" ng-app="schema">
					<display-widget></display-widget>
					<schema-widget></schema-widget>
				</div>
			</div>
</div>
 -->
 
 <s:set var="currentTask" value="%{getCurrentTask()}" />
    <b>TASK DETAILS</b><br>
 	<table border="1">
		<tr>
			<td><b>id</b></td>
			<td><b>name</b></td>
			<td><b>assignee</b></td>
			<td><b>description</b></td>
			<td><b>processDefinitionId</b></td>	
			<td><b>processInstanceId</b></td>			
		</tr>
		<tr>
		    <td><s:property value="#currentTask.id" /></td>
			<td><s:property value="#currentTask.name" /></td> 
	    	<td><s:property value="#currentTask.assignee" /></td>
	    	<td><s:property value="#currentTask.description" /></td>
	    	<td><s:property value="#currentTask.processDefinitionId" /></td>
	    	<td><s:property value="#currentTask.processInstanceId" /></td>
		</tr>
	</table>
	<br>
		
 
 
 <form action="<wp:action path="/ExtStr2/do/jpactiviti/Front/Task/completeTaskEntry.action" />" method="post">
		
		<input type="hidden" name="taskId" value="<c:out value='${param.taskId}'/>" />
		<input type="hidden" name="redirectUrl" value='<wp:url page="${taskListPageVar.code}"/>' />
		<input type="hidden" name="redirectUrlTaskEntry" value="<wp:url page="${taskEntryPageVar.code}"/>" />
		
		<s:iterator value="%{getFormData().getFormProperties()}" >
			<c:choose>
				<c:when test="${type == 'boolean'}">
				 	
					<p>
						<label for='<s:property value="id" />'> <s:property value="name" />: </label><br>
						<input 
							type="radio" 
							name="<s:property value='id'/>" 
							id="<s:property value='id' />" 
							checked="checked"
							value="true" >Yes</input>	<br>
							<input 
							type="radio" 
							name="<s:property value='id'/>" 
							id="<s:property value='id' />" 
							value="false" >No</input>	<br>							
					</p>
				
				 <p>
				 
				 </p>
				</c:when>
				<c:when test="${type == 'date'}">
					
						<label for='<s:property value="id" />'> <s:property value="name" />: </label>
						<input 
							type="text" 
							name="<s:property value='id'/>" 
							id="<s:property value='id' />" 
							placeholder="dd/MM/yyyy"
							value="<s:property value='value' />" >
						</input>	
				
				</c:when>
				<c:otherwise>
					<p>
						<label for='<s:property value="id" />'> <s:property value="name" />: </label>
						<input 
							type="text" 
							name="<s:property value='id'/>" 
							id="<s:property value='id' />" 
							
							value="<s:property value='value' />" >
						</input>								
					</p>
				</c:otherwise>
			</c:choose>	
		</s:iterator>	
			
		<wpsf:submit name="submit" value="Submit" ></wpsf:submit>
	
	
</form>



  


