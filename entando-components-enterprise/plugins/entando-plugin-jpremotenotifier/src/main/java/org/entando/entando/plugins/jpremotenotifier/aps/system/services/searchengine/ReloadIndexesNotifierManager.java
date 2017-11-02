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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.searchengine;

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.searchengine.event.RemoteReloadIndexesEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.searchengine.event.RemoteReloadIndexesObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ReloadIndexesNotifierManager extends AbstractNotifierInterceptorManager implements RemoteReloadIndexesObserver {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ReloadIndexesNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromReloadIndexes(RemoteReloadIndexesEvent event) {
		try {
			String folder = event.getParameters().getProperty("folder");
			ICmsSearchEngineManager searchEngine = this.getSearchEngineManager();
			searchEngine.startReloadContentsReferences(folder);
			this._folders.add(folder);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.searchengine.ISearchEngineDAOFactory.getSearcher(..)) && args(subfolder)")
	public void notifyReloadIndexes(String subfolder) {
		if (this._folders.contains(subfolder)) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		properties.put("folder", subfolder);
		this.notifyRemoteEvent(properties);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (reload indexes)!");
		RemoteReloadIndexesEvent remoteEvent = new RemoteReloadIndexesEvent();
		remoteEvent.setParameters(properties);
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected ICmsSearchEngineManager getSearchEngineManager() {
		return this._searchEngineManager;
	}
	public void setSearchEngineManager(ICmsSearchEngineManager searchEngineManager) {
		this._searchEngineManager = searchEngineManager;
	}
	
	private ConfigInterface _configManager;
	private ICmsSearchEngineManager _searchEngineManager;
	
	private Set<String> _folders = new HashSet<String>();
	
}
