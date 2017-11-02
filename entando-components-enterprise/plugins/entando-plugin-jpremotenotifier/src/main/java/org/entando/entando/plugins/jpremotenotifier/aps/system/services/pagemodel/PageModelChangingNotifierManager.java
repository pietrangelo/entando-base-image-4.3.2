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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.pagemodel;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.util.ApsProperties;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.pagemodel.event.RemotePageModelChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.pagemodel.event.RemotePageModelChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PageModelChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemotePageModelChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(PageModelChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromPageModelChanging(RemotePageModelChangingEvent event) {
		try {
			_logger.debug("Event received (Page Model): {}", event.getEventID());
			((IManager) this.getPageModelManager()).refresh();
			((IManager) this.getPageManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.pagemodel.IPageModelManager.addPageModel(..))")
	public void notifyAdd() {
		//System.out.println("Notifying event: add");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.pagemodel.IPageModelManager.updatePageModel(..))")
	public void notifyUpdate() {
		//System.out.println("Notifying event: update");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.pagemodel.IPageModelManager.deletePageModel(..))")
	public void notifyDelete() {
		//System.out.println("Notifying event: delete");
		this.notifyRemoteEvent(null);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (page model)!");
		RemotePageModelChangingEvent remoteEvent = new RemotePageModelChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}
	
	private IPageManager _pageManager;
	private IPageModelManager _pageModelManager;
	
}
