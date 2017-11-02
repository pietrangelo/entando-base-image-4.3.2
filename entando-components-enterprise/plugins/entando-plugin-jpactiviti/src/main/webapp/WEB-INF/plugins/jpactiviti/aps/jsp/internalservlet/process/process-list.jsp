<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:url paramRepeat="true" var="currentPageVar"/>

 <br>
 <div>
	<b>PROCESS INSTANCES</b><br>
	<table border="1">
		<tr>
			<td><b>id</b></td>
			<td><b>processDefinitionId</b></td>
			<td><b>complete</b></td>
		</tr>	
		<s:iterator value="%{getProcessInstanceResponseList()}" >
			<tr>
				<td>
					<a href='<wp:url page="${currentPageVar}">
						  					<wp:parameter name="processInstanceId"><c:out value="${id}" /></wp:parameter>
						  					
					  						</wp:url>' ><s:property value="id" /></a>
					
				</td> 
				<td><s:property value="processDefinitionId" /></td> 
				<td><s:property value="completed" /></td>			
			</tr>	
		</s:iterator>	
	</table>
 </div>
 <br>
 <c:choose>
   	<c:when test="${param.processInstanceId != null}">   		
   		<b>Process Instance Diagram</b> <br>
   		<img	src="<s:url action="processInstanceImage" namespace="/do/jpactiviti/Front/Process">
						<s:param  name="processInstanceId">${param.processInstanceId}</s:param>			
								</s:url>" />
		 		
   	</c:when>   
</c:choose>
   
