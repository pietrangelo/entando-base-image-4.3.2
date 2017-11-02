<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:url paramRepeat="true" var="currentPageVar"/>


   <div>
	<b>HISTORIC PROCESS INSTANCES</b> <br>
		<table border="1">
			<tr>
				<td><b>id</b></td>
				<td><b>processDefinitionId</b></td>
				<td><b>startTime</b></td>
				<td><b>endTime</b></td>
				<td><b>startUserId</b></td>			
			</tr>				
		
		<s:iterator value="%{getHistoricProcessInstanceResponseList()}" >
			<tr>
				<td>
						<a href='<wp:url page="${currentPageVar}">
						  					<wp:parameter name="historicProcessInstanceId"><c:out value="${id}" /></wp:parameter>
						  					
					  						</wp:url>' ><s:property value="id" /></a>
						
				</td>
				<td><s:property value="processDefinitionId" /> </td>
				<td><s:property value="startTime" /> </td>
				<td><s:property value="endTime" /> </td>
				<td><s:property value="startUserId" /> </td>			
			</tr>		
		</s:iterator>
		
		</table>	
   </div>
   <br>
   <c:choose>
   	<c:when test="${param.historicProcessInstanceId != null}">
   		
   		<b>HISTORIC TASK INSTANCES</b> <br>
		<table border="1">
			<tr>
				<td><b>id</b></td>
				<td><b>processInstanceId</b></td>
				<td><b>name</b></td>
				<td><b>description</b></td>
				<td><b>owner</b></td>
				<td><b>assignee</b></td>
				<td><b>startTime</b></td>
				<td><b>endTime</b></td>
				<td><b>claimTime</b></td>			
			</tr>				
		
		<s:iterator value="%{getHistoricTaskInstanceResponseList()}" >
			<tr>
				<td><s:property value="id" /></td>
				<td><s:property value="processInstanceId" /> </td>
				<td><s:property value="name" /> </td>
				<td><s:property value="description" /> </td>
				<td><s:property value="owner" /> </td>
				<td><s:property value="assignee" /></td>
				<td><s:property value="startTime" /></td>
				<td><s:property value="endTime" /></td>
				<td><s:property value="claimTime" /></td>			
			</tr>		
		</s:iterator>
		
		</table>
   		
   		
   	</c:when>   
   </c:choose>
   <br>

  