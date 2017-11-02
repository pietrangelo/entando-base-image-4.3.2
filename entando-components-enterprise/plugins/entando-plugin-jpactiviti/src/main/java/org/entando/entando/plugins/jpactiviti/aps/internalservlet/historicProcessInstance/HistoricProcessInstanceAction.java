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

package org.entando.entando.plugins.jpactiviti.aps.internalservlet.historicProcessInstance;

import java.util.List;

import org.activiti.rest.service.api.history.HistoricProcessInstanceResponse;
import org.activiti.rest.service.api.history.HistoricTaskInstanceResponse;
import org.entando.entando.plugins.jpactiviti.aps.system.AbstractActivitiAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;


/**
 * 
 */
public class HistoricProcessInstanceAction extends AbstractActivitiAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger =  LoggerFactory.getLogger(HistoricProcessInstanceAction.class);
	private String redirectUrl;
	private String historicProcessInstanceId;
	
	
	public List<HistoricProcessInstanceResponse> getHistoricProcessInstanceResponseList(){
		List<HistoricProcessInstanceResponse> ret = null;
		UserDetails currentUser = this.getCurrentUser();
		try {
			ret = this.getActivitiManager().getHistoricProcessInstanceResponseList(currentUser.getUsername(),currentUser.getPassword());
		} catch (ApsSystemException ex) {
			_logger.error("error in getProcessInstanceResponseList", ex);
		}		
		return ret;
	}
	
	public List<HistoricTaskInstanceResponse> getHistoricTaskInstanceResponseList(){
		List<HistoricTaskInstanceResponse> ret = null;
		
		UserDetails currentUser = this.getCurrentUser();
		try {
			ret = this.getActivitiManager().getHistoricTaskInstanceResponseList(currentUser.getUsername(), currentUser.getPassword(), this.getHistoricProcessInstanceId());
		} catch (ApsSystemException ex) {
			_logger.error("error in getHistoricTaskInstanceResponseList", ex);
		}
		return ret;
		
	}
	
		

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

		
	public String getMimeType() {
		return "image/png";
	}

	
	public String getHistoricProcessInstanceId() {
		return historicProcessInstanceId;
	}

	public void setHistoricProcessInstanceId(String historicProcessInstanceId) {
		this.historicProcessInstanceId = historicProcessInstanceId;
	}
	

}
