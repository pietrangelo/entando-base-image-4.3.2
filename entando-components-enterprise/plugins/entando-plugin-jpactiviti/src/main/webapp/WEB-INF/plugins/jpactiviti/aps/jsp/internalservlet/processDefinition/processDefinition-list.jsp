<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:url paramRepeat="true" var="currentPageVar"/>
<wp:pageWithWidget widgetTypeCode="jpactiviti_task_entry" var="taskEntryPageVar"  listResult="false" />
<wp:pageWithWidget widgetTypeCode="jpactiviti_processes" var="processInstanceListPageVar"  listResult="false" />


<div>
	<b>PROCESS Definitions Response List</b><br>
	<table border="1">
		<tr>
			<td><b>id</b></td>
			<td><b>name</b></td>
			<td><b>description</b></td>
			<td></td>
		</tr>	
		<s:iterator value="%{getProcessDefinitionResponseList()}" >
			<tr>
				<td><s:property value="id" /></td> 
				<td><s:property value="name" /></td>
				<td><s:property value="description" /></td>	
				<td>
					<form action="<wp:action path="/ExtStr2/do/jpactiviti/Front/ProcessDefinition/startProcessInstance.action" />" method="post">
							<input type="hidden" name="processDefinitionId" value="<s:property value="id" />" />
							<input type="hidden" name="redirectUrl" value="<wp:url page="${processInstanceListPageVar.code}"/>" />
							<input type="hidden" name="redirectUrlTaskEntry" value="<wp:url page="${taskEntryPageVar.code}"/>" />
							<wpsf:submit name="submit" value="Start" ></wpsf:submit>
					</form>					
				</td>		
			</tr>	
		</s:iterator>	
	</table>
 </div>
 <br>
 <div>
 	<form action='<wp:action path="/ExtStr2/do/jpactiviti/Front/ProcessDefinition/createNewDeployment.action" />' method="post" enctype="multipart/form-data" >
 		<label for="attachment">Upload New Process Definiton</label>
 		<input type="hidden" name="redirectUrl" value="<c:url value='${currentPageVar}'/>" />
		<input type="file" id="attachment" name="attachment" value="Browse" />
		<wpsf:submit name="submit" value="Upload" ></wpsf:submit> 	
 	</form>
 </div>


 
  