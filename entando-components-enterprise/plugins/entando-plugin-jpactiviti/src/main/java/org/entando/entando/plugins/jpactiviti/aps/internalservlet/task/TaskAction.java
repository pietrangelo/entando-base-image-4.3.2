/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando Enterprise Edition software.
 * You can redistribute it and/or modify it
 * under the terms of the Entando's EULA
 * 
 * See the file License for the specific language governing permissions   
 * and limitations under the License
 * 
 * 
 * 
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package org.entando.entando.plugins.jpactiviti.aps.internalservlet.task;

import java.util.List;

import org.activiti.rest.service.api.form.FormDataResponse;
import org.activiti.rest.service.api.form.RestFormProperty;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.entando.entando.plugins.jpactiviti.aps.system.services.TaskResponse;
import org.entando.entando.plugins.jpactiviti.aps.system.AbstractActivitiAction;
import org.entando.entando.plugins.jpactiviti.aps.system.services.IActivitiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



/**
 * 
 */
public class TaskAction extends AbstractActivitiAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger =  LoggerFactory.getLogger(TaskAction.class);
	protected ObjectMapper objectMapper = new ObjectMapper();	
	private String taskId;
	private String redirectUrl;
	private String redirectUrlTaskEntry;
	private IActivitiManager _activitiManager;	
	
	
	public TaskResponse getCurrentTask(){
		TaskResponse ret = null;
		
		UserDetails currentUser = this.getCurrentUser();
		try{
			ret = this.getActivitiManager().getTask(currentUser.getUsername(),currentUser.getPassword(), this.getTaskId());
		}catch (ApsSystemException ex) {
			_logger.error("error in getCurrentTask", ex);
		}	
		return ret;
		
	}
	public List<TaskResponse> getTaskResponseListAssignee(){
		List<TaskResponse> ret = null;
		
		UserDetails currentUser = this.getCurrentUser();
		try {
			ret = this.getActivitiManager().getTaskResponseListAssignee(currentUser.getUsername(),currentUser.getPassword());
		} catch (ApsSystemException ex) {
			_logger.error("error in getTaskResponseList", ex);
		}		
		
		return ret;
	}
	
	public List<TaskResponse> getTaskResponseListCandidateUser(){
		List<TaskResponse> ret = null;
		
		UserDetails currentUser = this.getCurrentUser();
		try {
			ret = this.getActivitiManager().getTaskResponseListCandidateUser(currentUser.getUsername(),currentUser.getPassword());
		} catch (ApsSystemException ex) {
			_logger.error("error in getTaskResponseList", ex);
		}		
		
		return ret;
	}
	
	public FormDataResponse getFormData(){
		FormDataResponse ret = null;
		
		UserDetails currentUser = this.getCurrentUser();
		try {
			ret = this.getActivitiManager().getFormDataResponse(currentUser.getUsername(),currentUser.getPassword(),this.getTaskId() );
		} catch (ApsSystemException ex) {
			_logger.error("error in getFormData", ex);
		}
		
		return ret;
	}
	
	
	public String completeTaskEntry(){		
		UserDetails currentUser = this.getCurrentUser();
		TaskResponse newTaskResponse = null;
		String processInstanceId;
		
		ObjectNode requestNode = objectMapper.createObjectNode();
	    requestNode.put("taskId", this.getTaskId());
	    ArrayNode propertyArray = objectMapper.createArrayNode();
	    requestNode.set("properties", propertyArray);
	    ObjectNode propNode;	    
	    
	    FormDataResponse formDataResponse = this.getFormData();
	    
	    
		for(RestFormProperty each: formDataResponse.getFormProperties()){
			propNode = objectMapper.createObjectNode();
			propNode.put("id", each.getId());
			propNode.put("value", this.getParameter(each.getId()));
			propertyArray.add(propNode);			
		}
		
		try {	
			processInstanceId = this.getActivitiManager().getTask(currentUser.getUsername(), currentUser.getPassword(), this.getTaskId()).getProcessInstanceId();
			this.getActivitiManager().completeTaskEntry(currentUser.getUsername(),currentUser.getPassword(), requestNode);
			
			List<TaskResponse> taskResponseList = this.getActivitiManager().getTaskResponseListAssignee(currentUser.getUsername(), currentUser.getPassword());
			for(TaskResponse each: taskResponseList){
				if(each.getProcessInstanceId().equals(processInstanceId)){
					newTaskResponse = each;
				}
			}
		} catch (ApsSystemException ex) {
			_logger.error("error in completeTaskEntry", ex);
		}
		
		if(newTaskResponse!=null){
			String newRedirectUrlTaskEntry = this.getRedirectUrlTaskEntry() + "?taskId=" + newTaskResponse.getId();
			this.setRedirectUrlTaskEntry(newRedirectUrlTaskEntry);
			return "gotaskentry";
		}
		
		return SUCCESS;
	}
	
	public String startProcessInstance(){		
		UserDetails currentUser = this.getCurrentUser();
		TaskResponse newTaskResponse = null;
		
		try {
			ProcessInstanceResponse newProcessInstance = this.getActivitiManager().startProcessInstance(currentUser.getUsername(), currentUser.getPassword(), this.getParameter("processDefinitionId"));
			System.out.println("Process Instance ID: " + newProcessInstance.getId());
			
			List<TaskResponse> taskResponseList = this.getActivitiManager().getTaskResponseListAssignee(currentUser.getUsername(), currentUser.getPassword());
			for(TaskResponse each: taskResponseList){
				if(each.getProcessInstanceId().equals(newProcessInstance.getId())){
					newTaskResponse = each;
				}
			}
			
		} catch (ApsSystemException ex) {
			_logger.error("error in startProcessInstance", ex);
		}
		
		if(newTaskResponse!=null){
			String newRedirectUrlTaskEntry = this.getRedirectUrlTaskEntry() + "?taskId=" + newTaskResponse.getId();
			this.setRedirectUrlTaskEntry(newRedirectUrlTaskEntry);
			return "gotaskentry";
		}
		return SUCCESS;
	}
	
		
	
	public String claimTask(){
		
		UserDetails currentUser = this.getCurrentUser();
		String taskId = this.getTaskId();
		ObjectNode requestNode = objectMapper.createObjectNode();
	    requestNode.put("action", "claim");
	    requestNode.put("assignee", currentUser.getUsername());
	    
	    try {
			this.getActivitiManager().claimTask(currentUser.getUsername(),currentUser.getPassword(), taskId, requestNode);
		} catch (ApsSystemException ex) {
			_logger.error("error in claimTask", ex);
		}
	    
		return SUCCESS;
	}	
	
	
	public IActivitiManager getActivitiManager() {
		return _activitiManager;
	}

	public void setActivitiManager(IActivitiManager activitiManager) {
		this._activitiManager = activitiManager;
	}	
	
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	

	

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrlTaskEntry() {
		return redirectUrlTaskEntry;
	}

	public void setRedirectUrlTaskEntry(String redirectUrlTaskEntry) {
		this.redirectUrlTaskEntry = redirectUrlTaskEntry;
	}	
		
}
