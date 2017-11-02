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
package org.entando.entando.plugins.jpsalesforce.apsadmin.config;

import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.ISalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

public class SalesforceConfigAction extends BaseAction implements ISalesforceConfigAction {
	
	Logger _logger = LoggerFactory.getLogger(SalesforceConfigAction.class);
	
	@Override
	public String edit() {
		try {
			_config = this.getSalesforceManager().getConfig();
		} catch (Throwable t) {
			_logger.error("Error editing configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		SalesforceConfig config = this.getConfig();
		
		try {
			if (_resetEndopoint) {
				getSalesforceManager().resetEndpoints(config);
				_logger.info("Enpoints resetted as requested");
			} else {
				getSalesforceManager().updateConfiguration(config);
				_logger.info("Configuration updated");
			}
		} catch (Throwable t) {
			_logger.error("Error saving configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String test() {
		SalesforceConfig config = this.getConfig();
		
		try {
			getSalesforceManager().updateConfiguration(config);
			if (config.isValidForUnmannedLogin()) {
				SalesforceAccessDescriptor sad = getSalesforceManager().login();
				addActionMessage(getText("jpsalesforce.test.ok"));
				_logger.info("Configuration verified");
			} else {
				addActionError(getText("jpsalesforce.test.missing.parameters"));
				_logger.info("Missing configuration parameters");
			}
		} catch (Throwable t) {
			addActionError(getText("jpsalesforce.test.invalid.configuration"));
			_logger.info("Invalid configuration detected");
		}
		return SUCCESS;
	}
	
	public boolean isResetEndopoint() {
		return _resetEndopoint;
	}

	public void setResetEndopoint(boolean resetEndopoint) {
		this._resetEndopoint = resetEndopoint;
	}

	public SalesforceConfig getConfig() {
		return _config;
	}

	public void setConfig(SalesforceConfig config) {
		this._config = config;
	}

	public ISalesforceManager getSalesforceManager() {
		return _salesforceManager;
	}

	public void setSalesforceManager(ISalesforceManager salesforceManager) {
		this._salesforceManager = salesforceManager;
	}

	private SalesforceConfig _config;
	private boolean _resetEndopoint;
	
	private ISalesforceManager _salesforceManager;
}
