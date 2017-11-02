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

package org.entando.entando.plugins.jpactiviti.aps.internalservlet.processDefinition;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.activiti.rest.service.api.repository.ProcessDefinitionResponse;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.entando.entando.plugins.jpactiviti.aps.system.AbstractActivitiAction;
import org.entando.entando.plugins.jpactiviti.aps.system.services.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;


/**
 * 
 */
public class ProcessDefinitionAction extends AbstractActivitiAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger =  LoggerFactory.getLogger(ProcessDefinitionAction.class);
	private String redirectUrl;
	private String redirectUrlTaskEntry;
	private File attachment;
	private String attachmentFileName;
	private File attachmentContentType;
	private InputStream inputStream;		
	
	public List<ProcessDefinitionResponse> getProcessDefinitionResponseList(){
		List<ProcessDefinitionResponse> ret = null;
		UserDetails currentUser = this.getCurrentUser();
		try {
			ret = this.getActivitiManager().getProcessDefinitionResponseList(currentUser.getUsername(), currentUser.getPassword());
		} catch (ApsSystemException ex) {
			_logger.error("error in getProcessDefinitionResponseList "+ currentUser.getUsername() + "  " + currentUser.getPassword()  , ex);
		}
		return ret;
	}
	
	public String createNewDeployment() {
		UserDetails currentUser = this.getCurrentUser();
		String fileName = this.getAttachmentFileName();
		File file = this.getAttachment();
				
		try {
			this.getActivitiManager().createNewDeployment(currentUser.getUsername(), currentUser.getPassword(),fileName, file);
		} catch (ApsSystemException e) {
			_logger.error("error in createNewDeployment", e);
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

	public File getAttachment() {
		return attachment;
	}

	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}

	public File getAttachmentContentType() {
		return attachmentContentType;
	}
	
	public String getMimeType() {
		return "image/png";
	}

	public void setAttachmentContentType(File attachmentContentType) {
		this.attachmentContentType = attachmentContentType;
	}

	
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
	
	
	

}
