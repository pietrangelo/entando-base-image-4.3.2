<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:url paramRepeat="true" var="currentPageVar"/>
<wp:pageWithWidget widgetTypeCode="jpactiviti_task_entry" var="taskEntryPageVar"  listResult="false" />
<div>
 	
 		
	<div>
		<b>TASKS-INBOX</b><br>
		<table border="1">
			<tr>
				<td><b>id</b></td>
				<td><b>name</b></td>
				<td><b>assignee</b></td>
				<td><b>description</b></td>
				<td><b>processDefinitionId</b></td>	
				<td><b>processInstanceId</b></td>			
			</tr>
				
			<s:iterator value="%{getTaskResponseListAssignee()}" >
				
						<c:choose>
							<c:when test="${taskEntryPageVar != null}" >						
					  			<tr>
									<td>
										<a href='<wp:url page="${taskEntryPageVar.code}">
						  					<wp:parameter name="taskId"><c:out value="${id}" /></wp:parameter>
						  					<wp:parameter name="redirectUrl"><c:url value='${currentPageVar}'/></wp:parameter>
					  						</wp:url>' ><s:property value="id" /></a>
					  				</td> 
									<td><s:property value="name" /></td> 
									<td><s:property value="assignee" /></td>
									<td><s:property value="description" /></td>
									<td><s:property value="processDefinitionId" /></td>
									<td><s:property value="processInstanceId" /></td>			
								</tr>	
							</c:when>						
							<c:otherwise>						
					  			<tr>
									<td><s:property value="id" /></td> 
									<td><s:property value="name" /></td> 
									<td><s:property value="assignee" /></td>
									<td><s:property value="description" /></td>
									<td><s:property value="processDefinitionId" /></td>
									<td><s:property value="processInstanceId" /></td>			
								</tr>	
							</c:otherwise>	
						</c:choose>	
			</s:iterator>	
		</table>
 	</div>
 	
 	<br>
	<div>
		<b>TASKS-QUEUED</b><br>
		<table border="1">
			<tr>
				<td><b>id</b></td>
				<td><b>name</b></td>
				<td><b>assignee</b></td>
				<td><b>description</b></td>
				<td><b>processDefinitionId</b></td>
				<td><b>processInstanceId</b></td>
				<td></td>
			</tr>	
			<s:iterator value="%{getTaskResponseListCandidateUser()}" >
				<tr>
					<td><s:property value="id" /></td>
					<td><s:property value="name" /></td> 
					<td><s:property value="assignee" /></td>
					<td><s:property value="description" /></td>
					<td><s:property value="processDefinitionId" /></td>	
					<td><s:property value="processInstanceId" /></td>		
					<td>
						<form action="<wp:action path="/ExtStr2/do/jpactiviti/Front/Task/claimTask.action" />" method="post">
							<input type="hidden" name="taskId" value="<s:property value='id' />" />
							<input type="hidden" name="redirectUrl" value="<c:url value='${currentPageVar}'/>" />
							<wpsf:submit name="submit" value="Claim" ></wpsf:submit>
						</form>					
					</td>	
				</tr>	
			</s:iterator>	
		</table>
 	</div>
 	
		
 </div>
 
 
