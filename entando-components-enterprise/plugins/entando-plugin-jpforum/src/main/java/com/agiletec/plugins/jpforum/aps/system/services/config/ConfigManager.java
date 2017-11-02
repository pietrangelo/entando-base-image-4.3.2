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
package com.agiletec.plugins.jpforum.aps.system.services.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;

public class ConfigManager extends AbstractService implements IConfigManager {

	private static final Logger _logger =  LoggerFactory.getLogger(ConfigManager.class);

	@Override
	public void init() throws Exception {
		this.loadConfig();
		
	}
	protected void loadConfig() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpforumSystemConstants.FORUM_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpforumSystemConstants.FORUM_CONFIG_ITEM);
			}
			ForumConfigDOM configDOM = new ForumConfigDOM();
			ForumConfig config = configDOM.extractConfig(xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error initializing the forum configuration", t);
			throw new ApsSystemException("Error initializing the forum configuration", t);
		}
	}
	
	public void updateConfig(ForumConfig config) throws ApsSystemException {
		try {
			String xml = new ForumConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpforumSystemConstants.FORUM_CONFIG_ITEM, xml);
			this.loadConfig();
		} catch (Throwable t) {
			_logger.error("Error updating the forum configuration", t);
			throw new ApsSystemException("Error updating the forum configuration", t);
		}
	}
	
	public int getPostsPerPage() {
		return this.getConfig().getPostsPerPage();
	}

	public int getAttachsPerPost() {
		return this.getConfig().getAttachsPerPost();
	}

	public String getProfileNickAttributeName() {
		return this.getConfig().getProfileNickAttributeName();
	}

	public String getProfileTypecode() {
		return this.getConfig().getProfileTypecode();
	}
	
	@Override
	public ForumConfig getConfig() {
		return _config;
	}

	public void setConfig(ForumConfig config) {
		this._config = config;
	}
	
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}

	private ForumConfig _config;
	private ConfigInterface _configManager;

}
