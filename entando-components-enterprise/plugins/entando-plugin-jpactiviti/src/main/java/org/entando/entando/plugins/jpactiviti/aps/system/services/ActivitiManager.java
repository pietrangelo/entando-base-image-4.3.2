package org.entando.entando.plugins.jpactiviti.aps.system.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.activiti.rest.service.api.RestUrls;
import org.activiti.rest.service.api.form.FormDataResponse;
import org.activiti.rest.service.api.history.HistoricProcessInstanceResponse;
import org.activiti.rest.service.api.history.HistoricTaskInstanceResponse;
import org.activiti.rest.service.api.repository.DeploymentResponse;
import org.activiti.rest.service.api.repository.ProcessDefinitionResponse;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.entando.entando.plugins.jpactiviti.aps.system.services.TaskResponse; 

public class ActivitiManager extends BaseActivitiManager implements	IActivitiManager {

	private static final long serialVersionUID = 1L;
	private static final Logger _logger = LoggerFactory.getLogger(ActivitiManager.class);
	
	
	public DeploymentResponse createNewDeployment(String username,	String password, String attachmentFileName, File attachment) throws ApsSystemException {
		DeploymentResponse ret = null;
		HttpPost httpPost;
		String baseUrl;
		CloseableHttpResponse response;
		CloseableHttpClient client;
		JsonNode responseNode;		
		
		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_DEPLOYMENT_COLLECTION);
			client = this.getClient(username, password);
			httpPost = new HttpPost(this.getServerRestUrl() + baseUrl);
			httpPost.setEntity(HttpMultipartHelper.getMultiPartEntity(attachmentFileName, "application/xml", new FileInputStream(attachment), null));
			response = executeBinaryRequest(client, httpPost);
			responseNode = this.getObjectMapper().readTree(response.getEntity().getContent());
			ret = this.getObjectMapper().readValue(responseNode.traverse(),DeploymentResponse.class);
			closeResponse(response);		    
		} catch (Throwable t) {
			_logger.error("error in createNewDeployment", t);
			throw new ApsSystemException("error in createNewDeployment", t);
		}
		
		return ret;
	}

	public List<ProcessDefinitionResponse> getProcessDefinitionResponseList(String username, String password) throws ApsSystemException {
		String baseUrl;
		CloseableHttpResponse response;
		CloseableHttpClient client;
		JsonNode dataNode;
	    List<ProcessDefinitionResponse> ret = null;
	    HttpUriRequest request;		  
		
	    try {
	    	baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_PROCESS_DEFINITION_COLLECTION);
	    	client = getClient(username, password);
	    	request = new HttpGet(this.getServerRestUrl() + baseUrl);
	    	response = executeRequest(client,request);
	    	dataNode = this.getObjectMapper().readTree(response.getEntity().getContent()).get("data");
			ret = this.getObjectMapper().readValue(dataNode.traverse(), this.getObjectMapper().getTypeFactory().constructCollectionType(List.class, ProcessDefinitionResponse.class));
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getProcessDefinitionResponseList", t);
			throw new ApsSystemException(
					"error in getProcessDefinitionResponseList", t);
		}
		return ret;
	}
	
		
	public ProcessInstanceResponse startProcessInstance(String username,String password, String processDefinitionId) throws ApsSystemException {
		ProcessInstanceResponse ret = null;
		JsonNode rootNode = null;
		CloseableHttpResponse response = null;
		String baseUrl;
		HttpPost httpPost;
		ObjectNode requestNode;	

		try {
			requestNode = this.getObjectMapper().createObjectNode();
			requestNode.put("processDefinitionId", processDefinitionId);
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_PROCESS_INSTANCE_COLLECTION);
			httpPost = new HttpPost(this.getServerRestUrl() + baseUrl);
			httpPost.setEntity(new StringEntity(requestNode.toString()));
			response = executeRequest(getClient(username, password), httpPost);
			rootNode = this.getObjectMapper().readTree(response.getEntity().getContent());
			ret = this.getObjectMapper().readValue(rootNode.traverse(),ProcessInstanceResponse.class);
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in startProcessInstance", t);
			throw new ApsSystemException("error in startProcessInstance", t);
		}		
		return ret;
	}
		
	public List<ProcessInstanceResponse> getProcessInstanceResponseList(String username, String password) throws ApsSystemException {
		List<ProcessInstanceResponse> ret = null;
		String baseUrl;
		CloseableHttpClient client;
		HttpUriRequest request;	
		CloseableHttpResponse response;
		JsonNode dataNode = null;

		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_PROCESS_INSTANCE_COLLECTION);
			client = getClient(username, password);
			request = new HttpGet(this.getServerRestUrl() + baseUrl);
			response = executeRequest(client, request);
			dataNode = objectMapper.readTree(response.getEntity().getContent()).get("data");
			ret = objectMapper.readValue(dataNode.traverse(), objectMapper.getTypeFactory().constructCollectionType(List.class, ProcessInstanceResponse.class));
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getProcessInstanceResponseList", t);
			throw new ApsSystemException(
					"error in getProcessInstanceResponseList", t);
		}		
		return ret;
	}

	
	public InputStream returnProcessInstanceImageStream(String username, String password, String processInstanceId)	throws ApsSystemException {
		String baseUrl = null;
		InputStream ret = null;
		HttpGet httpGet;
		CloseableHttpClient client;
		CloseableHttpResponse response;	

		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_PROCESS_INSTANCE_DIAGRAM,	processInstanceId);
			httpGet = new HttpGet(this.getServerRestUrl()	+ baseUrl);
			client = getClient(username, password);
			response = executeRequest(client, httpGet );
			ret = response.getEntity().getContent();
			//closeResponse(response);
		} catch (Throwable t) {
			_logger.error(
					"error in activiti manager returnProcessInstanceImageStream",
					t);
			throw new ApsSystemException(
					"error in activiti manager returnProcessInstanceImageStream",
					t);
		}		
		return ret;
	}

	public TaskResponse getTask(String username, String password, String taskId) throws ApsSystemException {
		TaskResponse ret = null;
		String baseUrl = null;
		CloseableHttpClient client;
		HttpGet httpGet;
		CloseableHttpResponse response;
		JsonNode rootNode;		

		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_TASK_COLLECTION) + "/" + taskId;
			client = getClient(username, password);
			httpGet = new HttpGet(this.getServerRestUrl() + baseUrl);
			response = executeRequest(client, httpGet);
			
			rootNode = this.getObjectMapper().readTree(response.getEntity().getContent());
			ret = this.getObjectMapper().readValue(rootNode.traverse(),	TaskResponse.class);
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getTask", t);
			throw new ApsSystemException(
					"error in getTask", t);
		}
		return ret;
	  
	}
	
	public List<TaskResponse> getTaskResponseListAssignee(String username, String password) throws ApsSystemException {
		List<TaskResponse> ret = null;
		String baseUrl = null;
		CloseableHttpClient client;
		HttpGet httpGet;
		CloseableHttpResponse response;
		JsonNode dataNode;		

		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_TASK_COLLECTION) + "?assignee=" + username;
			client = getClient(username, password);
			httpGet = new HttpGet(this.getServerRestUrl() + baseUrl);
			response = executeRequest(client, httpGet);
			
			dataNode = this.getObjectMapper().readTree(response.getEntity().getContent()).get("data");
			
			ret = this.getObjectMapper().readValue(dataNode.traverse(),this.getObjectMapper().getTypeFactory().constructCollectionType(	List.class, TaskResponse.class));
			
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getTaskResponseListAssignee", t);
			throw new ApsSystemException(
					"error in getTaskResponseListAssignee", t);
		}
		return ret;
	}
	
	public List<TaskResponse> getTaskResponseListCandidateUser(String username, String password) throws ApsSystemException {
		List<TaskResponse> ret = null;
		String baseUrl = null;
		CloseableHttpClient client;
		HttpGet httpGet;
		CloseableHttpResponse response;
		JsonNode dataNode;		

		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_TASK_COLLECTION) + "?candidateUser=" + username;
			client = getClient(username, password);
			httpGet = new HttpGet(this.getServerRestUrl() + baseUrl);
			response = executeRequest(client, httpGet);
			
			dataNode = this.getObjectMapper().readTree(response.getEntity().getContent()).get("data");
			
			ret = this.getObjectMapper().readValue(dataNode.traverse(),this.getObjectMapper().getTypeFactory().constructCollectionType(	List.class, TaskResponse.class));
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getTaskResponseListCandidateUser", t);
			throw new ApsSystemException(
					"error in getTaskResponseListCandidateUser", t);
		}
		return ret;
	}

	

	
	public FormDataResponse getFormDataResponse(String username, String password, String taskId) throws ApsSystemException {
		FormDataResponse ret = null;
		String baseUrl = null;
		CloseableHttpClient client;
		HttpGet httpGet;
		CloseableHttpResponse response;
		JsonNode rootNode = null;
		
		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_FORM_DATA) + "?taskId=" + taskId;
			client = getClient(username, password);
			httpGet = new HttpGet(this.getServerRestUrl() + baseUrl);
			response = executeRequest(client,	httpGet);
			rootNode = this.getObjectMapper().readTree(response.getEntity().getContent());
			ret = this.getObjectMapper().readValue(rootNode.traverse(),	FormDataResponse.class);
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getFormDataResponse", t);
			throw new ApsSystemException("error in getFormDataResponse", t);
		}		
		return ret;
	}

		
	public void completeTaskEntry(String username, String password,	ObjectNode requestNode) throws ApsSystemException {
		String baseUrl = null;
		HttpPost httpPost;
		CloseableHttpClient client;
		CloseableHttpResponse response = null;		

		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_FORM_DATA);
			httpPost = new HttpPost(this.getServerRestUrl() + baseUrl);			
			httpPost.setEntity(new StringEntity(requestNode.toString()));
			client = getClient(username, password);
			response = executeRequest(client, httpPost);			
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in completeTaskEntry", t);
			throw new ApsSystemException("error in completeTaskEntry", t);
		}
	}

	
	
	public void claimTask(String username, String password, String taskId,	ObjectNode requestNode) throws ApsSystemException {
		String baseUrl = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost;
		CloseableHttpClient client;

		
		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_TASK, taskId);
			httpPost = new HttpPost(this.getServerRestUrl() + baseUrl);
			httpPost.setEntity(new StringEntity(requestNode.toString()));
			client = getClient(username, password);
			response = executeRequest(client, httpPost);
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in claimTask", t);
			throw new ApsSystemException("error in claimTask", t);
		}
	}

	
	

		
	public List<HistoricProcessInstanceResponse> getHistoricProcessInstanceResponseList(String username, String password) throws ApsSystemException {
		List<HistoricProcessInstanceResponse> ret = null;
		String baseUrl = null;
		CloseableHttpClient client;
		HttpGet httpGet;
		CloseableHttpResponse response;
		JsonNode dataNode;		
		
		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_HISTORIC_PROCESS_INSTANCES);
			client = this.getClient(username, password);
			httpGet = new HttpGet(this.getServerRestUrl() + baseUrl);
			response = executeRequest(client,	httpGet);
			dataNode = this.getObjectMapper().readTree(response.getEntity().getContent()).get("data");
			ret = objectMapper.readValue(dataNode.traverse(),objectMapper.getTypeFactory().constructCollectionType(List.class, HistoricProcessInstanceResponse.class));
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getHistoricProcessInstanceResponseList", t);
			throw new ApsSystemException(
					"error in getHistoricProcessInstanceResponseList", t);
		}
		return ret;
	}
	
	
		
	public List<HistoricTaskInstanceResponse> getHistoricTaskInstanceResponseList(String username, String password, String historicProcessInstanceId) throws ApsSystemException {
		List<HistoricTaskInstanceResponse> ret = null;
		String baseUrl = null;
		CloseableHttpClient client;
		HttpGet httpGet;
		CloseableHttpResponse response;
		JsonNode dataNode;
		
		try {
			baseUrl = RestUrls.createRelativeResourceUrl(RestUrls.URL_HISTORIC_TASK_INSTANCES)	+ "?processInstanceId=" + historicProcessInstanceId;
			client = this.getClient(username, password);
			httpGet = new HttpGet(this.getServerRestUrl() + baseUrl);
			response = executeRequest(client,	httpGet);
			dataNode = this.getObjectMapper().readTree(response.getEntity().getContent()).get("data");
			ret = objectMapper.readValue(dataNode.traverse(),objectMapper.getTypeFactory().constructCollectionType(List.class, HistoricTaskInstanceResponse.class));
			closeResponse(response);
		} catch (Throwable t) {
			_logger.error("error in getHistoricTaskInstanceResponseList", t);
			throw new ApsSystemException(
					"error in getHistoricTaskInstanceResponseList", t);
		}
		return ret;
	}

		
	public String getServerRestUrl() {
		return this.getConfigManager().getParam(JpactivitiSystemConstants.PARAM_URL_REST) + "/service/";
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	
	private ConfigInterface _configManager;
	
}
