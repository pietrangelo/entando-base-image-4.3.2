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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.api;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.api.IApiCatalogManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.api.event.RemoteApiCatalogChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.api.event.RemoteApiCatalogChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.ApsProperties;

@Aspect
public class ApiChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteApiCatalogChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromApiCatalogChange(RemoteApiCatalogChangingEvent event) {
		_logger.debug("Ricevuto evento RemoteApiCatalogChangingEvent: {}", event.getEventID());
		try {
			((IManager)this.getCatalogManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}

	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.api.IApiCatalogManager.updateMethodConfig(..))")
	public void notifyUpdateMethodConfig() {
		this.notifyRemoteEvent(null);
	}

	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.api.IApiCatalogManager.resetMethodConfig(..))")
	public void notifyResetMethodConfig() {
		this.notifyRemoteEvent(null);
	}

	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.api.IApiCatalogManager.saveService(..))")
	public void notifySaveService() {
		this.notifyRemoteEvent(null);
	}

	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.api.IApiCatalogManager.deleteService(..))")
	public void notifyDeleteService() {
		this.notifyRemoteEvent(null);
	}
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.api.IApiCatalogManager.updateService(..))")
	public void notifyUpdateService() {
		this.notifyRemoteEvent(null);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Invio RemoteApiCatalogChangingEvent!");
		RemoteApiCatalogChangingEvent remoteEvent = new RemoteApiCatalogChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public IApiCatalogManager getCatalogManager() {
		return _catalogManager;
	}
	public void setCatalogManager(IApiCatalogManager catalogManager) {
		this._catalogManager = catalogManager;
	}
	
	private IApiCatalogManager _catalogManager;
	
}
