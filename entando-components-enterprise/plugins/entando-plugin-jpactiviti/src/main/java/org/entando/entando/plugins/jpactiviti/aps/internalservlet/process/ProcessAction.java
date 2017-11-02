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

package org.entando.entando.plugins.jpactiviti.aps.internalservlet.process;

import java.io.InputStream;
import java.util.List;

import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.entando.entando.plugins.jpactiviti.aps.system.AbstractActivitiAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;


/**
 * 
 */
public class ProcessAction extends AbstractActivitiAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger =  LoggerFactory.getLogger(ProcessAction.class);
	private String processInstanceId;
	private InputStream inputStream;		
	
		
	public List<ProcessInstanceResponse> getProcessInstanceResponseList(){
		List<ProcessInstanceResponse> ret = null;
		UserDetails currentUser = this.getCurrentUser();
		
		try {
			ret = this.getActivitiManager().getProcessInstanceResponseList(currentUser.getUsername(),currentUser.getPassword());
		} catch (ApsSystemException ex) {
			_logger.error("error in getProcessInstanceResponseList", ex);
		}
		return ret;
	}
		
	public String returnProcessInstanceImageStream() {
		UserDetails currentUser = this.getCurrentUser();
		InputStream is;
		try {
			is = this.getActivitiManager().returnProcessInstanceImageStream(currentUser.getUsername(), currentUser.getPassword(), this.getProcessInstanceId());
			this.setInputStream(is);
		} catch (Throwable t) {
			_logger.error("error in returnProcessInstanceImageStream", t);
            return FAILURE;
        }
		return SUCCESS;
	}
	

	public String getMimeType() {
		return "image/png";
	}

	
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}	

}
