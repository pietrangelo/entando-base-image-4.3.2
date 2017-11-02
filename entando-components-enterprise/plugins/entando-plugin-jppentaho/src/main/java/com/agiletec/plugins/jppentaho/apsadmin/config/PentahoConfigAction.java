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
package com.agiletec.plugins.jppentaho.apsadmin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jppentaho.aps.system.services.config.IPentahoConfigManager;
import com.agiletec.plugins.jppentaho.aps.system.services.config.PentahoConfig;

public class PentahoConfigAction extends BaseAction implements IPentahoConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(PentahoConfigAction.class);

	@Override
	public String edit() {
		try {
			PentahoConfig config = this.getPentahoConfigManager().getConfig();
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			this.getPentahoConfigManager().updateConfig(this.getConfig());
			this.addActionMessage(this.getText("jppentaho.message.config.successfullyUpdated"));
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public PentahoConfig getConfig() {
		return _config;
	}
	public void setConfig(PentahoConfig config) {
		this._config = config;
	}
	
	protected IPentahoConfigManager getPentahoConfigManager() {
		return _pentahoConfigManager;
	}
	public void setPentahoConfigManager(IPentahoConfigManager pentahoConfigManager) {
		this._pentahoConfigManager = pentahoConfigManager;
	}
	
	private PentahoConfig _config;
	
	private IPentahoConfigManager _pentahoConfigManager;
	
}