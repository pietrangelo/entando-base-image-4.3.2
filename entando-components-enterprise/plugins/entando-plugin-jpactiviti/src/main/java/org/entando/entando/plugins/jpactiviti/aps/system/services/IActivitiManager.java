package org.entando.entando.plugins.jpactiviti.aps.system.services;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.activiti.rest.service.api.form.FormDataResponse;
import org.activiti.rest.service.api.history.HistoricProcessInstanceResponse;
import org.activiti.rest.service.api.history.HistoricTaskInstanceResponse;
import org.activiti.rest.service.api.repository.DeploymentResponse;
import org.activiti.rest.service.api.repository.ProcessDefinitionResponse;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.entando.entando.plugins.jpactiviti.aps.system.services.TaskResponse;

public interface IActivitiManager {

	public List<ProcessDefinitionResponse> getProcessDefinitionResponseList(String username, String password) throws ApsSystemException;
	public List<ProcessInstanceResponse> getProcessInstanceResponseList(String username, String password) throws ApsSystemException;
	public List<HistoricProcessInstanceResponse> getHistoricProcessInstanceResponseList(String username, String password) throws ApsSystemException;
	public TaskResponse getTask(String username, String password, String taskId) throws ApsSystemException;
	public List<TaskResponse> getTaskResponseListAssignee(String username, String password) throws ApsSystemException;
	public List<TaskResponse> getTaskResponseListCandidateUser(String username, String password) throws ApsSystemException;
	public FormDataResponse getFormDataResponse(String username, String password, String taskId) throws ApsSystemException;
	public void completeTaskEntry(String username, String password, ObjectNode requestNode) throws ApsSystemException;
	public DeploymentResponse createNewDeployment(String username, String password,String attachmentFileName, File attachment) throws ApsSystemException;
	public void claimTask(String username, String password,String taskId,  ObjectNode requestNode) throws ApsSystemException;
	public ProcessInstanceResponse startProcessInstance(String username, String password, String processDefinitionId ) throws ApsSystemException;
	public List<HistoricTaskInstanceResponse> getHistoricTaskInstanceResponseList(String username, String password, String historicProcessInstanceId) throws ApsSystemException;
	public InputStream returnProcessInstanceImageStream(String username, String password, String processInstanceId) throws ApsSystemException;
		
}
