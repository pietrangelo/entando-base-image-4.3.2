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
package org.entando.entando.plugins.jpsugarcrm.aps.internalservlet;

import org.entando.entando.plugins.jpsugarcrm.aps.services.client.ISugarCRMClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public class SugarRedirectAction extends BaseAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SugarRedirectAction.class);
	
	public String executeRedirect() throws Exception {
		try {
			String url = this.getSugarCRMClientManager().executeLogin(this.getCurrentUser(), null);
			this.setSugarRedirectUrl(url);
		} catch (Throwable t) {
			_logger.error("Error redirection to SugarCRM", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String getSugarRedirectUrl() {
		return _sugarRedirectUrl;
	}
	protected void setSugarRedirectUrl(String sugarRedirectUrl) {
		this._sugarRedirectUrl = sugarRedirectUrl;
	}
	
	protected ISugarCRMClientManager getSugarCRMClientManager() {
		return _sugarCRMClientManager;
	}
	public void setSugarCRMClientManager(ISugarCRMClientManager sugarCRMClientManager) {
		this._sugarCRMClientManager = sugarCRMClientManager;
	}
	
	private String _sugarRedirectUrl;
	private ISugarCRMClientManager _sugarCRMClientManager;
	
}
