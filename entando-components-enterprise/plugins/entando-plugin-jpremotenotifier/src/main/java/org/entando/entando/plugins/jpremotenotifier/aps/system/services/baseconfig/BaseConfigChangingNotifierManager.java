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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.baseconfig;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.baseconfig.event.RemoteBaseConfigChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.baseconfig.event.RemoteBaseConfigChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

@Aspect
public class BaseConfigChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteBaseConfigChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(BaseConfigChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromBaseConfigChanging(RemoteBaseConfigChangingEvent event) {
		//NOTHING TO DO
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.baseconfig.ConfigInterface.updateConfigItem(..)) && args(name, config)")
	public void notifyRefresh(String name, String config) {
		if (!name.equals(JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR)) {
			this.notifyRemoteEvent(null);
		}
	}
	
	@Override
	public void notifyRomoteRefresh() {
		this.notifyRemoteEvent(null);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Invio notifica refresh (configuration)!");
		RemoteBaseConfigChangingEvent remoteEvent = new RemoteBaseConfigChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	private ConfigInterface _configManager;
	
}
