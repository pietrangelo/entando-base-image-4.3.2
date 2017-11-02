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
package com.agiletec.plugins.jpforum.apsadmin.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpforum.aps.system.services.config.ForumConfig;
import com.agiletec.plugins.jpforum.aps.system.services.config.IConfigManager;

public class ConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ConfigAction.class);

	public String edit() {
		try {
			this.setConfig(this.getConfigManager().getConfig());
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			ForumConfig config = this.getConfig();
			this.getConfigManager().updateConfig(config);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<String> getAttributes() {
		List<String> attributes = new ArrayList<String>();
		String currentTypeCode = this.getConfig().getProfileTypecode();
		List<AttributeInterface> profileAttributes = this.getUserProfileManager().getProfileType(currentTypeCode).getAttributeList();
		Iterator<AttributeInterface> it = profileAttributes.iterator();
		while (it.hasNext()) {
			AttributeInterface attr = it.next();
			if (attr instanceof MonoTextAttribute) {
				MonoTextAttribute monotextAttr = (MonoTextAttribute) attr;
				if (monotextAttr.isSearcheable()) {
					attributes.add(monotextAttr.getName());
				}
			}
		}
		return attributes;
	}
	
	
	public void setConfig(ForumConfig config) {
		this._config = config;
	}
	public ForumConfig getConfig() {
		return _config;
	}
	
	protected IConfigManager getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(IConfigManager configManager) {
		this._configManager = configManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private ForumConfig _config;
	private IConfigManager _configManager;
	private IUserProfileManager _userProfileManager;
	
}
